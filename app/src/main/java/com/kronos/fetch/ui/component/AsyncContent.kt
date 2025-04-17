package com.kronos.fetch.ui.component

import android.R.id.message
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kronos.fetch.network.Async

@Composable
fun <T> AsyncContent(
  async: Async<T>,
  modifier: Modifier = Modifier,
  loading: @Composable () -> Unit = { CircularProgressIndicator() },
  error: @Composable (String) -> Unit = { Text(text = it) },
  content: @Composable (T) -> Unit
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
  ) {
    when (async) {
      is Async.Loading -> loading()
      is Async.Error -> error(async.message)
      is Async.Success -> content(async.data)
    }
  }
}