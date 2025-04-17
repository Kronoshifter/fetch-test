package com.kronos.fetch.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kronos.fetch.model.FetchItem
import com.kronos.fetch.network.ApiService
import com.kronos.fetch.network.Async
import com.kronos.fetch.network.toError
import com.kronos.fetch.network.toSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

data class FetchTestUiState(
  val items: List<FetchItem> = emptyList(),
  val userMessage: String? = null
)

class FetchTestViewModel(
  private val api: ApiService
) : ViewModel() {
  private val _uiState = MutableStateFlow(FetchTestUiState())
  private val _itemsAsync = MutableStateFlow<Async<List<FetchItem>>>(Async.Loading)

  val uiState = combine(_uiState, _itemsAsync) { uiState, itemsAsync ->
    when (itemsAsync) {
      Async.Loading -> Async.Loading
      is Async.Success -> uiState.copy(items = itemsAsync.data).toSuccess()
      is Async.Error -> uiState.copy(userMessage = itemsAsync.message).toSuccess()
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), Async.Loading)

  fun clearUserMessage() {
    _uiState.update { it.copy(userMessage = null) }
  }

  private fun fetchItems() {
    viewModelScope.launch {
      api.fetchItems()
        .map { it.toSuccess() }
        .flowOn(Dispatchers.Default)
        .catch { error ->
          Log.e("FetchTestViewModel", error.message, error)
          emit((error.message ?: "Error retrieving items, please try again").toError())
        }
        .collect { asyncItems ->
          _itemsAsync.update { asyncItems }
        }
    }
  }

  fun refresh() {
    fetchItems()
  }

  init {
    fetchItems()
  }
}