package com.example.kotlin_jetpack_architecture.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.kotlin_jetpack_architecture.R
import com.example.kotlin_jetpack_architecture.ui.auth.state.RegistrationFields
import com.example.kotlin_jetpack_architecture.util.ApiEmptyResponse
import com.example.kotlin_jetpack_architecture.util.ApiErrorResponse
import com.example.kotlin_jetpack_architecture.util.ApiSuccessResponse
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "RegisterFragment: $viewModel")
        subscribeObservers()
    }

    fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer{viewState ->
            viewState.registrationFields?.let { registrationFields ->
                registrationFields.registration_email?.let{input_email.setText(it)}
                registrationFields.registration_username?.let{input_username.setText(it)}
                registrationFields.registration_password?.let{input_password.setText(it)}
                registrationFields.registration_confirm_password?.let{input_password_confirm.setText(it)}
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRegistrationFields(
            RegistrationFields(
                input_email.text.toString(),
                input_username.text.toString(),
                input_password.text.toString(),
                input_password_confirm.text.toString()
            )
        )
    }
}