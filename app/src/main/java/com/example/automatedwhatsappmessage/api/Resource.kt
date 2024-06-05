package com.example.automatedwhatsappmessage.api


data class Resource<out T>(
    val status: ApiStatus,
    val data: T?,
    val message: String?,
    val code: Int
) {
    companion object {

        fun <T> success(data: T?, code: Int): Resource<T> {
            return Resource(ApiStatus.SUCCESS, data, null, code)
        }

        fun <T> error(msg: String, data: T?, code: Int): Resource<T> {
            return Resource(ApiStatus.ERROR, data, msg, code)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(ApiStatus.LOADING, data, null, 0)
        }

    }
}

enum class ApiStatus {
    SUCCESS,
    ERROR,
    LOADING
}


