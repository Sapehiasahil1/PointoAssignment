package com.example.pointoassignment.Location.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.pointoassignment.Location.LocationHelper
import com.example.pointoassignment.Location.LocationService

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationHelper = LocationHelper(application)

    fun checkLocationStatus(context: Context,permissionLauncher: (Array<String>) -> Unit) {

        val permissions = mutableListOf<String>()

        if (!locationHelper.isLocationPermissionGranted()) {
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !locationHelper.isNotificationPermissionGranted()) {
            permissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        if (permissions.isEmpty()) {

            if(!locationHelper.isLocationEnabled()) {
                locationHelper.promptEnableLocation()
            } else
            startLocationService(context)
        } else {
            permissionLauncher(permissions.toTypedArray())
        }
    }

    private fun startLocationService(context: Context) {

        val serviceIntent = Intent(context, LocationService::class.java)
        context.startForegroundService(serviceIntent)
    }
}