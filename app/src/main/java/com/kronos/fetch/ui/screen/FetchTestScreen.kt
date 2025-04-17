package com.kronos.fetch.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kronos.fetch.model.FetchItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FetchTestScreen(
  modifier: Modifier = Modifier,
  vm: FetchTestViewModel = koinViewModel()
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Fetch Test") }
      )
    },
    modifier = modifier
  ) {
    val state by vm.state.collectAsStateWithLifecycle()

    FetchTestContent(
      items = state.items,
      padding = it,
      modifier = Modifier.fillMaxSize()
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FetchTestContent(
  items: List<FetchItem>,
  modifier: Modifier = Modifier,
  padding: PaddingValues = PaddingValues(0.dp),
) {
  LazyColumn(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = padding,
    modifier = modifier
  ) {
    items.filterNot {
      it.name.isNullOrBlank()
    }.sortedWith(
      compareBy(
        { it.listId },
        { it.name }
      )
    ).groupBy { it.listId }.forEach { listId, listItems ->
      stickyHeader(key = listId) {
        //TODO: add header
      }

      items(
        items = listItems,
        key = { it.id },
        contentType = { "FetchItem" }
      ) { item ->
        FetchListItem(
          name = item.name ?: "",
          modifier = Modifier
            .fillMaxWidth()
        )
      }
    }
  }
}

@Composable
fun FetchListItem(
  name: String,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = Modifier
      .defaultMinSize(minWidth = 100.dp, minHeight = 100.dp)
      .then(modifier),
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      Text(
        text = name,
        style = MaterialTheme.typography.displayLarge,
        color = MaterialTheme.colorScheme.primary,
      )
    }
  }
}

@Preview
@Composable
private fun FetchListItemPreview() {
  Surface {
    FetchListItem(
      name = "Test",
      modifier = Modifier
//        .fillMaxWidth()
        .padding(16.dp)
    )
  }
}