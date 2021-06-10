package com.example.kotlin_jetpack_architecture.ui
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kotlin_jetpack_architecture.session.SessionManager
import com.example.kotlin_jetpack_architecture.util.Constants.Companion.PERMISSIONS_REQUEST_READ_STORAGE
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity: DaggerAppCompatActivity(), DataStateChangeListener, UICommunicationListener{

    val TAG: String = "AppDebug"


    override fun onUIMessageReceived(uiMessage: UIMessage) {
        when(uiMessage.uiMessageType){

            is UIMessageType.AreYouSureDialog ->{
                areYouSureDialog(
                    uiMessage.message,
                    uiMessage.uiMessageType.callback
                )

            }

            is UIMessageType.Toast ->{
                displayToast(uiMessage.message)
            }
            is UIMessageType.Dialog ->{
                displayInfoDialog(uiMessage.message)
            }

            is UIMessageType.None ->{
                Log.i(TAG, "onUIMessageReceived: ${uiMessage.message}")
            }
        }
    }

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onDataStateChange(dataState: DataState<*>?) {

        dataState?.let{
            GlobalScope.launch(Main){

                displayProgressBar(it.loading.isLoading)

                it.error?.let{ errorEvent->
                    handleStateError(errorEvent)

                }

                it.data?.let{

                    it.response?.let{ responseEvent->
                        handleStateResponse(responseEvent)
                    }

                }

            }
        }

    }

    private fun handleStateError(errorEvent: Event<StateError>){

        errorEvent.getContentIfNotHandled()?.let{ error ->

            when(error.response.responseType){

                is ResponseType.Toast -> {
                    error.response.message?.let {message ->
                        displayToast(message )
                    }
                }

                is ResponseType.Dialog -> {
                    error.response.message?.let {message ->
                        displayErrorDialog(message)
                    }
                }

                is ResponseType.None -> {
                    Log.e(TAG, "handleStateError: ${error.response.message}")

                    }

                }

            }


        }

    private fun handleStateResponse(event: Event<Response>){

        event.getContentIfNotHandled()?.let{ response ->

            when(response.responseType){

                is ResponseType.Toast -> {
                    response.message?.let {message ->
                        displayToast(message )
                    }
                }

                is ResponseType.Dialog -> {
                    response.message?.let {message ->
                        displaySuccessDialog(message)
                    }
                }

                is ResponseType.None -> {
                    Log.e(TAG, "handleStateResponse: {${response.message}")

                }

            }

        }

    }

    abstract fun displayProgressBar(bool: Boolean)

    override fun hideSoftKeyBoard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager
                .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun isStoragePermissionGranted(): Boolean{
        if (
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED  ) {


            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSIONS_REQUEST_READ_STORAGE
            )

            return false
        } else {
            // Permission has already been granted
            return true
        }
    }

}
