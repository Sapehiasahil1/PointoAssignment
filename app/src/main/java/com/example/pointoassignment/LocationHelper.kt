package com.example.pointoassignment

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

class LocationHelper(private val context: Context) {

    fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    fun isNotificationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    fun isLocationEnabled(): Boolean {
        val locationManger = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun promptEnableLocation() {
        Toast.makeText(context, "Please Enable the location", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    fun requestLocationPermission(launcher: ActivityResultLauncher<String>) {
        launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
}