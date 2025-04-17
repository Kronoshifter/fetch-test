package com.kronos.fetch.network

sealed interface Async<out T> {
  data class Success<out T>(val data: T) : Async<T>
  data class Error<out T>(val message: String) : Async<T>
  data object Loading : Async<Nothing>
}

fun <T> T.toSuccess(): Async<T> = Async.Success(this)
fun <T> String.toError(): Async<T> = Async.Error(this)
fun <T, R> Async.Error<T>.mapError(): Async<R> = Async.Error(message)