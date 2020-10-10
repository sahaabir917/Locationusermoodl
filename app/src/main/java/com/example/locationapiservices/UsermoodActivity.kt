package com.example.locationapiservices

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_usermood.*

class UsermoodActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usermood)
        var nm =getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && !nm.isNotificationPolicyAccessGranted){
            startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
        }

        var am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        ringer.setOnClickListener {
            am.ringerMode = AudioManager.RINGER_MODE_NORMAL
            Toast.makeText(applicationContext,"Ringer mood actived", Toast.LENGTH_SHORT).show()
        }

        silent.setOnClickListener {
            am.ringerMode = AudioManager.RINGER_MODE_SILENT
            Toast.makeText(applicationContext,"silent mood actived", Toast.LENGTH_SHORT).show()
        }

        vibarate.setOnClickListener {
            am.ringerMode =  AudioManager.RINGER_MODE_VIBRATE
            Toast.makeText(applicationContext,"Vibarate mood actived", Toast.LENGTH_SHORT).show()
        }
    }
}
