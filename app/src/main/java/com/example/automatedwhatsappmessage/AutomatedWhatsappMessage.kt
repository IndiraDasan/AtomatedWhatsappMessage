package com.example.automatedwhatsappmessage

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorkerFactory
import com.example.automatedwhatsappmessage.Common.AppPreferences
import com.example.automatedwhatsappmessage.broadcastreceiver.CallDetectingService
import com.example.automatedwhatsappmessage.broadcastreceiver.PhoneStateReceiver
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AutomatedWhatsappMessage:Application() {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override fun onCreate() {
        super.onCreate()

        instance = this
        AppPreferences.init(this)


        startForegroundService()


    }
    private fun startForegroundService() {
        val serviceIntent = Intent(this,CallDetectingService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }


    companion object{

        lateinit var instance: AutomatedWhatsappMessage

    }

}