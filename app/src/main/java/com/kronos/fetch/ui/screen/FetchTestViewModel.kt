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
import kotlinx.coroutines.flow.*
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
      is Async.Success -> {
        uiState.copy(
          items = itemsAsync.data
            .filterNot { it.name.isNullOrBlank() }
            .sortedByListIdAndName()
        ).toSuccess()
      }

      is Async.Error -> uiState.copy(userMessage = itemsAsync.message).toSuccess()
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), Async.Loading)

  fun clearUserMessage() {
    _uiState.update { it.copy(userMessage = null) }
  }

  fun refresh() {
    _uiState.update { FetchTestUiState() }
    _itemsAsync.update { Async.Loading }
    fetchItems()
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

  private fun List<FetchItem>.sortedByListIdAndName() = sortedWith(
    compareBy(
      { it.listId },
      // Using my best judgment here, I'm thinking that sorted by name should
      // mean that names with smaller numbers in them should be sorted before other ones
      { it.name?.length ?: 0 },
      { it.name }
    )
  )

  init {
    fetchItems()
  }
}