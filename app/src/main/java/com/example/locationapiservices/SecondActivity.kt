package com.example.locationapiservices

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.locationapiservices.AppDb.Usersettings
import com.example.locationapiservices.AppDb.appDb
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_second.*


class SecondActivity : AppCompatActivity() {
    private var latitudes: Double? = null
    private var longitudes: Double? = null
    private var get_place: TextView? = null
    private var longititudes: TextView? = null
    private var select_res : TextView? = null
    lateinit var option : Spinner
    lateinit var  select: String
    private var savesetting : Button? = null
    private var getlat : String? = null
    private var getlong : String? = null
    private var getsp : String? = null
    private var getlatu: Double? = null
    private var getlongi: Double? = null
    var PLACE_PICKER_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var db = Room.databaseBuilder(applicationContext, appDb::class.java,"Usersettings").build()
        setContentView(R.layout.activity_second)
        get_place = findViewById<View>(R.id.text_view) as TextView
        longititudes = findViewById<View>(R.id.longitude) as TextView
        option = findViewById<View>(R.id.sp_option) as Spinner
        select_res = findViewById<View>(R.id.sp_result) as TextView
        savesetting = findViewById<View>(R.id.save_settings) as Button

        savesetting!!.setOnClickListener {

            getlat = get_place!!.text.toString()
            getlong = longititudes!!.text.toString()
            getsp = select_res!!.text.toString()


            Thread{
                var i= 0
                var usersettings = Usersettings()
                usersettings.latitude = latitudes
                usersettings.longitude = longitudes
                usersettings.usermood = getsp.toString()
                db.usersetting_dao().saveUsersettings(usersettings)
//                Toast.makeText(applicationContext,"data saved",Toast.LENGTH_SHORT).show()
            }.start()


//            get_place!!.text = "latitude is :" + getlat + " longititude is : " + getlong + "setting is : " + getsp
        }

        get_place!!.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()
            val intent: Intent
            try {
                intent = builder.build(this@SecondActivity)
                startActivityForResult(intent, PLACE_PICKER_REQUEST)
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            }
        }

        val options = arrayOf("silent","general")
        option.adapter =ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
              select_res!!.text  = options.get(position).toString()
            }
        }

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(data, this@SecondActivity)
                 latitudes = place.latLng.latitude
                 longitudes = place.latLng.longitude
                val placename = place.name
                val address = latitudes.toString() + longitude.toString()
                get_place!!.text = latitudes.toString()
                longititudes!!.text = longitudes.toString()
            }
        }
    }
}