package com.example.kotlin_jetpack_architecture.di.auth
import com.example.kotlin_jetpack_architecture.api.auth.OpenApiAuthService
import com.example.kotlin_jetpack_architecture.persistence.AccountPropertiesDao
import com.example.kotlin_jetpack_architecture.persistence.AuthTokenDao
import com.example.kotlin_jetpack_architecture.repository.auth.AuthRepository
import com.example.kotlin_jetpack_architecture.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class AuthModule{

    // TEMPORARY
    @AuthScope
    @Provides
    fun provideFakeApiService(retrofitBuilder: Retrofit.Builder): OpenApiAuthService{
        return retrofitBuilder
            .build()
            .create(OpenApiAuthService::class.java)
    }

    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        openApiAuthService: OpenApiAuthService
    ): AuthRepository {
        return AuthRepository(
            authTokenDao,
            accountPropertiesDao,
            openApiAuthService,
            sessionManager
        )
    }

}
