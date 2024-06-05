package com.example.automatedwhatsappmessage.broadcastreceiver

import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Handler
import android.os.Looper

import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.example.automatedwhatsappmessage.Common.AppPreferences
import com.example.automatedwhatsappmessage.api.AddCallStatus
import com.example.automatedwhatsappmessage.api.AddCallStatusResults
import com.example.automatedwhatsappmessage.api.CallType
import com.example.automatedwhatsappmessage.api.DeviceDetail
import com.example.automatedwhatsappmessage.api.RetrofitClient
import com.example.automatedwhatsappmessage.worker.WorkerCreator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder


@Suppress("DEPRECATION")
class PhoneStateReceiver: BroadcastReceiver() {
    var messageState: Int = 0
    //var isIncoming: Boolean = false
    var isOutGoing: Boolean = false
    var isReceiving: Boolean = false
    var isMissedCall: Boolean = false
    var laststate: Int = 0
    var isIncoming: Boolean = false



    override fun onReceive(context: Context?, intent: Intent?) {
        val telephonyManager: TelephonyManager = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                super.onCallStateChanged(state, phoneNumber)
                when (state) {
                    TelephonyManager.CALL_STATE_IDLE -> {
                        if (isIncoming && isReceiving) {
                            Log.d(TAG, "onCallStateChanged: ReceivedCall")
                            deviceCheck(context, "${phoneNumber}", "ReceivedCall")
                            isIncoming = false
                            isReceiving = false

                        } else if (isMissedCall) {
                            deviceCheck(context, "${phoneNumber}", "MissedCall")
                            isMissedCall = false
                            Log.d(TAG, "onCallStateChanged: MissedCall")

                        } else {
                            deviceCheck(context, "${phoneNumber}", "DailedCalls")
                            Log.d(TAG, "onCallStateChanged: DailedCalls")
                            isMissedCall = false
                            isReceiving = false
                            isIncoming = false
                        }
                        // Reset flags after handling the call state
                    }

                    TelephonyManager.CALL_STATE_RINGING -> {
                        isIncoming = true
                        isMissedCall = true
                        WorkerCreator.setupOnetimeBFUWorker(context)
                        Log.d(TAG, "onCallStateChanged: Ringing")
                    }

                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                        if (isIncoming) {
                            isReceiving = true
                            isMissedCall = false
                        }
                        Log.d(TAG, "onCallStateChanged: Call is dialing, active or on hold")
                    }

                }
                Log.d(TAG, "PhoneNumber: $phoneNumber")
            }
        }, PhoneStateListener.LISTEN_CALL_STATE)

    }

    private fun showToast(context: Context?, message: String) {
        context?.let {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deviceCheck(context: Context?, phoneNumber: String, messagetype: String) {
        context?.let {
            if (isNetworkAvailable(context)) {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val response = RetrofitClient.getAPIInstance().deviceCheck(
                            DeviceDetail("12345678", "${messagetype}")
                        )
                        if (response.isSuccessful) {
                            AppPreferences.isMessageService = true
                            val results = response.body()?.results
                            val messageType = results?.map { it.messagetype.toString() }?.toString()?.replace("[", "")?.replace("]", "")

                            if (results != null && results.any { it.appstatus.toString().trim() == "1" }) {
                                val accountType = results.map { it.accounttype.toString().trim() }.toString().replace("[", "").replace("]", "")

                                if (accountType == "personal") {
                                    when (messagetype.toUpperCase()) {
                                        CallType.DAILEDCALL.toString() -> {
                                            Log.d(TAG, "onCallStateChanged: DAILEDCALL")
                                            val templateContent = results.map { it.messageTemplate.map { it.templatecontent } }.toString().replace("[", "").replace("]", "")
                                            openWhatsApp(context, phoneNumber = phoneNumber, templateContent)
                                        }
                                        CallType.MISSEDCALL.toString() -> {
                                            Log.d(TAG, "onCallStateChanged: MISSEDCALL")

                                            val templateContent = results.map { it.messageTemplate.map { it.templatecontent } }.toString().replace("[", "").replace("]", "")
                                            openWhatsApp(context, phoneNumber = phoneNumber, templateContent)
                                        }
                                        CallType.RECEIVEDCALL.toString() -> {
                                            Log.d(TAG, "onCallStateChanged: RECEIVEDCALL")
                                            val templateContent = results.map { it.messageTemplate.map { it.templatecontent } }.toString().replace("[", "").replace("]", "")
                                            openWhatsApp(context, phoneNumber = phoneNumber, templateContent)
                                        }
                                    }
                                } else {
                                    when (messagetype.lowercase()) {
                                        CallType.DAILEDCALL.toString().lowercase(),
                                        CallType.MISSEDCALL.toString().lowercase(),
                                        CallType.RECEIVEDCALL.toString().lowercase() -> {
                                            if (AppPreferences.isMessageSent == true) {
                                                val templateContent = results.map { it.messageTemplate.map { it.templatecontent } }.toString().replace("[", "").replace("]", "")
                                                openBWhatsApp(context, phoneNumber = phoneNumber, templateContent)
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Handle app status not equal to 1
                            }
                            Log.d(TAG, "API call successful: ${response.body()}")
                        } else {
                            Log.e(TAG, "API call failed: ${response.errorBody()?.string()}")
                            // Handle API call failure
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "Error during API call", e)
                        // Handle exceptions
                    }

                }
            } else {
                // Handle no internet connection
                Log.d(TAG, "No internet connection")
                showToast(context, "No internet connection")
                // You can show a toast or display a dialog to inform the user about the lack of internet connection
            }
        }

    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    // Inside your PhoneStateReceiver class

    private fun openWhatsApp(context: Context, phoneNumber: String,messageTemplate: String) {
        ismessageSend = false
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${URLEncoder.encode(messageTemplate, "UTF-8")}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage("com.whatsapp") // Restrict to WhatsApp
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK // Add this flag

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Handle case where WhatsApp is not installed
            Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // Handle other exceptions
            Log.d(TAG, "openWhatsApp: ${e.message}")
            Toast.makeText(context, "Failed to open WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
    private fun openBWhatsApp(context: Context, phoneNumber: String,message: String) {
        AppPreferences.isMessageSent = false
        ismessageSend = false
        val message = "Your message here" // Replace with your message
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${URLEncoder.encode(message, "UTF-8")}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage("com.whatsapp.w4b") // Restrict to WhatsApp
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK // Add this flag

        try {
            context.startActivity(intent)
            ismessageSend = true
        } catch (e: ActivityNotFoundException) {
            // Handle case where WhatsApp is not installed
            Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // Handle other exceptions
            Log.d(TAG, "openWhatsApp: ${e.message}")
            Toast.makeText(context, "Failed to open WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }


    companion object{
        const val TAG = "PhoneStateReceiver"
        var ismessageSend = true

            }


}



