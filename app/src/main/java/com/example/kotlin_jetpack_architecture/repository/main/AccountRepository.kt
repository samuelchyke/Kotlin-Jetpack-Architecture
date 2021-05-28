package com.example.kotlin_jetpack_architecture.repository.main

import android.util.Log
import com.example.kotlin_jetpack_architecture.api.auth.main.OpenApiMainService
import com.example.kotlin_jetpack_architecture.persistence.AccountPropertiesDao
import com.example.kotlin_jetpack_architecture.session.SessionManager
import kotlinx.coroutines.Job
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

    fun cancelActiveJobs(){
        Log.d(TAG,"AuthRepository: cancelling on-going jobs....")
    }
}