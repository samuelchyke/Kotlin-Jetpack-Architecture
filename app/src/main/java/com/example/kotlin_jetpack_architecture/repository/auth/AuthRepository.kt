package com.example.kotlin_jetpack_architecture.repository.auth

import androidx.lifecycle.LiveData
import com.example.kotlin_jetpack_architecture.api.auth.OpenApiAuthService
import com.example.kotlin_jetpack_architecture.api.auth.network_responses.LoginResponse
import com.example.kotlin_jetpack_architecture.api.auth.network_responses.RegistrationResponse
import com.example.kotlin_jetpack_architecture.persistence.AccountPropertiesDao
import com.example.kotlin_jetpack_architecture.persistence.AuthTokenDao
import com.example.kotlin_jetpack_architecture.session.SessionManager
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

    fun testLoginRequest(email: String, password: String): LiveData<GenericApiResponse<LoginResponse>> {
        return openApiAuthService.login(email, password)
    }

    fun testRegistrationRequest(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): LiveData<GenericApiResponse<RegistrationResponse>>{
        return openApiAuthService.register(email, username, password, confirmPassword)
    }
}

