package com.example.locationapiservices.AppDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface Usersetting_dao {
    @Insert
    fun saveUsersettings(usersettings: Usersettings)

    @Query("select * from Usersettings")
    fun selectall() : List<Usersettings>


}