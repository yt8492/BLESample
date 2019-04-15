package com.yt8492.blesample

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.getSystemService

class MainActivity : AppCompatActivity() {

    private var bluetoothAdapter: BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.e("hogehoge", "BLE is not supported")
            return
        }

        val bluetoothManager = getSystemService<BluetoothManager>()
        bluetoothAdapter = bluetoothManager?.adapter
        bluetoothAdapter?.let {
            Log.e("hogehoge", "Bluetooth is not supported")
            return
        }
    }

    private fun requestPermission() {
        PERMISSIONS.filter { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
            .toTypedArray()
            .let {
                if (it.isNotEmpty()) {
                    requestPermissions(it, REQUEST_PERMISSION)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        requestBluetoothFeature()
    }

    private fun requestBluetoothFeature() {
        if (bluetoothAdapter?.isEnabled == true) {
            return
        }

        val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION -> {
                if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    Log.d("hogehoge", "Permission granted")
                } else {
                    Log.e("hogehoge", "Permission denied")
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_ENABLE_BLUETOOTH -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "Bluetooth is not working", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private const val REQUEST_PERMISSION = 1
        private const val REQUEST_ENABLE_BLUETOOTH = 2
        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )
    }
}
