package com.nooble.noobleaudio.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShortsRoomData::class], version = 1, exportSchema = true)
abstract class ShortsAudioDatabase : RoomDatabase() {
    abstract fun shortDetailsDao(): ShortsDetailDao
}