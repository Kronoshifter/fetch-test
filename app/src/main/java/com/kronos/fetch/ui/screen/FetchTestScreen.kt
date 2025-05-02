package com.kronos.fetch.ui.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kronos.fetch.model.FetchItem
import com.kronos.fetch.ui.component.AsyncContent
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FetchTestScreen(
  modifier: Modifier = Modifier,
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
  vm: FetchTestViewModel = koinViewModel(),
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Fetch Test") },
        actions = {
          IconButton(onClick = vm::refresh) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "Refresh"
            )
          }
        }
      )
    },
    snackbarHost = { SnackbarHost(snackbarHostState) },
    modifier = modifier
  ) { paddingValues ->
    val state by vm.uiState.collectAsStateWithLifecycle()

    AsyncContent(
      async = state,
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
    ) { uiState ->
      FetchTestContent(
        items = uiState.items,
        removedItems = uiState.removedItems,
        onDismissItem = vm::removeItem,
        modifier = Modifier.fillMaxSize()
      )

      uiState.userMessage?.let { message ->
        LaunchedEffect(message) {
          snackbarHostState.showSnackbar(
            message = message,
            withDismissAction = true,
            duration = SnackbarDuration.Short
          )
          vm.clearUserMessage()
        }
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FetchTestContent(
  items: List<FetchItem>,
  removedItems: List<FetchItem>,
  onDismissItem: (FetchItem) -> Unit,
  modifier: Modifier = Modifier,
) {
  if (items.isEmpty()) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = modifier
    ) {
      Text(
        text = "No data",
        color = MaterialTheme.colorScheme.secondary,
      )
    }
    return
  }

  LazyColumn(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier
  ) {
    items.filter { it !in removedItems }.groupBy { it.listId }.forEach { listId, listItems ->
      stickyHeader(
        key = "FetchListHeader-$listId",
        contentType = "FetchListHeader"
      ) {
        FetchListHeader(
          listId = listId,
          modifier = Modifier
            .fillMaxWidth()
            .animateItem(
              fadeInSpec = null,
              fadeOutSpec = null,
              placementSpec = spring(
                stiffness = Spring.StiffnessMediumLow,
                visibilityThreshold = IntOffset.VisibilityThreshold
              )
            )
        )
      }

      items(
        items = listItems,
        key = { "FetchListItem-${it.id}" },
        contentType = { "FetchItem" }
      ) { item ->
        requireNotNull(item.name) { "All items with null names should be filtered out by now, something went very wrong" }
        FetchListItem(
          item = item,
          onDismiss = onDismissItem,
          modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .animateItem(
              fadeInSpec = null,
              fadeOutSpec = null,
              placementSpec = spring(
                stiffness = Spring.StiffnessMediumLow,
                visibilityThreshold = IntOffset.VisibilityThreshold
              )
            )
        )
      }
    }
  }
}

@Composable
fun FetchListHeader(
  listId: Long,
  modifier: Modifier = Modifier,
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .shadow(elevation = 8.dp)
      .background(MaterialTheme.colorScheme.primary)
  ) {
    Text(
      text = "List ID $listId",
      style = MaterialTheme.typography.headlineLarge,
      color = MaterialTheme.colorScheme.onPrimary,
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
  }
}

@Composable
fun FetchListItem(
  item: FetchItem,
  onDismiss: (FetchItem) -> Unit,
  modifier: Modifier = Modifier,
) {
  val dismissState = rememberSwipeToDismissBoxState()

  LaunchedEffect(dismissState.targetValue, dismissState.currentValue) {
    if (
      (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart || dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd)
      && dismissState.targetValue == dismissState.currentValue
    ) {
      onDismiss(item)
    }
  }

  val dismissColor = Color.Red

  SwipeToDismissBox(
    state = dismissState,
    backgroundContent = {
      Surface(
        modifier = Modifier.fillMaxSize(),
        color = dismissColor,
        shape = CardDefaults.shape
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
        ) {
          if (dismissState.targetValue != SwipeToDismissBoxValue.EndToStart) {
            Icon(
              imageVector = Icons.Filled.Delete,
              contentDescription = null,
              tint = contentColorFor(Color.Red),
              modifier = Modifier.padding(16.dp)
            )
          }

          Spacer(Modifier.weight(1f))

          if (dismissState.targetValue != SwipeToDismissBoxValue.StartToEnd) {
            Icon(
              imageVector = Icons.Filled.Delete,
              contentDescription = null,
              tint = contentColorFor(Color.Red),
              modifier = Modifier.padding(16.dp)
            )
          }
        }
      }
    },
    modifier = Modifier
      .fillMaxHeight()
      .then(modifier)
      .clipToBounds()
  ) {
    Card(
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
      modifier = Modifier
        .fillMaxHeight()
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        item.name?.let {
          Text(
            text = it,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
          )
        }

        Spacer(Modifier.weight(1f))

        Column(
          horizontalAlignment = Alignment.End,
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Text("List ID: ${item.listId}", color = MaterialTheme.colorScheme.secondary)
          Text("Item ID: ${item.id}", color = MaterialTheme.colorScheme.secondary)
        }
      }
    }
  }
}

@Preview(device = PIXEL_7, showBackground = true)
@Composable
private fun FetchListItemPreview() {
  val item = FetchItem(
    name = "Test Item",
    listId = 1,
    id = 1
  )

  Surface(modifier = Modifier.height(IntrinsicSize.Min)) {
    FetchListItem(
      item = item,
      onDismiss = {},
      modifier = Modifier
        .padding(16.dp)
        .height(IntrinsicSize.Min),
    )
  }
}

@Preview(device = PIXEL_7, showBackground = true)
@Composable
private fun FetchListHeaderPreview() {
  FetchListHeader(
    listId = 1,
    modifier = Modifier
      .fillMaxWidth()
      .height(IntrinsicSize.Min),
  )
}

@Preview(device = PIXEL_7, showBackground = true)
@Composable
private fun FetchTestContentPreview() {
  Surface(modifier = Modifier.fillMaxSize()) {
    FetchTestContent(
      items = listOf(
        FetchItem(id = 1, listId = 1, name = "Test 1"),
        FetchItem(id = 2, listId = 2, name = "Test 2"),
        FetchItem(id = 3, listId = 2, name = "Test 3"),
        FetchItem(id = 4, listId = 3, name = "Test 4"),
        FetchItem(id = 5, listId = 3, name = "Test 5"),
        FetchItem(id = 6, listId = 3, name = "Test 6"),
      ),
      removedItems = listOf(),
      onDismissItem = {},
      modifier = Modifier.fillMaxSize()
    )
  }
}