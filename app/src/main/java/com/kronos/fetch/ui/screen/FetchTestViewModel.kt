package com.kronos.fetch.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kronos.fetch.model.FetchItem
import com.kronos.fetch.network.ApiService
import com.kronos.fetch.network.Async
import com.kronos.fetch.network.mapError
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
import kotlin.time.Duration.Companion.seconds

data class FetchTestUiState(
  val items: List<FetchItem> = emptyList()
)

class FetchTestViewModel(
  private val api: ApiService
) : ViewModel() {
  private val _uiState = MutableStateFlow(FetchTestUiState())
  private val _itemsAsync = api.fetchItems()
    .distinctUntilChanged()
    .map { Async.Success(it) }
    .flowOn(Dispatchers.Default)
    .catch<Async<List<FetchItem>>> { emit(Async.Error(it.message ?: "Error retrieving items")) }

  val uiState = combine(_uiState, _itemsAsync) { uiState, itemsAsync ->
    when (itemsAsync) {
      Async.Loading -> Async.Loading
      is Async.Success -> uiState.copy(items = itemsAsync.data).toSuccess()
      is Async.Error -> itemsAsync.mapError()
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), Async.Loading)
}