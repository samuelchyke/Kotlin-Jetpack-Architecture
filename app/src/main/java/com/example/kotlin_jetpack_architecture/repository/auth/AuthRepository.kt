package com.example.kotlin_jetpack_architecture.repository.auth

import com.example.kotlin_jetpack_architecture.api.auth.OpenApiAuthService
import com.example.kotlin_jetpack_architecture.persistance.AccountPropertiesDao
import com.example.kotlin_jetpack_architecture.persistance.AuthTokenDao
import com.example.kotlin_jetpack_architecture.session.SessionManager

class AuthRepository
constructor(
    val authTokenDao: AuthTokenDao,
    accountPropertiesDao: AccountPropertiesDao,
    openApiAuthService: OpenApiAuthService,
    sessionManager: SessionManager
){

}