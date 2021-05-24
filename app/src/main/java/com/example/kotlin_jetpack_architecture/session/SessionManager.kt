package com.example.kotlin_jetpack_architecture.session

import android.app.Application
import com.example.kotlin_jetpack_architecture.persistance.AuthTokenDao

class SessionManager
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
    ) {

}