package com.nooble.noobleaudio.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreatorResult (
    val userID: Int,
    val email: String
):Parcelable
