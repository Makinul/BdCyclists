package com.bd.cyclists.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bd.cyclists.services.LocationTrackingService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@OptIn(ExperimentalPermissionsApi::class) // Required for Accompanist Permissions APIs
@Composable
fun RecordScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current

    // State for location permission
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    // NEW: Notification Permission state for Android 13+
    val postNotificationPermissionState =
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

    // Observe tracking state from a ViewModel or derive locally based on service
    var isTrackingServiceActive by remember { mutableStateOf(false) }

    // Collect location updates from the service (assuming service is active)
    val locationTrackingService =
        remember { LocationTrackingService() } // Get instance (will use Koin later)
    val pathPoints = locationTrackingService.locationUpdates.collectAsState(
        initial = LatLng(
            0.0,
            0.0
        )
    ).value // Collect new points
    // NOTE: This basic usage of `remember { LocationTrackingService() }` is for demonstration.
    // In a real app, you'd use a ViewModel to manage service state and pass updates.
    // Or, you could use a BroadcastReceiver within the Composable if using LocalBroadcastManager.


//    // List to store recorded path points
//    val pathPoints = mutableListOf<LatLng>()
//    pathPoints.add(LatLng(23.885568, 90.416056))
//    pathPoints.add(LatLng(23.885050, 90.416236))
//    pathPoints.add(LatLng(23.885072, 90.416592))
//    pathPoints.add(LatLng(23.881786, 90.417516))
//    pathPoints.add(LatLng(23.881912, 90.418041))
//    pathPoints.add(LatLng(23.880485, 90.418982))
//    pathPoints.add(LatLng(23.879975, 90.419296))
//    pathPoints.add(LatLng(23.879823, 90.415227))
//    pathPoints.add(LatLng(23.880233, 90.410416))
//    pathPoints.add(LatLng(23.880384, 90.405275))
//    pathPoints.add(LatLng(23.879578, 90.401479))

    val collectedPathPoints = remember { mutableStateListOf<LatLng>() }
    // As new location comes from service, add it to the list
    if (pathPoints != LatLng(0.0, 0.0)) { // Only add if not initial dummy value
        collectedPathPoints.add(pathPoints)
    }

    // --- (Same Lifecycle Observer for ON_STOP as before, or manage service state via ViewModel) ---
    // If you need to stop the service when the UI is gone, you'd send an intent here.
    // However, for background tracking, the service typically runs independently until explicitly stopped.
    // This DisposableEffect is more about UI cleanup, not necessarily stopping the background service.


//    // Location Callback to receive updates
//    val locationCallback = remember {
//        object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                locationResult.lastLocation?.let { location ->
//                    val newPoint = LatLng(location.latitude, location.longitude)
//                    pathPoints.add(newPoint)
//
//                    print("newPoint.longitude ${newPoint.longitude}, newPoint.latitude ${newPoint.latitude}")
//                    // Optionally move camera to current location
//                    cameraPositionState.position = CameraPosition.fromLatLngZoom(newPoint, 15f)
//                }
//            }
//        }
//    }

//    // Function to start location updates
//    val startLocationUpdates: () -> Unit = {
//        if (locationPermissionState.status.isGranted) {
//            val locationRequest = LocationRequest.Builder(10000L) // Update every 10 seconds
//                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
//                .setMinUpdateIntervalMillis(5000L) // Minimum update interval
//                .build()
//
//            try {
//                fusedLocationClient.requestLocationUpdates(
//                    locationRequest,
//                    locationCallback,
//                    context.mainLooper // Use main looper for callbacks
//                )
//                isTracking = true
//            } catch (e: SecurityException) {
//                // This should ideally not happen if permission is granted, but as a fallback
//                e.printStackTrace()
//                // You might want to show a message to the user
//            }
//        }
//    }

//    // Function to stop location updates
//    val stopLocationUpdates: () -> Unit = {
//        fusedLocationClient.removeLocationUpdates(locationCallback)
//        isTracking = false
//    }

