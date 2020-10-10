package com.example.locationapiservices

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class locationservice : Service() {
    var currentlatitude : Double = 0.0
    var currentlongitude : Double =0.0
    var prelatitude : Double = 0.0
    var prelongitude : Double = 0.0
    var result = FloatArray(1)
    var i : Int = 1;
    var newdistance : Float = 0.0F
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (locationResult != null && locationResult.lastLocation != null) {
                 currentlatitude = locationResult.lastLocation.latitude
                 currentlongitude = locationResult.lastLocation.longitude
                Log.d("current", "latitude is $currentlatitude and longtitude is $currentlongitude")
//                if(currentlatitude == )

                Toast.makeText(applicationContext, currentlatitude.toString() + " " + currentlongitude.toString(), Toast.LENGTH_LONG).show()
                Toast.makeText(applicationContext, prelatitude.toString() + " " + prelongitude.toString(), Toast.LENGTH_LONG).show()

                if(i==1){
                    prelatitude = currentlatitude
                    prelongitude = currentlongitude
                }
            }

            Location.distanceBetween(
                prelatitude,
                prelongitude,
                currentlongitude,
                currentlongitude,
                result
            )
            var distance = result[0]/1000
            Log.d("previous", "latitude is $prelatitude and longtitude is $prelongitude")

            Toast.makeText(applicationContext,prelatitude.toString()+" "+ prelongitude,Toast.LENGTH_SHORT).show();
            Toast.makeText(applicationContext, distance.toString(), Toast.LENGTH_SHORT).show()

            if(distance>=12684.469){
                Toast.makeText(applicationContext, "location changed", Toast.LENGTH_SHORT).show()
                prelongitude = currentlongitude
                prelatitude = currentlatitude

            }
        }

    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("not yet implemented")
    }

    private fun StartLocationService() {
        val channelId = "location_notification_channel"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent()
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder =
            NotificationCompat.Builder(applicationContext, channelId)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("location service")
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        builder.setContentText("Running")
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(false)
        builder.priority = NotificationCompat.PRIORITY_MAX
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    "location_service",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.description = "This channel is used by location service"
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
        val locationRequest = LocationRequest()
        locationRequest.interval = 4000
        locationRequest.fastestInterval = 2000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        startForeground(Constant.LOCATION_SERVICE_ID, builder.build())
    }

    private fun stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this)
            .removeLocationUpdates(locationCallback)
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            if (action != null) {
                if (action == Constant.ACTION_START_LOCATION_SERVICE) {
                    StartLocationService()
                } else if (action == Constant.ACTION_STOP_LOCATION_SERVICE) {
                    stopLocationService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}
