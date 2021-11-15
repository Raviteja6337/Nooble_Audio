package com.nooble.noobleaudio.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ShortResult(
    val shorts: List<ShortDetails>
): Parcelable
