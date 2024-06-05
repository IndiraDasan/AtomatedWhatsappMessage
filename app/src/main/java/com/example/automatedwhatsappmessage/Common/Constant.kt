package com.example.automatedwhatsappmessage.Common

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.automatedwhatsappmessage.R
import com.example.automatedwhatsappmessage.api.ErrorResponse
import com.example.automatedwhatsappmessage.api.Resource
import com.google.gson.Gson
import retrofit2.Response



    fun <S> runCatchingResult(
        resp: Response<S>,
        result: MutableLiveData<Resource<S>>,
    ) {
        try {
            result.postValue(Resource.loading(null))
            if (resp.isSuccessful) {
                Log.d(TAG, "Received response from server. ${resp.body()}")
                result.postValue(Resource.success(data = resp.body(), code = resp.code()))
            } else {
                Log.d(TAG, "Unable to get data. ${resp.code()} ${resp.message()} ${resp.errorBody()}")
                val errorBody = resp.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                val errorMessage = if(resp.code() == 422){
                    try {
                        errorResponse.data[0]
                    } catch(e: Exception){
                        Log.d(TAG, "${e.localizedMessage} exception occured")
                        resp.message()
                    }
                }else{
                    errorResponse.message
                }
                result.postValue(Resource.error(errorMessage, null, resp.code()))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting data", e)
            result.postValue(Resource.error("Error. ${e.message}", null, 500))
        }
    }

fun showDialog(title: String, context: Context, imageview:Int){
    val dialog: Dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.custom_alert_dialog)
    val body = dialog.findViewById(R.id.txtDialogHeader) as TextView
    val imageView = dialog.findViewById<ImageView>(R.id.imgViewHeader)
    imageView.setImageResource(imageview)
    body.text = title
    val btn = dialog.findViewById<Button>(R.id.btnCloseDialog)
    if (!dialog.isShowing){
        dialog.show()
    }else{
        dialog.dismiss()
    }
    btn.setOnClickListener{
        dialog.dismiss()
    }
    Handler(context.mainLooper).postDelayed({
        dialog.dismiss()
    }, 2000)
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(value: T) {
            observer.onChanged(value)
            removeObserver(this)
        }
    })
}


const val TAG = "Constant"
