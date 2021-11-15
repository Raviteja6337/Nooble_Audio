package com.nooble.noobleaudio.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShortDetails(
    val shortId: Int?,
    val title: String,
    val dateCreated: String,
    val audioPath: String,
    val creator: CreatorResult?
):Parcelable
