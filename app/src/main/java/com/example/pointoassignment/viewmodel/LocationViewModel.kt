package com.example.pointoassignment.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import com.example.pointoassignment.LocationHelper
import com.example.pointoassignment.LocationService

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationHelper = LocationHelper(application)

    fun checkLocationStatus(context: Context,permissionLauncher: (Array<String>) -> Unit) {

//        if(locationHelper.isLocationPermissionGranted()) {
//            if(locationHelper.isLocationEnabled()) {
//                //Location is enabled
//
//                startLocationService(context)
//
//            } else {
//                locationHelper.promptEnableLocation()
//
//            }
//        } else {
//            permissionLauncher(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        }

        val permissions = mutableListOf<String>()

        if (!locationHelper.isLocationPermissionGranted()) {
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !locationHelper.isNotificationPermissionGranted()) {
            permissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        if (permissions.isEmpty()) {
            // No permissions needed, start location service
            startLocationService(context)
        } else {
            // Request permissions
            permissionLauncher(permissions.toTypedArray())
        }
    }

    private fun startLocationService(context: Context) {

        val serviceIntent = Intent(context, LocationService::class.java)
        context.startForegroundService(serviceIntent)
    }
}