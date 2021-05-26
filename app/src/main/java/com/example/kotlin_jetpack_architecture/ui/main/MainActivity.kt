package com.example.kotlin_jetpack_architecture.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.example.kotlin_jetpack_architecture.R
import com.example.kotlin_jetpack_architecture.ui.BaseActivity
import com.example.kotlin_jetpack_architecture.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun displayProgressBar(bool: Boolean) {
        if (bool){
            progress_bar.visibility = View.VISIBLE
        }
        else{
            progress_bar.visibility = View.INVISIBLE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        subscribeObservers()
    }

    private fun subscribeObservers(){
        sessionManager.cachedToken.observe(this, { authToken ->
            Log.d(TAG, "MainActivity, subscribeObservers: ViewState: $authToken")
            if (authToken == null || authToken.account_pk == -1 || authToken.token == null) {
                navAuthActivity()
            }
        })
    }

    private fun navAuthActivity(){
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}