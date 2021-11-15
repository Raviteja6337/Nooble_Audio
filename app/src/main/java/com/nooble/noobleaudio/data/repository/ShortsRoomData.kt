package com.nooble.noobleaudio.data.repository

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nooble.noobleaudio.data.model.CreatorResult
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class ShortsRoomData(

    @PrimaryKey(autoGenerate = true)
    var shortId: Long = 0L,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "message")
    val dateCreated: String?,

    @ColumnInfo(name = "image_url")
    val audioPath: String? = null,

) : Parcelable


//val shortId: Int?,
//val title: String,fetchAllShorts
//val dateCreated: String,
//val audioPath: String,
//val creator: CreatorResult?