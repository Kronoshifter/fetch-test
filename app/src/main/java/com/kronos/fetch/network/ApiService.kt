package com.kronos.fetch.network

import android.util.Log.e
import com.kronos.fetch.model.FetchItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val REQUEST_URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json"

interface ApiService {
  fun fetchItems(): Flow<List<FetchItem>>
}

class ApiServiceImpl(private val client: HttpClient) : ApiService {
  override fun fetchItems(): Flow<List<FetchItem>> = flow {
    val response = client.request(REQUEST_URL).body<List<FetchItem>>()
    emit(response)
  }
}