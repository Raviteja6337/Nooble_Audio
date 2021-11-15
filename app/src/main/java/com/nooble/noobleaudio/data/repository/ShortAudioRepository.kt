package com.nooble.noobleaudio.data.repository

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Room

class ShortAudioRepository(context: Context) {

    private val DB_NAME = "shorts_db"

    private var shortsDatabase: ShortsAudioDatabase = Room.databaseBuilder(
        context,
        ShortsAudioDatabase::class.java, DB_NAME
    ).fallbackToDestructiveMigration()
        .build()

    fun insertNotification(notifications: ShortsRoomData) {
        AsyncTask.execute {
            shortsDatabase.shortDetailsDao().insertTask(notifications)
        }
    }


    fun fetchAllShorts(): LiveData<List<ShortsRoomData>> {
        return shortsDatabase.shortDetailsDao().fetchAllShorts()
    }

    fun deleteShort(notification: ShortsRoomData) {
        shortsDatabase.shortDetailsDao().deleteShort(notification)
    }

    fun deleteAllShorts() {
        shortsDatabase.shortDetailsDao().deleteAllShorts()
    }

}