package com.nooble.noobleaudio.Utils

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri

class AudioPlayback(private val context: Context) {

    private val TAG = AudioPlayback::class.java.simpleName

    private var mPlayer: MediaPlayer? = null
    private var audioManager: AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager


//    val isPlaying: Boolean
//        get() = if (mPlayer != null) mPlayer!!.isPlaying else false


    //native method might return null, so default False
    fun isPlaying(): Boolean {
        return mPlayer?.isPlaying ?: false
    }

    fun release() {
        try {
            mPlayer?.let {
                it.stop()
                it.reset()
                it.release()
                mPlayer = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // can be improved : for singleton and then setting resource files for audio
    fun setInstructions(uri: Uri?) {
        mPlayer?.let {
            release()
        }
        // clear previous object while interrupting
        mPlayer = MediaPlayer.create(context, uri)
    }

    fun setInstructions(inst: Int) {
        mPlayer?.let { release() } // clear previous object
        mPlayer = MediaPlayer.create(context, inst)
    }

    fun playAudio() {
        try {
            mPlayer?.setOnPreparedListener {
                mPlayer?.start()
            }
            mPlayer?.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setVolume(volume: Int) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
    }

    fun getVolume(): Int {
//        return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }


}