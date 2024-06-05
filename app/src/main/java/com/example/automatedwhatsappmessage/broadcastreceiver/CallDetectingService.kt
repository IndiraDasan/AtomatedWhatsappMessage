package com.example.automatedwhatsappmessage.broadcastreceiver

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.automatedwhatsappmessage.MainActivity
import com.example.automatedwhatsappmessage.R
import java.net.URLEncoder

class CallDetectingService : Service() {

    private val CHANNEL_ID = "CallDetectingServiceChannel"

    private var callReceiver: PhoneStateReceiver? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        callReceiver = PhoneStateReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        registerReceiver(callReceiver, intentFilter)
        startForegroundService()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(callReceiver)
    }

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundService() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE // Use FLAG_IMMUTABLE for Android 12 and above
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT // Use FLAG_UPDATE_CURRENT for earlier versions
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, pendingIntentFlags
        )

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Call Detecting Service")
            .setContentText("Detecting incoming calls")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Call Detecting Service Channel"
            val descriptionText = "Channel for Call Detecting Service"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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
