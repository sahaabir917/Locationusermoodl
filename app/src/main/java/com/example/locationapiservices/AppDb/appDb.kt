package com.example.locationapiservices.AppDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(Usersettings::class)],version = 1)
abstract class appDb : RoomDatabase() {

//    abstract fun pass_dao() : password_Dao
    abstract fun usersetting_dao() : Usersetting_dao
}