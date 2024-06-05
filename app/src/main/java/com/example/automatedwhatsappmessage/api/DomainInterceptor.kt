package com.example.automatedwhatsappmessage.api

import android.annotation.SuppressLint
import android.util.Log
import com.example.automatedwhatsappmessage.Common.BASE_URL
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.net.URL

class DomainInterceptor() : Interceptor {
    @SuppressLint("SuspiciousIndentation")
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newUrl = request.url.toString()

        Log.d("DomainInterceptor", "intercept11: ${newUrl}")

        return try {
            chain.proceed(request.
            newBuilder()
                .url(URL
                    (newUrl)).build())
        } catch (e: Exception) {
            Log.e("DomainInterceptor", "Error sending request. ", e)
            Response.Builder()
                .request(request.newBuilder().url(URL(newUrl)).build())
                .protocol(Protocol.HTTP_2)
                .code(503)
                .message("Service Unavailable")
                .body(ResponseBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(), Gson().toJson(ApiResponse<String?>(
                                false,
                                "Network Error. ${e.message}",
                                0,
                                null))
                    )).build()
        }

    }
}

enum class InterceptType {
    OTHER
}