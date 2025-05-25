package com.bd.cyclist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun RecordScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Ensure your Mapbox access token is set before calling MapboxMap
    // You might set it globally in your Application class or here for testing
    // Mapbox.getInstance(context, BuildConfig.MAPBOX_ACCESS_TOKEN)
//    val mapToken = BuildConfig.MAPBOX_ACCESS_TOKEN
//    Text("mapToken: $mapToken")
//    print(mapToken)
    Box(modifier = modifier.fillMaxSize()) {
        MapboxMapView()
    }
}

@Composable
fun MapboxMapView() {

}