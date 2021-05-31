package com.example.kotlin_jetpack_architecture.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.kotlin_jetpack_architecture.api.main.OpenApiMainService
import com.example.kotlin_jetpack_architecture.models.AccountProperties
import com.example.kotlin_jetpack_architecture.models.AuthToken
import com.example.kotlin_jetpack_architecture.persistence.AccountPropertiesDao
import com.example.kotlin_jetpack_architecture.repository.auth.NetworkBoundResource
import com.example.kotlin_jetpack_architecture.session.SessionManager
import com.example.kotlin_jetpack_architecture.ui.DataState
import com.example.kotlin_jetpack_architecture.ui.main.account.state.AccountViewState
import com.example.kotlin_jetpack_architecture.util.AbsentLiveData
import androidx.lifecycle.switchMap
import com.example.kotlin_jetpack_architecture.api.GenericResponse
import com.example.kotlin_jetpack_architecture.ui.Response
import com.example.kotlin_jetpack_architecture.ui.ResponseType
import com.example.kotlin_jetpack_architecture.util.ApiSuccessResponse
import com.example.kotlin_jetpack_architecture.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AccountRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val accountPropertiesDao: AccountPropertiesDao,
    val sessionManager: SessionManager
){

    private val TAG = "AppDebug"

    private var repositoryJob: Job? = null

    @InternalCoroutinesApi
    fun getAccountProperties(authToken: AuthToken): LiveData<DataState<AccountViewState>> {
        return object: NetworkBoundResource< AccountProperties, AccountProperties, AccountViewState>(
            isNetworkAvailable = sessionManager.isConnectedToTheInternet() == true,
            isNetworkRequest = true,
            shouldLoadFromCache = true,
            shouldCancelIfNoInternet = false
        ){

            override fun loadFromCache(): LiveData<AccountViewState> {
                return accountPropertiesDao.searchByPk(authToken.account_pk!!)
                    .switchMap{
                        object: LiveData<AccountViewState>(){
                            override fun onActive() {
                                super.onActive()
                                value = AccountViewState(it)
                            }
                        }
                    }
            }

            override suspend fun updateLocalDb(cacheObject: AccountProperties?) {
                cacheObject?.let {
                    accountPropertiesDao.updateAccountProperties(
                        cacheObject.pk,
                        cacheObject.email,
                        cacheObject.username
                    )
                }
            }

            override suspend fun createCacheRequestAndReturn() {
                withContext(Dispatchers.Main){

                    // finishing by viewing db cache
                    result.addSource(loadFromCache()){ viewState ->
                        onCompleteJob(DataState.data(viewState, null))
                    }
                }
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<AccountProperties>) {
                updateLocalDb(response.body)

                createCacheRequestAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<AccountProperties>> {
                return openApiMainService
                    .getAccountProperties(
                        "Token ${authToken.token!!}"
                    )
            }


            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    @InternalCoroutinesApi
    fun saveAccountProperties( authToken: AuthToken, accountProperties: AccountProperties): LiveData<DataState<AccountViewState>>{
        return object: NetworkBoundResource<GenericResponse, Any, AccountViewState>(
            sessionManager.isConnectedToTheInternet() == true,
            isNetworkRequest = true,
            shouldLoadFromCache = false,
            shouldCancelIfNoInternet = true
        ){
            //Not Applicable
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<GenericResponse>) {

                updateLocalDb(null)

                withContext(Main) {

                    onCompleteJob(
                        DataState.data(
                            data = null,
                            response = Response(response.body.response, ResponseType.Toast()))
                    )
                }
            }

            //not Used
            override fun loadFromCache(): LiveData<AccountViewState> {
                return AbsentLiveData.create()
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                return openApiMainService.saveAccountProperties(
                    "Token ${authToken.token!!}",
                    accountProperties.email,
                    accountProperties.username
                )

            }


            override suspend fun updateLocalDb(cacheObject: Any?) {
               return accountPropertiesDao.updateAccountProperties(
                        accountProperties.pk,
                        accountProperties.email,
                        accountProperties.username
                    )
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }
        }.asLiveData()

    }



    fun cancelActiveJobs(){
        Log.d(TAG,"AuthRepository: cancelling on-going jobs....")
    }
}