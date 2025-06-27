package com.bd.cyclists.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MapsScreen() {
    Text("Maps Screen Content")
    val viewModel = viewModel<MapsViewModel>()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        RgbSelector(
            color = viewModel.color,
            onColorClick = {
                viewModel.changeColor(it)
            }
        )
    }
}

@Composable
fun RgbSelector(
    color: Color,
    onColorClick: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(color)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                onColorClick(Color.Red)
            }) {
                Text(text = "Red")
            }
            Button(onClick = {
                onColorClick(Color.Green)
            }) {
                Text(text = "Green")
            }
            Button(onClick = {
                onColorClick(Color.Blue)
            }) {
                Text(text = "Blue")
            }
        }
    }
}