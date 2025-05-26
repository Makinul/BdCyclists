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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Recommended for lifecycle-aware observation

@Composable
fun Progress(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel()
) {

    // Observe the UI state from the ViewModel
    // use collectAsStateWithLifecycle for lifecycle-aware collection (recommended)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // Alternatively, for simpler cases or older compose versions:
    // val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
            Text("Loading...")
        } else {
            Text(text = uiState.message)
            if (uiState.posts.isNotEmpty()) {
                Text(
                    text = "First Post: ${uiState.posts.first().title}",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            uiState.error?.let { errorMsg ->
                Text(text = "Error: $errorMsg", color = MaterialTheme.colorScheme.error)
            }
        }

        Button(
            onClick = { viewModel.loadPosts() }, // Trigger action on ViewModel
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Reload Posts")
        }
    }
}