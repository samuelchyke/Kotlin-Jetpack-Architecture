package com.example.kotlin_jetpack_architecture.ui.auth

import androidx.lifecycle.ViewModel
import com.example.kotlin_jetpack_architecture.repository.auth.AuthRepository

class AuthViewModel
constructor(
    val authRepository: AuthRepository
): ViewModel()
{
}