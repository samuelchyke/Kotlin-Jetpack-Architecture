package com.example.kotlin_jetpack_architecture.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.kotlin_jetpack_architecture.R
import com.example.kotlin_jetpack_architecture.ui.BaseActivity
import com.example.kotlin_jetpack_architecture.ui.ResponseType
import com.example.kotlin_jetpack_architecture.ui.auth.state.AuthStateEvent
import com.example.kotlin_jetpack_architecture.ui.main.MainActivity
import com.example.kotlin_jetpack_architecture.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import com.example.kotlin_jetpack_architecture.viewmodels.ViewModelProviderFactory
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.progress_bar
import javax.inject.Inject

class AuthActivity : BaseActivity(),
    NavController.OnDestinationChangedListener
{

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        viewModel = ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)

        subscribeObservers()

    }


    override fun onResume() {
        super.onResume()
        checkPreviousAuthUser()
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(this, { dataState ->
            onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { authViewState ->
                        authViewState.authToken?.let {
                            Log.d(TAG, "AuthActivity, DataState: $it")
                            viewModel.setAuthToken(it)
                        }
                    }
                }
                data.response?.let{event ->
                    event.peekContent().let{ response ->
                        response.message?.let{ message ->
                            if(message == RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE){
                                onFinishCheckPreviousAuthUser()
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

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        viewModel.cancelActiveJobs()
    }

    override fun displayProgressBar(bool: Boolean) {
        if (bool){
            progress_bar.visibility = View.VISIBLE
        }
        else{
            progress_bar.visibility = View.INVISIBLE
        }
    }

    fun checkPreviousAuthUser(){
        viewModel.setStateEvent(AuthStateEvent.CheckPreviousAuthEvent())
    }

    private fun onFinishCheckPreviousAuthUser(){
        fragment_container.visibility = View.VISIBLE
    }

    override fun expandAppBar() {//ignore
    }


}