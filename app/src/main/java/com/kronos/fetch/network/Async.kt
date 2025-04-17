package com.kronos.fetch.network

sealed interface Async<out T> {
  data class Success<T>(val data: T) : Async<T>
  data class Error<T>(val error: String) : Async<T>
  data object Loading : Async<Nothing>
}