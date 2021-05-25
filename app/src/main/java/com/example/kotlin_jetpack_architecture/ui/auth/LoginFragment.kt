package com.example.kotlin_jetpack_architecture.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.kotlin_jetpack_architecture.R
import com.example.kotlin_jetpack_architecture.util.ApiEmptyResponse
import com.example.kotlin_jetpack_architecture.util.ApiErrorResponse
import com.example.kotlin_jetpack_architecture.util.ApiSuccessResponse

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

        viewModel.testLogin().observe(viewLifecycleOwner, { response ->
            when (response) {
                is ApiSuccessResponse -> {
                    Log.d(TAG, "LOGIN RESPONSE: ${response.body}")
                }
                is ApiErrorResponse -> {
                    Log.d(TAG, "LOGIN RESPONSE: ${response.errorMessage}")
                }
                is ApiEmptyResponse -> {
                    Log.d(TAG, "LOGIN RESPONSE: Empty Response")
                }
            }
        })
    }

}