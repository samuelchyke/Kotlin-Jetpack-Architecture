package com.example.kotlin_jetpack_architecture.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlin_jetpack_architecture.R
import com.example.kotlin_jetpack_architecture.models.AuthToken
import com.example.kotlin_jetpack_architecture.ui.auth.state.LoginFields
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "LoginFragment: $viewModel")
        subscribeObservers()

        login_button.setOnClickListener {
            viewModel.setAuthToken(
                AuthToken(
                    1,
                    "gdfngidfng4nt43n43jn34jn"
                )
            )
        }
    }

    private fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            viewState.loginFields?.let{loginFields ->
                loginFields.login_email?.let{input_email.setText(it)}
                loginFields.login_password?.let{input_password.setText(it)}
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setLoginFields(
            LoginFields(
                input_email.text.toString(),
                input_password.text.toString()
            )
        )
    }

}