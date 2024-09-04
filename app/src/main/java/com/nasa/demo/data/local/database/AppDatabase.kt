package com.nasa.demo.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nasa.demo.data.local.dao.NasaImageDao
import com.nasa.demo.data.model.NasaImageEntity

@Database(entities = [NasaImageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun nasaImageDao(): NasaImageDao
}