package com.example.pointoassignment.screens

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pointoassignment.Bluetooth.viewmodel.BleViewModel
import com.example.pointoassignment.Location.viewmodel.LocationViewModel

@Composable
fun HomeScreen(
//    onGetLocationClick: () -> Unit,
//    onGetDeviceInfoClick: () -> Unit,
    context: Context,
    navController: NavController
) {

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            // Location permission granted
            Toast.makeText(context, "Location permission granted", Toast.LENGTH_SHORT).show()

        } else {
            // Location permission denied
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }

        if (permissions[android.Manifest.permission.POST_NOTIFICATIONS] == true) {
            // Notification permission granted
            Toast.makeText(context, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            // Notification permission denied
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val locationViewModel: LocationViewModel= viewModel()
    val bleViewModel: BleViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = {
                locationViewModel.checkLocationStatus(context) { permissions->
                    permissionLauncher.launch(permissions)
                }
            }
//            onGetLocationClick

        ) {
            Text("Get Location")
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                bleViewModel.checkDeviceStatus(context, permissionLauncher,navController )
            }

        ) {
            Text("Get device Info")
        }
    }
}