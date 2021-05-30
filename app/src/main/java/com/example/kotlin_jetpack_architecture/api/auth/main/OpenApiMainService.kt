package com.example.kotlin_jetpack_architecture.api.auth.main

import androidx.lifecycle.LiveData
import com.example.kotlin_jetpack_architecture.models.AccountProperties
import com.example.kotlin_jetpack_architecture.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import java.sql.SQLInvalidAuthorizationSpecException

interface OpenApiMainService {

    @GET("account/properties")
    fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): LiveData<GenericApiResponse<AccountProperties>>
}