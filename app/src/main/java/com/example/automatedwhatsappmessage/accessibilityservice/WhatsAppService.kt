package com.example.automatedwhatsappmessage.accessibilityservice

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.automatedwhatsappmessage.Common.AppPreferences
import com.example.automatedwhatsappmessage.broadcastreceiver.PhoneStateReceiver


@Suppress("DEPRECATION")
class WhatsAppService:AccessibilityService(){

    var ischeckMessage:Boolean = false



    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val eventType = event.eventType
        val packageName = event.packageName
        val className = event.className
        val action = event.action
        // Additional event properties can be accessed from the 'event' object
        Log.d(TAG, "Package Name: $packageName")
        Log.d(TAG, "Class Name: $className")
        Log.d(TAG, "eventtype: $eventType")
        val windowTitle = event.text?.toString()
        Log.d(TAG, "windowtitle: $windowTitle")
        Log.d(TAG, "action: $action")
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            Log.d(TAG, "Class Name0: $className")
            val nodeInfo = event.source
            Log.i(TAG, "ACC::onAccessibilityEvent: nodeInfo=$nodeInfo")
            if (nodeInfo == null) {
                return
            }
            if (event.packageName == "com.whatsapp"){
                Log.d(TAG, "onAccessibilityEvent: ${AppPreferences.messageType}")
                if (AppPreferences.messageType?.trim().equals("automatic")) {
                    if (AppPreferences.isMessageService == true){
                        Log.d(TAG, "Class Name1: $className")
                        val listLeftButton =
                            nodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send")
                        for (node in listLeftButton) {
                            Log.d(TAG, "ACC::onAccessibilityEvent: left_button $node")
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                            Log.d(TAG, "Class Name2: $className")
                            AppPreferences.isMessageService = false
                        }
                    }

                }

            }
        }

    }

    override fun onInterrupt(){
        Log.d(TAG, "onInterrupt: something went to wrong")
    }

    override fun onSystemActionsChanged() {
        super.onSystemActionsChanged()
        Log.d(TAG, "onSystemActionsChanged: ")
    }

    override fun onServiceConnected(){
    }



    companion object{
        const val TAG = "AccessibilityService"
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }
    
}