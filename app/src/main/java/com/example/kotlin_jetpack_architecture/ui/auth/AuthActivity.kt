package com.example.kotlin_jetpack_architecture.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlin_jetpack_architecture.R
import com.example.kotlin_jetpack_architecture.ui.BaseActivity

class AuthActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}