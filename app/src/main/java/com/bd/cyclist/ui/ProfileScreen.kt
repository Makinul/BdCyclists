package com.bd.cyclist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen() {
    /* Content for Profile screen */
    Box(
        modifier = Modifier
            .fillMaxSize() // Make the box fill the available space
            .background(MaterialTheme.colorScheme.surfaceVariant) // Set background color
            .padding(16.dp)
    ) {
        Text("Profile Screen Content")
    }
}