//    // Lifecycle observer to stop updates when the composable leaves the screen
//    DisposableEffect(lifecycleOwner) {
//        val observer = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_STOP) {
//                if (isTracking) {
//                    stopLocationUpdates()
//                }
//            }
//        }
//        lifecycleOwner.lifecycle.addObserver(observer)
//
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(observer)
//            // Ensure updates are stopped if composable is disposed while tracking
//            if (isTracking) {
//                stopLocationUpdates()
//            }
//        }
//    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            // 1. Permission Granted
            locationPermissionState.status.isGranted -> {
                val initialCameraLatLng =
                    collectedPathPoints.lastOrNull() ?: LatLng(23.8103, 90.4125)
                val cameraPositionState = rememberCameraPositionState {
                    position =
                        CameraPosition.fromLatLngZoom(initialCameraLatLng, 15f) // Zoom closer
                }

                GoogleMap(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(zoomControlsEnabled = true)
                ) {
                    // Draw the polyline path using collectedPathPoints
                    if (collectedPathPoints.size > 1) { // Need at least two points to draw a line
                        Polyline(
                            points = collectedPathPoints,
                            color = Color.Blue,
                            width = 8f
                        )
                    }

                    // Optional: Mark start/end points when tracking is not active
                    if (collectedPathPoints.isNotEmpty() && !isTrackingServiceActive) {
                        Marker(
                            state = rememberUpdatedMarkerState(position = collectedPathPoints.first()),
                            title = "Start Point"
                        )
                        Marker(
                            state = rememberUpdatedMarkerState(position = collectedPathPoints.last()),
                            title = "End Point"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (isTrackingServiceActive) {
                            val stopIntent =
                                Intent(context, LocationTrackingService::class.java).apply {
                                    action = LocationTrackingService.ACTION_STOP_TRACKING
                                }
                            context.stopService(stopIntent)
                            isTrackingServiceActive = false
                        } else {
                            // --- NEW: Handle Notification Permission Before Starting Service ---
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
                                when {
                                    postNotificationPermissionState.status.isGranted -> {
                                        // Notification permission granted, proceed to start service
                                        // Request permission again just in case, though it should be granted here
                                        if (locationPermissionState.status.isGranted) {
                                            val startIntent =
                                                Intent(
                                                    context,
                                                    LocationTrackingService::class.java
                                                ).apply {
                                                    action =
                                                        LocationTrackingService.ACTION_START_TRACKING
                                                }
                                            // Clear path points when starting a new track
                                            collectedPathPoints.clear()
                                            context.startForegroundService(startIntent)
                                            isTrackingServiceActive = true
                                        } else {
                                            // Re-request permission if somehow not granted
                                            locationPermissionState.launchPermissionRequest()
                                        }
                                    }

                                    postNotificationPermissionState.status.shouldShowRationale -> {
                                        // Show rationale for notification permission
                                        // You can show a custom dialog here, similar to location rationale
                                        // showAlertDialog(postNotificationPermissionState)
                                    }

                                    else -> { // Permission not yet requested or permanently denied
                                        postNotificationPermissionState.launchPermissionRequest()
                                        // The service will only start if permission is granted after this request
                                    }
                                }
                            } else {
                                // Request permission again just in case, though it should be granted here
                                if (locationPermissionState.status.isGranted) {
                                    val startIntent =
                                        Intent(context, LocationTrackingService::class.java).apply {
                                            action = LocationTrackingService.ACTION_START_TRACKING
                                        }
                                    // Clear path points when starting a new track
                                    collectedPathPoints.clear()
                                    // For Android O (API 26) and above, use startForegroundService()
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        context.startForegroundService(startIntent)
                                    } else {
                                        context.startService(startIntent)
                                    }
                                    isTrackingServiceActive = true
                                } else {
                                    // Re-request permission if somehow not granted
                                    locationPermissionState.launchPermissionRequest()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(if (isTrackingServiceActive) "Stop Tracking" else "Start Tracking")
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun showAlertDialog(postNotificationPermissionState: PermissionState) {
    AlertDialog(
        onDismissRequest = { /* Dismiss action */ },
        title = { Text("Notification Permission Required") },
        text = { Text("BdCyclist needs notification access to run location tracking in the background. Please grant this permission.") },
        confirmButton = {
            TextButton(onClick = { postNotificationPermissionState.launchPermissionRequest() }) {
                Text("Grant")
            }
        },
        dismissButton = {
            TextButton(onClick = { /* Dismiss */ }) { Text("Dismiss") }
        }
    )
}