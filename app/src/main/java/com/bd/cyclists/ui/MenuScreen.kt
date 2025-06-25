package com.bd.cyclists.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuScreen() {
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