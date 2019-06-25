/* Author - Aykut Asil(aykuttasil) */
package com.aykuttasil.sweetloc.data

data class Resource<out T, out E>(
    val status: Status,
    val data: T?,
    val errorData: E?,
    val error: Throwable?
) {

    companion object Factory {

        fun <T, E> success(data: T?): Resource<T, E> {
            return Resource(
                Status.SUCCESS,
                data,
                null,
                null
            )
        }

        fun <T, E> error(error: Throwable, errorData: E?): Resource<T, E> {
            return Resource(
                Status.ERROR,
                null,
                errorData,
                error
            )
        }

        fun <T, E> loading(): Resource<T, E> {
            return Resource(
                Status.LOADING,
                null,
                null,
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