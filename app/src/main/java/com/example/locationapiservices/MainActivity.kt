package com.example.locationapiservices

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.locationapiservices.AppDb.appDb

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_lOCATION_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        var db = Room.databaseBuilder(
           applicationContext,
            appDb::class.java,
            "UserSettingsDatabase"
        ).build()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.usermood).setOnClickListener {
            val intent = Intent(this@MainActivity,UsermoodActivity::class.java)
            startActivity(intent)
        }


        findViewById<View>(R.id.start).setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_lOCATION_PERMISSION
                )
            } else {
                startLocationService()
            }
        }

        findViewById<View>(R.id.stop).setOnClickListener { stopLocationService() }
        findViewById<View>(R.id.settings).setOnClickListener {
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_lOCATION_PERMISSION && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService()
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isLocationServiceRunning(): Boolean {
        val activityManager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (activityManager != null) {
            for (service in activityManager.getRunningServices(
                Int.MAX_VALUE
            )) {
                if (locationservice::class.java.name == service.service.className) {
                    if (service.foreground) {
                        return true
                    }
                }
            }
            return false
        }
        return false
    }

    private fun startLocationService() {
        if (!isLocationServiceRunning()) {
            val intent = Intent(applicationContext, locationservice::class.java)
            intent.action = Constant.ACTION_START_LOCATION_SERVICE
            startService(intent)
            Toast.makeText(this, "location service started", Toast.LENGTH_LONG).show()
        }
    }


    private fun stopLocationService() {
        if (isLocationServiceRunning()) {
            val intent = Intent(applicationContext, locationservice::class.java)
            intent.action = Constant.ACTION_STOP_LOCATION_SERVICE
            startService(intent)
            Toast.makeText(this, "location service stopped", Toast.LENGTH_LONG).show()
        }
    }
}
