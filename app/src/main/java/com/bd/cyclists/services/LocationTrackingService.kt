package com.bd.cyclists.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.bd.cyclists.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class LocationTrackingService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var notificationManager: NotificationManager
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // Using SharedFlow to emit location updates to any interested collectors
    // This is better than LocalBroadcastManager for Compose/Kotlin flows
    private val _locationUpdates = MutableSharedFlow<LatLng>()
    val locationUpdates: SharedFlow<LatLng> = _locationUpdates

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                serviceScope.launch {
                    val latLng = LatLng(location.latitude, location.longitude)
                    _locationUpdates.emit(latLng)
                    // You would typically save this location to a database (e.g., Room) here
                    // e.g., yourLocationDao.insertLocation(location.toEntity())
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_TRACKING -> startLocationUpdates()
            ACTION_STOP_TRACKING -> stopSelf() // Stop the service
        }
        return START_STICKY // Service will be restarted if killed by system
    }

    private fun startLocationUpdates() {
        val notification = createNotification("Tracking your ride...")

        // Start as a foreground service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        val locationRequest = LocationRequest.Builder(UPDATE_INTERVAL_MS)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateIntervalMillis(FASTEST_UPDATE_INTERVAL_MS)
            .build()

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                mainLooper
            )
        } catch (e: SecurityException) {
            // This should only happen if permissions were revoked after service started
            // Log and potentially stop service, inform user
            e.printStackTrace()
            stopSelf()
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        stopForeground(STOP_FOREGROUND_REMOVE) // Remove notification
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
        serviceScope.cancel() // Cancel coroutine scope
    }

    override fun onBind(intent: Intent?): IBinder? {
        // This service is not designed for binding, so return null
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW // Low importance to be less intrusive
            ).apply {
                description = "Notifies when BdCyclist is tracking your location in the background."
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(contentText: String): Notification {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    this,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("BdCyclist Tracking")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_menu_compass) // Replace with your app's icon
            .setContentIntent(pendingIntent)
            .setOngoing(true) // Makes the notification persistent
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    companion object {
        const val CHANNEL_ID = "bdcyclists_location_channel"
        const val CHANNEL_NAME = "BdCyclists Location Tracking"
        const val NOTIFICATION_ID = 101 // Unique ID for your notification

        const val ACTION_START_TRACKING = "com.bd.cyclists.START_TRACKING"
        const val ACTION_STOP_TRACKING = "com.bd.cyclists.STOP_TRACKING"

        // Location update intervals
        private const val UPDATE_INTERVAL_MS = 5000L // 5 seconds
        private const val FASTEST_UPDATE_INTERVAL_MS = 2500L // 2.5 seconds
    }
}