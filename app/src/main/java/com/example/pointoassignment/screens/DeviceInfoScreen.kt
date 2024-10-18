package com.example.pointoassignment.screens

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeviceInfoScreen(deviceAddress: String?) {

    var deviceName by remember { mutableStateOf("Unknown Device") }
    var services by remember { mutableStateOf<List<BluetoothGattService>>(emptyList()) }
    var gatt: BluetoothGatt? by remember { mutableStateOf(null) }

    val context = LocalContext.current
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter = bluetoothManager.adapter


    val gattCallback = rememberGattCallback(
        onServicesDiscovered = { discoveredServices ->
            services = discoveredServices
        },
        onConnectionError = {
            Toast.makeText(context, "Failed to connect to device.", Toast.LENGTH_SHORT).show()
        }
    )


    LaunchedEffect(deviceAddress) {
       val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
        gatt = device.connectGatt(context, false, gattCallback)
        deviceName = device.name ?: "Unknown Device"
    }

    // Clean up the GATT connection when the composable leaves the composition
    DisposableEffect(gatt) {
        onDispose {
            gatt?.close()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        if (deviceAddress == null) {
            Text("No Device Connected", fontSize = 20.sp)
            return@Column
        }

        Text(
            text = "Device Name: $deviceName",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )


        Text(
            text = "Device Address: $deviceAddress",
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (services.isEmpty()) {
            Text("Discovering services...", fontSize = 18.sp,color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        } else {
            Text("Services:", fontSize = 20.sp, style = MaterialTheme.typography.titleMedium)
            services.forEach { service ->
                Text("Service UUID: ${service.uuid}", fontSize = 18.sp, modifier = Modifier.padding(start = 8.dp))

                // Display characteristics for each service
                service.characteristics.forEach { characteristic ->
                    Text("  Characteristic UUID: ${characteristic.uuid}", fontSize = 16.sp, modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}

// Helper function to create the GATT callback
fun rememberGattCallback(
    onServicesDiscovered: (List<BluetoothGattService>) -> Unit,
    onConnectionError: () -> Unit
): BluetoothGattCallback {
    return object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                gatt.discoverServices() // Discover services once connected
            } else {
                onConnectionError()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                onServicesDiscovered(gatt.services) // Update the services state
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            // Handle characteristic read results if needed
        }

        // Implement other callback methods as needed
    }
}
