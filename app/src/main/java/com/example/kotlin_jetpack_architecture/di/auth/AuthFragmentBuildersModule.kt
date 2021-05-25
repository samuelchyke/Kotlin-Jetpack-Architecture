package com.example.kotlin_jetpack_architecture.di.auth
import com.example.kotlin_jetpack_architecture.ui.auth.ForgotPasswordFragment
import com.example.kotlin_jetpack_architecture.ui.auth.LauncherFragment
import com.example.kotlin_jetpack_architecture.ui.auth.LoginFragment
import com.example.kotlin_jetpack_architecture.ui.auth.RegisterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeLauncherFragment(): LauncherFragment

    @ContributesAndroidInjector()
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector()
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector()
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment

}