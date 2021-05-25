package com.example.kotlin_jetpack_architecture.di

import com.example.kotlin_jetpack_architecture.di.auth.AuthFragmentBuildersModule
import com.example.kotlin_jetpack_architecture.di.auth.AuthModule
import com.example.kotlin_jetpack_architecture.di.auth.AuthScope
import com.example.kotlin_jetpack_architecture.di.auth.AuthViewModelModule
import com.example.kotlin_jetpack_architecture.ui.auth.AuthActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class, AuthFragmentBuildersModule::class, AuthViewModelModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

}