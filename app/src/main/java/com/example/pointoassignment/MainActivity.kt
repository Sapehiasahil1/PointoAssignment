package com.example.pointoassignment

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pointoassignment.Bluetooth.viewmodel.BleViewModel
import com.example.pointoassignment.screens.HomeScreen
import com.example.pointoassignment.ui.theme.PointoAssignmentTheme
import com.example.pointoassignment.Location.viewmodel.LocationViewModel
import com.example.pointoassignment.screens.DeviceInfoScreen

class MainActivity : ComponentActivity() {

    private val locationViewModel: LocationViewModel by viewModels()
    private val bleViewModel: BleViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            // Location permission granted
            Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()

        } else {
            // Location permission denied
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }

        if (permissions[android.Manifest.permission.POST_NOTIFICATIONS] == true) {
            // Notification permission granted
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            // Notification permission denied
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            PointoAssignmentTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) {

//                    HomeScreen(
//                        onGetLocationClick = {
//                            locationViewModel.checkLocationStatus(this) { permissions ->
//                                requestPermissionLauncher.launch(permissions)
//                            }
//                        },
//                        onGetDeviceInfoClick = {
//                            bleViewModel.checkDeviceStatus(this) { permissions->
//                                requestPermissionLauncher.launch(permissions)
//                            },
//                        }
//                    )
                    MainNavHost()
                }

            }
        }
    }

}

@Composable
fun MainNavHost() {

    val navController = rememberNavController()

    NavHost(navController, startDestination = "homeScreen") {
        composable("homeScreen") {
            HomeScreen(
                navController = navController,
                context = LocalContext.current
            )
        }
        composable("deviceInfoScreen/{device}") {
                backStackEntry ->
            val deviceAddress = backStackEntry.arguments?.getString("device")
            // You'll need to implement the logic to retrieve the BluetoothDevice object from the address
            // For example, you could look up the device using a method in your ViewModel
            // Here's a simplified example:
            DeviceInfoScreen(deviceAddress =deviceAddress)
        }
    }
}


