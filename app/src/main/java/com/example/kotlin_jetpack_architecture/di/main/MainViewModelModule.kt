package com.example.kotlin_jetpack_architecture.di.main

import androidx.lifecycle.ViewModel
import com.example.kotlin_jetpack_architecture.di.ViewModelKey
import com.example.kotlin_jetpack_architecture.ui.main.account.AccountViewModel
import com.example.kotlin_jetpack_architecture.ui.main.blog.viewmodel.BlogViewModel
import com.example.kotlin_jetpack_architecture.ui.main.create_blog.CreateBlogViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BlogViewModel::class)
    abstract fun bindBlogViewModel(blogViewModel: BlogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateBlogViewModel::class)
    abstract fun bindCreateBlogViewModel(createBlogViewModel: CreateBlogViewModel): ViewModel
}
