package com.example.pointoassignment.Bluetooth.viewmodel

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController


class BleViewModel(application: Application) : AndroidViewModel(application) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    fun checkDeviceStatus(
        context: Context,
        permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
        navController: NavController
    ) {
        val permissions = mutableListOf<String>()

        if (!isBluetoothPermissionGranted()) {
            permissions.add(android.Manifest.permission.BLUETOOTH_CONNECT)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !isLocationPermissionGranted()
        ) {
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (permissions.isEmpty()) {

            if (bluetoothAdapter?.isEnabled == true) {
                if(getConnectedDevice() == null) {
                    Toast.makeText(context, "No Device Connected", Toast.LENGTH_SHORT).show()
                } else {
                    //add code here to go to a composable
                    val connectedDevice = getConnectedDevice()
                    navController.navigate("deviceInfoScreen/${connectedDevice?.address}")
                }
            } else {
                Toast.makeText(context, "Please enable Bluetooth.", Toast.LENGTH_SHORT).show()
            }
        } else {

            permissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private fun isBluetoothPermissionGranted(): Boolean {

        return ContextCompat.checkSelfPermission(
            getApplication(),
            android.Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationPermissionGranted(): Boolean {

        return ContextCompat.checkSelfPermission(
            getApplication(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getConnectedDevice(): BluetoothDevice? {

        val bondedDevices = bluetoothAdapter?.bondedDevices
        return bondedDevices?.firstOrNull { device ->
            device.type == BluetoothDevice.DEVICE_TYPE_DUAL
        }
    }

}