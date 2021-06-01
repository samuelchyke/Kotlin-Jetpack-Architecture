package com.example.kotlin_jetpack_architecture.di.main

import com.example.kotlin_jetpack_architecture.api.main.OpenApiMainService
import com.example.kotlin_jetpack_architecture.persistence.AccountPropertiesDao
import com.example.kotlin_jetpack_architecture.persistence.AppDatabase
import com.example.kotlin_jetpack_architecture.persistence.BlogPostDao
import com.example.kotlin_jetpack_architecture.repository.main.AccountRepository
import com.example.kotlin_jetpack_architecture.repository.main.BlogRepository
import com.example.kotlin_jetpack_architecture.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder): OpenApiMainService {
        return retrofitBuilder
            .build()
            .create(OpenApiMainService::class.java)
    }

    @MainScope
    @Provides
    fun provideAccountRepository(
        openApiMainService: OpenApiMainService,
        accountPropertiesDao: AccountPropertiesDao,
        sessionManager: SessionManager
    ): AccountRepository {
        return AccountRepository(openApiMainService, accountPropertiesDao, sessionManager)
    }


    @MainScope
    @Provides
    fun provideBlogPostDao(db: AppDatabase): BlogPostDao {
        return db.getBlogPostDao()
    }

    @MainScope
    @Provides
    fun provideBlogRepository(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ): BlogRepository {
        return BlogRepository(openApiMainService, blogPostDao, sessionManager)
    }

}

