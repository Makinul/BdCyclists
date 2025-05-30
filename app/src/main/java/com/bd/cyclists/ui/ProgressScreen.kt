package com.bd.cyclists.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bd.cyclists.MainViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Recommended for lifecycle-aware observation
import com.bd.cyclists.data.model.Post

@Composable
fun ProgressScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel()
) {

    // Observe the UI state from the ViewModel
    // use collectAsStateWithLifecycle for lifecycle-aware collection (recommended)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // Alternatively, for simpler cases or older compose versions:
    // val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

//    Column(
//        modifier = modifier
//            .fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        if (uiState.isLoading) {
//            CircularProgressIndicator()
//            Text("Loading...")
//        } else {
//            if (uiState.posts.isNotEmpty()) {
//                LazyColumn(
//                    Modifier.fillMaxSize()
//                ) {
//                    // Add a single item
//                    item {
//                        ItemHeader(title = "Header")
//                    }
//
//                    // Add 5 items
//                    itemsIndexed(uiState.posts) { index, post ->
//                        Item(index = index, post = post)
//                    }
//
//                    // Add another single item
//                    item {
//                        ItemFooter(title = "Footer")
//                    }
//                }
//            }
//            uiState.error?.let { errorMsg ->
//                Text(text = "Error: $errorMsg", color = MaterialTheme.colorScheme.error)
//            }
//        }
//
//        Button(
//            onClick = { viewModel.loadPosts() }, // Trigger action on ViewModel
//            modifier = Modifier.padding(top = 16.dp),
//            enabled = !uiState.isLoading
//        ) {
//            Text("Reload Posts")
//        }
//    }

    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        // Add a single item
        item {
            ItemHeader(title = "Header")
        }

        // Add 5 items
        itemsIndexed(uiState.posts) { index, post ->
            Item(index = index, post = post)
        }

        // Add another single item
        item {
            ItemFooter(title = "Footer")
        }
    }

    // Check if we're near the bottom and trigger loadMore
    LaunchedEffect(listState.layoutInfo.visibleItemsInfo.lastOrNull()) {
//        if (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == 10) { // Adjust '3' as needed
//            print("anythings")
//        }
        println("anythings")
    }
}

@Composable
fun ItemHeader(modifier: Modifier = Modifier, title: String) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title
        )
    }
}

@Composable
fun ItemFooter(modifier: Modifier = Modifier, title: String) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title
        )
    }
}

@Composable
fun Item(modifier: Modifier = Modifier, index: Int, post: Post) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = "${index + 1}. ${post.title}".uppercase()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = post.body
        )
    }
}