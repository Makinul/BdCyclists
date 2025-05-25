package com.bd.cyclist.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@OptIn(ExperimentalPermissionsApi::class) // Required for Accompanist Permissions APIs
@Composable
fun RecordScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // Use rememberPermissionState to manage the location permission
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Ensure your Mapbox access token is set before calling MapboxMap
    // You might set it globally in your Application class or here for testing
    // Mapbox.getInstance(context, BuildConfig.MAPBOX_ACCESS_TOKEN)
//    val mapToken = BuildConfig.MAPBOX_ACCESS_TOKEN
//    Text("mapToken: $mapToken")
//    print(mapToken)
//    Box(modifier = modifier.fillMaxSize()) {
//        MapboxMapView()
//    }
//    BuildConfig.GOOGLE_MAP_API_KEY
//    // Example: Initial camera position for Dhaka
//    val dhakaLatLng = LatLng(23.8103, 90.4125)
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(dhakaLatLng, 10f)
//    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            // 1. Permission Granted
            locationPermissionState.status.isGranted -> {
                // If permission is granted, display the Google Map
                val dhakaLatLng = LatLng(23.8103, 90.4125)
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(dhakaLatLng, 10f)
                }

                GoogleMap(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(), // Map takes available space
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true), // Enable "My Location" dot
                    uiSettings = MapUiSettings(zoomControlsEnabled = true)
                ) {
                    Marker(
                        state = rememberUpdatedMarkerState(position = dhakaLatLng),
                        title = "BdCyclist Start Point",
                        snippet = "Welcome to Dhaka!"
                    )
                }
                Text("Map loaded.", modifier = Modifier.padding(top = 8.dp))
            }
            // 2. Permission Denied (but not permanently) - show rationale
            locationPermissionState.status.shouldShowRationale -> {
                AlertDialog(
                    onDismissRequest = { /* Dismiss action */ },
                    title = { Text("Location Permission Required") },
                    text = {
                        Text(
                            "BdCyclist needs access to your location to display your current position " +
                                    "and track your rides on the map."
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { locationPermissionState.launchPermissionRequest() }) {
                            Text("Grant Permission")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { /* Handle dismiss */ }) {
                            Text("Not Now")
                        }
                    }
                )
            }
            // 3. Permission Permanently Denied or Not Requested Yet
            !locationPermissionState.status.isGranted && !locationPermissionState.status.shouldShowRationale -> {
                // This covers both initial request and permanently denied cases
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Location permission is crucial for this feature.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                        Text("Request Location Permission")
                    }
                    // If permanently denied, guide user to app settings
                    if (!locationPermissionState.status.isGranted) { // More explicit check for permanently denied
                        TextButton(
                            onClick = {
                                // Direct user to app settings
                                val intent =
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.fromParts("package", context.packageName, null)
                                    }
                                context.startActivity(intent)
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Open App Settings")
                        }
                    }
                }
            }
        }
    }
}