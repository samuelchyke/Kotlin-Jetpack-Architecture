package com.example.kotlin_jetpack_architecture.repository.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
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
import com.example.kotlin_jetpack_architecture.util.ApiEmptyResponse
import com.example.kotlin_jetpack_architecture.util.ApiErrorResponse
import com.example.kotlin_jetpack_architecture.util.ApiSuccessResponse
import com.example.kotlin_jetpack_architecture.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.example.kotlin_jetpack_architecture.util.GenericApiResponse
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager
){
    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>>{
        return openApiAuthService.login(email, password)
            .switchMap { response ->
                object: LiveData<DataState<AuthViewState>>(){
                    override fun onActive() {
                        super.onActive()
                        when(response){
                            is ApiSuccessResponse ->{
                                value = DataState.data(
                                    AuthViewState(
                                        authToken = AuthToken(response.body.pk, response.body.token)
                                    ),
                                    response = null
                                )
                            }
                            is ApiErrorResponse ->{
                                value = DataState.error(
                                    Response(
                                        message = response.errorMessage,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }
                            is ApiEmptyResponse ->{
                                value = DataState.error(
                                    Response(
                                        message = ERROR_UNKNOWN,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }
                        }
                    }
                }
            }
    }


    fun attemptRegistration(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<DataState<AuthViewState>>{
        return openApiAuthService.register(email, username, password, confirmPassword)
            .switchMap { response ->
                object: LiveData<DataState<AuthViewState>>(){
                    override fun onActive() {
                        super.onActive()
                        when(response){
                            is ApiSuccessResponse ->{
                                value = DataState.data(
                                    AuthViewState(
                                        authToken = AuthToken(response.body.pk, response.body.token)
                                    ),
                                    response = null
                                )
                            }
                            is ApiErrorResponse ->{
                                value = DataState.error(
                                    Response(
                                        message = response.errorMessage,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }
                            is ApiEmptyResponse ->{
                                value = DataState.error(
                                    Response(
                                        message = ERROR_UNKNOWN,
                                        responseType = ResponseType.Dialog()
                                    )
                                )
                            }
                        }
                    }
                }
            }
    }

}



