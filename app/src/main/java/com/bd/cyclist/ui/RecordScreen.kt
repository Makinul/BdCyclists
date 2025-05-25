package com.bd.cyclist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.bd.cyclist.BuildConfig
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.Plugin.Mapbox

@OptIn(MapboxExperimental::class)
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
    val context = LocalContext.current
    AndroidView(
        factory = {
            MapView(context).apply {
                getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}