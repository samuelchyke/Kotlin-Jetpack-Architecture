package com.example.kotlin_jetpack_architecture.di.main

import androidx.lifecycle.ViewModel
import com.example.kotlin_jetpack_architecture.di.ViewModelKey
import com.example.kotlin_jetpack_architecture.ui.main.account.AccountViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

}
