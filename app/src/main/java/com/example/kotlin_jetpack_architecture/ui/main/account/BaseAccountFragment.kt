package com.example.kotlin_jetpack_architecture.ui.main.account

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.kotlin_jetpack_architecture.R
import com.example.kotlin_jetpack_architecture.ui.DataStateChangeListener
import com.example.kotlin_jetpack_architecture.ui.auth.state.AuthStateEvent
import com.example.kotlin_jetpack_architecture.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseAccountFragment : DaggerFragment(){


    val TAG: String = "AppDebug"

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var stateChangeListener: DataStateChangeListener
    lateinit var viewModel: AccountViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpActionBarWithNavController(R.id.accountFragment, activity as AppCompatActivity)

        viewModel = activity?.run {
            ViewModelProvider(this, providerFactory).get(AccountViewModel::class.java)
        }?: throw Exception("Invalid Activity")
        cancelActiveJobs()
    }

    fun setUpActionBarWithNavController(fragmentId : Int, activity: AppCompatActivity){
        val appBarConfiguration = AppBarConfiguration(setOf(fragmentId))
        NavigationUI.setupActionBarWithNavController(
            activity,
            findNavController(),
            appBarConfiguration
        )
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            stateChangeListener = context as DataStateChangeListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement DataStateChangeListener" )
        }
    }



    fun cancelActiveJobs(){
        viewModel.cancelActiveJobs()
    }


}
