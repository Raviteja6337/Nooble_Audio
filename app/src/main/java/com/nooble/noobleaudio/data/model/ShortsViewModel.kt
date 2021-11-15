package com.nooble.noobleaudio.data.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nooble.noobleaudio.data.repository.ShortAudioRepository
import com.nooble.noobleaudio.data.repository.ShortsRoomData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ShortsViewModel(application: Application) : AndroidViewModel(application) {

    private val notificationRepository: ShortAudioRepository = ShortAudioRepository(application)

    val shortsDetailsData = notificationRepository.fetchAllShorts()

    fun deleteShort(notification: ShortsRoomData) {
        GlobalScope.launch {
            notificationRepository.deleteShort(notification)
        }
    }

    fun deleteAllShorts() {
        GlobalScope.launch {
            notificationRepository.deleteAllShorts()
        }
    }
}
