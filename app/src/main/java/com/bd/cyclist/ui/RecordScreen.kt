package com.bd.cyclist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bd.cyclist.BuildConfig
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

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
//    Box(modifier = modifier.fillMaxSize()) {
//        MapboxMapView()
//    }
    BuildConfig.GOOGLE_MAP_API_KEY
    // Example: Initial camera position for Dhaka
    val dhakaLatLng = LatLng(23.8103, 90.4125)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(dhakaLatLng, 10f)
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true), // Enable "My Location" dot
            uiSettings = MapUiSettings(zoomControlsEnabled = true) // Enable zoom controls
        ) {
            // Add a marker for Dhaka
            Marker(
                state = rememberMarkerState(position = dhakaLatLng),
                title = "BdCyclist Start Point",
                snippet = "Welcome to Dhaka!"
            )
            // You can add more markers, circles, polylines, etc. here
        }
        // Optional: Overlay other UI elements on top of the map
        Text(
            text = "Google Map for BdCyclist Record View",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun MapboxMapView() {

}