package com.example.kotlin_jetpack_architecture.api.auth.main

import androidx.lifecycle.LiveData
import com.example.kotlin_jetpack_architecture.api.GenericResponse
import com.example.kotlin_jetpack_architecture.models.AccountProperties
import com.example.kotlin_jetpack_architecture.util.GenericApiResponse
import retrofit2.http.*
import java.sql.SQLInvalidAuthorizationSpecException

interface OpenApiMainService {

    @GET("account/properties")
    fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): LiveData<GenericApiResponse<AccountProperties>>


    @PUT("account/properties/update")
    @FormUrlEncoded
    fun saveAccountProperties(
        @Header("Authorization") authorization: String,
        @Field("email") email: String,
        @Field  ("username") username: String,
    ): LiveData<GenericApiResponse<GenericResponse>>
}