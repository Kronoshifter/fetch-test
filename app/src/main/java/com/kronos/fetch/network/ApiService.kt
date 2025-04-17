package com.kronos.fetch.network

import com.kronos.fetch.model.FetchItem
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val REQUEST_URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json"

interface ApiService {
  fun fetchItems(): Flow<List<FetchItem>>
}

class ApiServiceImpl(private val client: HttpClient) : ApiService {
  override fun fetchItems(): Flow<List<FetchItem>> = flow {
    emit(client.request(REQUEST_URL).body())
  }
}