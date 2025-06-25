package com.bd.cyclists.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GroupScreen() {
    /* Content for Settings screen */
    Column(
        modifier = Modifier
            .fillMaxSize() // Make the box fill the available space
            .background(MaterialTheme.colorScheme.surfaceVariant) // Set background color
            .padding(16.dp)
    ) {
        Text("Menu Screen Content")

        var currentValue by remember { mutableIntStateOf(0) }
        Text("Current value = $currentValue")
        Button(onClick = {
            currentValue = currentValue + 1
        }) {
            Text("Button")
        }
    }
}


/**
 * Data class to represent the state of each downloadable item.
 *
 * @param id A unique identifier for the item.
 * @param name The display name of the file.
 * @param downloadProgress The current download progress, from 0.0f to 1.0f.
 * @param isDownloading A flag to indicate if the download is currently active.
 */
data class DownloadableItem(
    val id: Int,
    val name: String,
    var downloadProgress: Float = 0f,
    var isDownloading: Boolean = false
)


/**
 * The main screen that displays a list of downloadable items.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadListScreen() {
    // A coroutine scope that is tied to the lifecycle of this composable.
    // Used to launch the download simulation without blocking the UI.
    val coroutineScope = rememberCoroutineScope()

    // Use `remember` with `mutableStateListOf` to hold the state for our list.
    // This is crucial. When a property of an item inside this list changes
    // (e.g., `downloadProgress`), Compose will automatically trigger a recomposition
    // for the composables that use that item.
    val downloadableItems = remember {
        mutableStateListOf(
            DownloadableItem(1, "Large Image Asset.jpg"),
            DownloadableItem(2, "User Manual.pdf"),
            DownloadableItem(3, "Game Update.zip"),
            DownloadableItem(4, "Video Lecture.mp4"),
            DownloadableItem(5, "Project Sources.tar.gz")
        )
    }

    /**
     * Simulates a download for a specific item by its index.
     * It updates the item's state over time.
     */
    fun simulateDownload(index: Int) {
        coroutineScope.launch {
            // Get the specific item from our state list.
            val item = downloadableItems[index]

            // Prevent starting a new download if one is already in progress.
            if (item.isDownloading) return@launch

            // Create a new item object with updated state to trigger recomposition.
            downloadableItems[index] = item.copy(isDownloading = true, downloadProgress = 0f)

            // Simulate progress updates.
            for (progress in 1..100) {
                delay(500) // Simulate network delay
                val currentItem = downloadableItems[index]
                downloadableItems[index] = currentItem.copy(downloadProgress = progress / 100f)
            }

            // Mark the download as complete.
            val finalItem = downloadableItems[index]
            downloadableItems[index] = finalItem.copy(isDownloading = false)
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // `itemsIndexed` gives us both the item and its index in the list.
        // The index is used to update the correct item's state.
        itemsIndexed(
            items = downloadableItems,
            key = { _, item -> item.id } // Provide a stable key for better performance
        ) { index, item ->
            DownloadableItemRow(
                item = item,
                onDownloadClick = { simulateDownload(index) }
            )
        }
    }
}

/**
 * A composable function that displays a single row for a downloadable item.
 * It's stateless and simply displays the data passed to it.
 *
 * @param item The data to display.
 * @param onDownloadClick A lambda function to be invoked when the download button is clicked.
 */
@Composable
fun DownloadableItemRow(
    item: DownloadableItem,
    onDownloadClick: () -> Unit
) {
    // Animate the progress bar changes for a smoother visual effect.
    val animatedProgress by animateFloatAsState(
        targetValue = item.downloadProgress,
        label = "progressAnimation"
    )

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(16.dp))
                Button(
                    onClick = onDownloadClick,
                    // Disable the button while this specific item is downloading.
                    enabled = !item.isDownloading && item.downloadProgress < 1f
                ) {
                    Text(
                        when {
                            item.isDownloading -> "Downloading..."
                            item.downloadProgress == 1f -> "Complete"
                            else -> "Download"
                        }
                    )
                }
            }

            // Only show the progress bar if the download has started.
            if (item.downloadProgress > 0f) {
                Spacer(Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DownloadListScreenPreview() {
    MaterialTheme {
        DownloadListScreen()
    }
}
