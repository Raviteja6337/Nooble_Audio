package com.nooble.noobleaudio.data.repository

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShortsDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(notificationData: ShortsRoomData): Long

    @Query("SELECT * FROM ShortsRoomData")
    fun fetchAllShorts(): LiveData<List<ShortsRoomData>>

    @Delete
    fun deleteShort(notificationData: ShortsRoomData)

    @Query("DELETE FROM ShortsRoomData")
    fun deleteAllShorts()
}