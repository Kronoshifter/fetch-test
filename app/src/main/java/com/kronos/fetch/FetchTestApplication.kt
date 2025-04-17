package com.kronos.fetch

import android.app.Application
import com.kronos.fetch.ui.screen.FetchTestViewModel
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
  viewModelOf(::FetchTestViewModel)
}