package com.kronos.fetch.network

sealed interface Async<out T> {
  data class Success<out T>(val data: T) : Async<T>
  data class Error<out T>(val data: T, val message: String) : Async<T>
  data object Loading : Async<Nothing>
}

fun <T> T.toSuccess(): Async<T> = Async.Success(this)
fun <T> T.toError(message: String): Async<T> = Async.Error(this, message)