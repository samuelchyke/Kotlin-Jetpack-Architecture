package com.example.kotlin_jetpack_architecture.di

import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_jetpack_architecture.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory
}