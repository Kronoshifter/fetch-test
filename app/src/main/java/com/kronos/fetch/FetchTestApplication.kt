package com.kronos.fetch

import android.app.Application
import com.kronos.fetch.ui.screen.FetchTestViewModel
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class FetchTestApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidLogger()
      androidContext(this@FetchTestApplication)
      modules(appModule)
    }
  }
}

val appModule = module {
  single {
    HttpClient(OkHttp) {
      install(ContentNegotiation) {
        json()
      }

      install(HttpTimeout) {
        requestTimeoutMillis = 1000
      }
    }
  }

  viewModelOf(::FetchTestViewModel)
}