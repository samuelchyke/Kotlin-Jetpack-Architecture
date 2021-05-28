package com.example.kotlin_jetpack_architecture.ui.main.account

import androidx.lifecycle.LiveData
import com.example.kotlin_jetpack_architecture.models.AccountProperties
import com.example.kotlin_jetpack_architecture.repository.main.AccountRepository
import com.example.kotlin_jetpack_architecture.session.SessionManager
import com.example.kotlin_jetpack_architecture.ui.BaseViewModel
import com.example.kotlin_jetpack_architecture.ui.DataState
import com.example.kotlin_jetpack_architecture.ui.main.account.state.AccountStateEvent
import com.example.kotlin_jetpack_architecture.ui.main.account.state.AccountStateEvent.*
import com.example.kotlin_jetpack_architecture.ui.main.account.state.AccountViewState
import com.example.kotlin_jetpack_architecture.util.AbsentLiveData
import javax.inject.Inject

class AccountViewModel
@Inject
constructor(
    val sessionManager: SessionManager,
    val accountRepository: AccountRepository
): BaseViewModel<AccountStateEvent, AccountViewState>()
{
    override fun handleStateEvent(stateEvent: AccountStateEvent): LiveData<DataState<AccountViewState>> {
        when(stateEvent){
            is GetAccountPropertiesEvent ->{
                return AbsentLiveData.create()
            }
            is UpdateAccountPropertiesEvent ->{
                return AbsentLiveData.create()
            }
            is ChangePasswordEvent ->{
                return AbsentLiveData.create()
            }
            is None ->{
                return AbsentLiveData.create()
            }
        }
    }

    override fun initNewViewState(): AccountViewState {
        return AccountViewState()
    }

    fun setAccountPropertiesData(accountProperties: AccountProperties){
        val update = getCurrentViewStateOrNew()
        if(update.accountProperties == accountProperties){
            return
        }
        update.accountProperties = accountProperties
        _viewState.value = update

    }

    fun logout(){
        sessionManager.logout()
    }



}