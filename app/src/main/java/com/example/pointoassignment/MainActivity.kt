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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pointoassignment.screens.HomeScreen
import com.example.pointoassignment.ui.theme.PointoAssignmentTheme
import com.example.pointoassignment.viewmodel.LocationViewModel

class MainActivity : ComponentActivity() {

    private val locationViewModel: LocationViewModel by viewModels()

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
                    HomeScreen {

                        locationViewModel.checkLocationStatus(this) { permissions->
                            requestPermissionLauncher.launch(permissions)
                        }
                    }
                }
            }
        }
    }
}
