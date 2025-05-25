package com.bd.cyclist.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberUpdatedMarkerState
import androidx.compose.ui.graphics.Color // For polyline color
import kotlin.collections.mutableListOf

@OptIn(ExperimentalPermissionsApi::class) // Required for Accompanist Permissions APIs
@Composable
fun RecordScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val dhakaLatLng = LatLng(23.8103, 90.4125)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(dhakaLatLng, 10f)
    }

    // State for location permission
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // State for tracking status
    var isTracking by remember { mutableStateOf(false) }

    // List to store recorded path points
    val pathPoints = mutableListOf<LatLng>()
    pathPoints.add(LatLng(23.885568, 90.416056))
    pathPoints.add(LatLng(23.885050, 90.416236))
    pathPoints.add(LatLng(23.885072, 90.416592))
    pathPoints.add(LatLng(23.881786, 90.417516))
    pathPoints.add(LatLng(23.881912, 90.418041))
    pathPoints.add(LatLng(23.880485, 90.418982))
    pathPoints.add(LatLng(23.879975, 90.419296))
    pathPoints.add(LatLng(23.879823, 90.415227))
    pathPoints.add(LatLng(23.880233, 90.410416))
    pathPoints.add(LatLng(23.880384, 90.405275))
    pathPoints.add(LatLng(23.879578, 90.401479))
    // Fused Location Provider Client
    val fusedLocationClient: FusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Location Callback to receive updates
    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val newPoint = LatLng(location.latitude, location.longitude)
//                    pathPoints.add(newPoint)

                    print("newPoint.longitude ${newPoint.longitude}, newPoint.latitude ${newPoint.latitude}")
                    // Optionally move camera to current location
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(newPoint, 15f)
                }
            }
        }
    }

    // Function to start location updates
    val startLocationUpdates: () -> Unit = {
        if (locationPermissionState.status.isGranted) {
            val locationRequest = LocationRequest.Builder(10000L) // Update every 10 seconds
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMinUpdateIntervalMillis(5000L) // Minimum update interval
                .build()

            try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    context.mainLooper // Use main looper for callbacks
                )
                isTracking = true
            } catch (e: SecurityException) {
                // This should ideally not happen if permission is granted, but as a fallback
                e.printStackTrace()
                // You might want to show a message to the user
            }
        }
    }

    // Function to stop location updates
    val stopLocationUpdates: () -> Unit = {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        isTracking = false
    }

    // Lifecycle observer to stop updates when the composable leaves the screen
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                if (isTracking) {
                    stopLocationUpdates()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            // Ensure updates are stopped if composable is disposed while tracking
            if (isTracking) {
                stopLocationUpdates()
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            // 1. Permission Granted
            locationPermissionState.status.isGranted -> {
                GoogleMap(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(zoomControlsEnabled = true)
                ) {
                    // Draw the polyline path
                    if (pathPoints.isNotEmpty()) {
                        Polyline(
                            points = pathPoints,
                            color = Color.Blue, // Path color
                            width = 8f // Path width
                        )
                    }

                    // Optionally, add a marker at the start of the path
                    if (pathPoints.isNotEmpty() && !isTracking) {
                        Marker(
                            state = rememberUpdatedMarkerState(position = pathPoints.first()),
                            title = "Start Point"
                        )
                    }
                    // Optionally, add a marker at the end of the path
                    if (pathPoints.isNotEmpty() && !isTracking) {
                        Marker(
                            state = rememberUpdatedMarkerState(position = pathPoints.last()),
                            title = "End Point"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (isTracking) {
                            stopLocationUpdates()
                        } else {
                            // Clear previous path if starting new tracking
                            pathPoints.clear()
                            startLocationUpdates()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(if (isTracking) "Stop Tracking" else "Start Tracking")
                }
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