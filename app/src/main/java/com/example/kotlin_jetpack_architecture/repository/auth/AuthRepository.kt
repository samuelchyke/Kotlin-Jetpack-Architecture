package com.example.kotlin_jetpack_architecture.repository.auth

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.kotlin_jetpack_architecture.api.auth.OpenApiAuthService
import com.example.kotlin_jetpack_architecture.api.auth.network_responses.LoginResponse
import com.example.kotlin_jetpack_architecture.api.auth.network_responses.RegistrationResponse
import com.example.kotlin_jetpack_architecture.models.AuthToken
import com.example.kotlin_jetpack_architecture.persistence.AccountPropertiesDao
import com.example.kotlin_jetpack_architecture.persistence.AuthTokenDao
import com.example.kotlin_jetpack_architecture.session.SessionManager
import com.example.kotlin_jetpack_architecture.ui.DataState
import com.example.kotlin_jetpack_architecture.ui.Response
import com.example.kotlin_jetpack_architecture.ui.ResponseType
import com.example.kotlin_jetpack_architecture.ui.auth.state.AuthViewState
import com.example.kotlin_jetpack_architecture.ui.auth.state.LoginFields
import com.example.kotlin_jetpack_architecture.ui.auth.state.RegistrationFields
import com.example.kotlin_jetpack_architecture.util.ApiSuccessResponse
import com.example.kotlin_jetpack_architecture.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import com.example.kotlin_jetpack_architecture.util.GenericApiResponse
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import javax.inject.Inject

private const val TAG = "AuthRepository"
class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager
){

    private var repositoryJob: Job? = null

    @InternalCoroutinesApi
    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>>{
        
        val loginFieldErrors = LoginFields(email,password).isValidForLogin()
        if(loginFieldErrors != LoginFields.LoginError.none()){
            return returnErrorResponse(loginFieldErrors, ResponseType.Dialog())
        }

        return object: NetworkBoundResource<LoginResponse, AuthViewState>(
            sessionManager.isConnectedToTheInternet() == true
        ){
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
                Log.d(TAG, "handleApiSuccessResponse:${response}")

                // Incorrect login credentials counts as a 200 response from server, so need to handle that
                if(response.body.response == GENERIC_AUTH_ERROR){
                    return onErrorReturn(response.body.errorMessage,
                        shouldUseDialog = true,
                        shouldUseToast = false
                    )
                }

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.pk, response.body.token)
                        )
                    )
                )

            }

            override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
                return openApiAuthService.login(email, password)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob= job
            }

        }.asLiveData()
    }

    @InternalCoroutinesApi
    fun attemptRegistration(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>>{

        val registrationFieldErrors = RegistrationFields(email, username, password, confirmPassword).isValidForRegistration()
        if(registrationFieldErrors != RegistrationFields.RegistrationError.none()){
            return returnErrorResponse(registrationFieldErrors, ResponseType.Dialog())
        }

        return object: NetworkBoundResource<RegistrationResponse, AuthViewState>(
            sessionManager.isConnectedToTheInternet() == true
        ){
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<RegistrationResponse>) {

                Log.d(TAG, "handleApiSuccessResponse: $response")

                if(response.body.response == GENERIC_AUTH_ERROR){
                    return onErrorReturn(response.body.errorMessage,
                        shouldUseDialog = true,
                        shouldUseToast = false
                    )
                }

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.pk, response.body.token)
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<RegistrationResponse>> {
                return openApiAuthService.register(email, username, password, confirmPassword)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    private fun returnErrorResponse(errorMessage: String, responseType: ResponseType.Dialog): LiveData<DataState<AuthViewState>> {
        return object : LiveData<DataState<AuthViewState>>(){
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    Response(
                        errorMessage,
                        responseType
                    )
                )
            }

        }
    }

    fun cancelActiveJobs(){
        Log.d(TAG, "AuthRepository: Cancelling on-going jobs...")
        repositoryJob?.cancel()
    }

}



