package com.kronos.fetch.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kronos.fetch.model.FetchItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

data class FetchTestUiState(
  val items: List<FetchItem> = emptyList()
)

class FetchTestViewModel(

) : ViewModel() {
  private val _uiState = MutableStateFlow(FetchTestUiState())
  val uiState = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      repeat(10) { index ->
        delay(2.seconds)
        _uiState.update { current ->
          val item = FetchItem(id = index.toLong(), listId = index.toLong(), name = "Item $index".takeIf { index % 2 == 0 })
          current.copy(items = current.items + item)
        }
      }
    }
  }
}