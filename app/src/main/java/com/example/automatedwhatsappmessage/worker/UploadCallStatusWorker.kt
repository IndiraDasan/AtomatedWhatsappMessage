package com.example.automatedwhatsappmessage.worker

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.automatedwhatsappmessage.Common.AppPreferences
import com.example.automatedwhatsappmessage.api.AddCallStatus
import com.example.automatedwhatsappmessage.api.ApiInterface
import com.example.automatedwhatsappmessage.broadcastreceiver.PhoneStateReceiver
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URLEncoder
import javax.inject.Inject




@HiltWorker
class UploadCallStatusWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val TAG = "GetConfigWorker"
        const val WORK_NAME = "config-worker"
        const val DATA = "DATA"
    }

    @Inject
    lateinit var apiService: ApiInterface
    override suspend fun doWork(): Result {
        GlobalScope.launch(Dispatchers.Main) {
            openWhatsApp(applicationContext,"+918608079668")

        }
return Result.success()
    }

    private fun openWhatsApp(context: Context, phoneNumber: String) {
        val message = "Your message here" // Replace with your message
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${URLEncoder.encode(message, "UTF-8")}"
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
            Log.d(PhoneStateReceiver.TAG, "openWhatsApp: ${e.message}")
            Toast.makeText(context, "Failed to open WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }


}