package com.aykuttasil.sweetloc.data.remote

data class Resource<out T>(val status: Status, val data: T?, val error: Throwable?) {
    companion object Factory {
        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(error: Throwable, data: T?): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                error
            )
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null
            )
        }
    }

    enum class Status {
        LOADING,
        SUCCESS,
        ERROR
    }
}