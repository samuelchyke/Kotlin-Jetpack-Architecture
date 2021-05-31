package com.example.kotlin_jetpack_architecture.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.*
import com.example.kotlin_jetpack_architecture.R
import com.example.kotlin_jetpack_architecture.models.AccountProperties
import com.example.kotlin_jetpack_architecture.ui.DataState
import com.example.kotlin_jetpack_architecture.ui.main.account.state.AccountStateEvent
import kotlinx.android.synthetic.main.fragment_update_account.*

class UpdateAccountFragment : BaseAccountFragment(){


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        setHasOptionsMenu(true)
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            Log.d(TAG, "UpdateAccountFragment, DataState: $dataState: ")
        })

        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            if(viewState != null){
                viewState.accountProperties?.let{
                    Log.d(TAG,"UpdateAccountFragment, DataState: $viewState: " )
                    setAccountDataFields(it)
                }
            }
        })
    }

    private fun setAccountDataFields(accountProperties: AccountProperties){
        input_email?.let{
            input_email.setText(accountProperties.email)
        }
        input_username?.let{
            input_email.setText(accountProperties.username)
        }
    }

    private fun saveChanges(){
        viewModel.setStateEvent(
            AccountStateEvent.UpdateAccountPropertiesEvent(
                input_email.text.toString(),
                input_username.text.toString()
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.save ->{
                saveChanges()
            }
        }
        return super.onOptionsItemSelected(item)


    }
}