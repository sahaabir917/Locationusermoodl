package com.example.locationapiservices.AppDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Usersettings {

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0

    @ColumnInfo(name = "usermood")
    var usermood : String = ""

    @ColumnInfo(name = "latitude")
    var latitude : Double? = null

    @ColumnInfo(name = "longitude")
    var longitude : Double? = null

}