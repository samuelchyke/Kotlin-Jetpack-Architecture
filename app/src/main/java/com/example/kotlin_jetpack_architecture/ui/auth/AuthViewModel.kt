package com.example.kotlin_jetpack_architecture.ui.auth

import androidx.lifecycle.*
import com.example.kotlin_jetpack_architecture.models.AuthToken
import com.example.kotlin_jetpack_architecture.repository.auth.AuthRepository
import com.example.kotlin_jetpack_architecture.ui.BaseViewModel
import com.example.kotlin_jetpack_architecture.ui.DataState
import com.example.kotlin_jetpack_architecture.ui.Loading
import com.example.kotlin_jetpack_architecture.ui.auth.state.*
import com.example.kotlin_jetpack_architecture.ui.auth.state.AuthStateEvent.*
import com.example.kotlin_jetpack_architecture.ui.main.blog.state.BlogViewState
import com.example.kotlin_jetpack_architecture.util.AbsentLiveData
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
): BaseViewModel<AuthStateEvent,AuthViewState>()
{
    @InternalCoroutinesApi
    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        when(stateEvent){

            is LoginAttemptEvent -> {
                return authRepository.attemptLogin(
                    stateEvent.email,
                    stateEvent.password
                )
            }

            is RegisterAttemptEvent -> {
                return authRepository.attemptRegistration(
                    stateEvent.email,
                    stateEvent.username,
                    stateEvent.password,
                    stateEvent.confirm_password
                )
            }

            is CheckPreviousAuthEvent -> {
                return authRepository.checkPreviousAuthUser()
            }


            is None ->{
                return object : LiveData<DataState<AuthViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        value = DataState(null, Loading(false), null)
                    }
                }
            }

        }
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    fun setRegistrationFields(registrationFields: RegistrationFields){
        val update = getCurrentViewStateOrNew()
        if(update.registrationFields == registrationFields){
            return
        }
        update.registrationFields = registrationFields
        _viewState.value = update
    }

    fun setLoginFields(loginFields: LoginFields){
        val update = getCurrentViewStateOrNew()
        if(update.loginFields == loginFields){
            return
        }
        update.loginFields = loginFields
        _viewState.value = update
    }

    fun setAuthToken(authToken: AuthToken){
        val update = getCurrentViewStateOrNew()
        if(update.authToken == authToken){
            return
        }
        update.authToken = authToken
        _viewState.value = update
    }

    fun cancelActiveJobs(){
        authRepository.cancelActiveJobs()
    }


    fun handlePendingData(){
        setStateEvent(None())
    }
    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}
