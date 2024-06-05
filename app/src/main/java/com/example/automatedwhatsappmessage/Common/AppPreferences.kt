package com.example.automatedwhatsappmessage.Common

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val NAME = "Automatedwhatsappmessage"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences


   // private val AUTH_TOKEN = Pair("auth_token", "0a07f6d27ca22793db2912b17bfe5351")
   // private val BASE_URL = Pair("base_url", "https://7165a35b773b-8051091035815681172.ngrok-free.app/")
    private val CONTENT = Pair("content", "")
    private val PHONE_NUMBER = Pair("phoneNumber", "")
    private val WHATSAPP_TYPE = Pair("whatsapp_type", "")
    private val TEMPLATE_NAME = Pair("template_name", "")
    private val MESSAGE_TEMPLATE_ID = Pair("template_id", "")
    private val DEVICE_UNIQUE_ID = Pair("deviceuniqueid", "")
    private val MESSAGE_TEMPLATE_IMAGE = Pair("template_image", "")
    private val MESSAGE_TEMPLATE_TYPE = Pair("template_type", "")
    private val MESSAGE_TYPE = Pair("message_type", "")
    private val SERVICE_ON_OFF = Pair("service_on_off", false)
    private val LOCATION = Pair("location", "")
    private val IS_CONFIGURED = Pair("isConfigured", false)
    private val MESSAGE_SERVICE = Pair("message_service", false)
    private val MESSAGE_SENT = Pair("message_sent", false)
    private val IMAGE_PATH = Pair("image_path","")

    //   private val HOST_DETAILS = Pair("access_points", emptyList<HostDetails>())


    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

     fun clearAllSharedPreferences(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
        val editor = preferences.edit()
// Clear all values
        editor.clear()
// Apply the changes
        editor.apply()
    }



    var isConfigured: Boolean?
        get() = preferences.getBoolean(IS_CONFIGURED.first, IS_CONFIGURED.second)
        set(value) = preferences.edit {
            if (value != null) {
                it.putBoolean(IS_CONFIGURED.first, value)
            }
        }


    var isMessageService: Boolean?
        get() = preferences.getBoolean(MESSAGE_SERVICE.first, MESSAGE_SERVICE.second)
        set(value) = preferences.edit {
            if (value != null) {
                it.putBoolean(MESSAGE_SERVICE.first, value)
            }
        }


    var isMessageSent: Boolean?
        get() = preferences.getBoolean(MESSAGE_SERVICE.first, MESSAGE_SERVICE.second)
        set(value) = preferences.edit {
            if (value != null) {
                it.putBoolean(MESSAGE_SERVICE.first, value)
            }
        }


    var templateLocation: String?
        get() = preferences.getString(LOCATION.first, LOCATION.second)
        set(value) = preferences.edit { it.putString(LOCATION.first, value) }

    var templateContent: String?
        get() = preferences.getString(CONTENT.first, CONTENT.second)
        set(value) = preferences.edit { it.putString(CONTENT.first, value) }

    var phoneNumber: String?
        get() = preferences.getString(PHONE_NUMBER.first, PHONE_NUMBER.second)
        set(value) = preferences.edit { it.putString(PHONE_NUMBER.first, value) }


    var whatsAppType: String?
        get() = preferences.getString(WHATSAPP_TYPE.first, WHATSAPP_TYPE.second)
        set(value) = preferences.edit { it.putString(WHATSAPP_TYPE.first, value) }


    var templateName: String?
        get() = preferences.getString(TEMPLATE_NAME.first, TEMPLATE_NAME.second)
        set(value) = preferences.edit { it.putString(TEMPLATE_NAME.first, value) }

    var deviceUniqueId: String?
        get() = preferences.getString(DEVICE_UNIQUE_ID.first, DEVICE_UNIQUE_ID.second)
        set(value) = preferences.edit { it.putString(DEVICE_UNIQUE_ID.first, value) }


    var messageTemplateId: String?
        get() = preferences.getString(MESSAGE_TEMPLATE_ID.first, MESSAGE_TEMPLATE_ID.second)
        set(value) = preferences.edit { it.putString(MESSAGE_TEMPLATE_ID.first, value) }


    var serviceOnOff: Boolean
        get() = preferences.getBoolean(SERVICE_ON_OFF.first, SERVICE_ON_OFF.second)
        set(value) = preferences.edit { it.putBoolean(SERVICE_ON_OFF.first, value) }


    var messageType: String?
        get() = preferences.getString(MESSAGE_TYPE.first, MESSAGE_TYPE.second)
        set(value) = preferences.edit { it.putString(MESSAGE_TYPE.first, value) }



    var templateType: String?
        get() = preferences.getString(MESSAGE_TEMPLATE_TYPE.first, MESSAGE_TEMPLATE_TYPE.second)
        set(value) = preferences.edit { it.putString(MESSAGE_TEMPLATE_TYPE.first, value) }

    var templateImage: String?
        get() = preferences.getString(MESSAGE_TEMPLATE_IMAGE.first, MESSAGE_TEMPLATE_IMAGE.second)
        set(value) = preferences.edit { it.putString(MESSAGE_TEMPLATE_IMAGE.first, value) }



    var image_path:String?
        get() = preferences.getString(IMAGE_PATH.first, IMAGE_PATH.second)
        set(value) = preferences.edit{it.putString(IMAGE_PATH.first,value)}

}