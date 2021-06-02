package com.example.kotlin_jetpack_architecture.util

class Constants {

    companion object{

        const val PASSWORD_RESET_URL: String = "https://open-api.xyz/password_reset/"
        const val BASE_URL = "https://open-api.xyz/api/"

        const val NETWORK_TIMEOUT = 6000L
        const val TESTING_NETWORK_DELAY = 3000L // fake network delay for testing
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing

        const val PAGINATION_PAGE_SIZE = 10
    }
}