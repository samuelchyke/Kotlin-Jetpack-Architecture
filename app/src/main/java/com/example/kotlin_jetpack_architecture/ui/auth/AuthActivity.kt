package com.example.kotlin_jetpack_architecture.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_jetpack_architecture.R
import com.example.kotlin_jetpack_architecture.ui.BaseActivity
import com.example.kotlin_jetpack_architecture.ui.ResponseType
import com.example.kotlin_jetpack_architecture.ui.main.MainActivity
import com.example.kotlin_jetpack_architecture.viewmodels.ViewModelProviderFactory
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        viewModel = ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)

        subscribeObservers()
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(this, { dataState ->
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { authViewState ->
                        authViewState.authToken?.let {
                            Log.d(TAG, "AuthActivity, DataState: $it")
                            viewModel.setAuthToken(it)
                        }
                    }
                }
                data.response?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        when (it.responseType) {
                            is ResponseType.Dialog -> {
                                // show dialog
                            }

                            is ResponseType.Toast -> {
                                // show toast
                            }

                            is ResponseType.None -> {
                                // print to log
                                Log.e(
                                    TAG,
                                    "AuthActivity: Response: ${it.message}, ${it.responseType}"
                                )
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(this, { viewState ->
            Log.d(TAG, "AuthActivity, subscribeObservers: AuthViewState: $viewState")
            viewState.authToken?.let {
                sessionManager.login(it)
            }
        })

        sessionManager.cachedToken.observe(this, { dataState ->
            Log.d(TAG, "AuthActivity, subscribeObservers: AuthDataState: $dataState")
            dataState.let{ authToken ->
                if(authToken != null && authToken.account_pk != -1 && authToken.token != null){
                    navMainActivity()
                }
            }
        })
    }

    private fun navMainActivity(){
        Log.d(TAG, "navMainActivity: called.")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}