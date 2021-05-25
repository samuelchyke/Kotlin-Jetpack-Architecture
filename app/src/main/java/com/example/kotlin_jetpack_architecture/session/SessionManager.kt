package com.example.kotlin_jetpack_architecture.session

import android.app.Application
import com.example.kotlin_jetpack_architecture.persistence.AuthTokenDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
    ) {

}