package com.bd.cyclist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Text("Home Screen Content")

    TabContent(0)
}

@Composable
fun TabContent(selectedTab: Int) {
    var selectedTabIndex by remember { mutableIntStateOf(selectedTab) }
    val tabs = listOf("Progress", "Activities") // Tab titles
    Column(modifier = Modifier) {
        // TabRow below the TopAppBar
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surfaceVariant // A subtle background for tabs
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) },
                    selectedContentColor = MaterialTheme.colorScheme.primary, // Color for selected tab text/icon
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant // Color for unselected tab text/icon
                )
            }
        }

        // Content area based on selected tab
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (selectedTabIndex) {
                0 -> Progress()
                1 -> Activities()
            }
        }
    }
}