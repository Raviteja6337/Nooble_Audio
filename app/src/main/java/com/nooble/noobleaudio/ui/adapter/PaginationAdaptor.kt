package com.nooble.noobleaudio.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.nooble.noobleaudio.R
import com.nooble.noobleaudio.data.model.ShortDetails
import java.util.*
import com.nooble.noobleaudio.ui.activity.MainActivity
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import android.os.Handler
import android.widget.*
import android.widget.SeekBar
import java.lang.Exception


class PaginationAdapter(context: Context, list:LinkedList<ShortDetails>) : RecyclerView.Adapter<ViewHolder>(),
    SeekBar.OnSeekBarChangeListener{

    private val TAG = PaginationAdapter::class.java.simpleName

    private val context: Context
    private var shortsList: LinkedList<ShortDetails>?
    private var isLoadingAdded = false
    private var mediaPlayer:MediaPlayer //Media Player

    private val builder : AlertDialog.Builder
    private var dialog  : AlertDialog? = null
    private var songProgressBar:SeekBar? = null
    private val mHandler : Handler

    init {
        this.context = context
        shortsList = list

//        val audioAttributes = AudioAttributes.Builder()
//            .setUsage(AudioAttributes.USAGE_MEDIA)
//            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//            .build()
        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        builder = AlertDialog.Builder(context)

        mHandler = Handler()
    }


    companion object {
        private const val LOADING = 0
        private const val ITEM = 1
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewHolder: ViewHolder? = null
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ITEM -> {
                val viewItem: View = inflater.inflate(R.layout.shorts_list_item, parent, false)
                viewHolder = ShortsViewHolder(viewItem)
            }
            LOADING -> {
                val viewLoading: View = inflater.inflate(R.layout.network_state_item, parent, false)
                viewHolder = LoadingViewHolder(viewLoading)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val short: ShortDetails = shortsList!![position]
        when (getItemViewType(position)) {
            ITEM -> {
                val shortViewHolder = holder as ShortsViewHolder
                shortViewHolder.shortsTitle.setText(short.title)
                shortViewHolder.dateCreated.setText(short.dateCreated)

                //SeekBar to show Song Progress
                shortViewHolder.seekBarAudio.setOnSeekBarChangeListener(this)
                songProgressBar = shortViewHolder.seekBarAudio
                songProgressBar?.setProgress(0)

                shortViewHolder.audioBtn.setOnClickListener(View.OnClickListener {

                        if (it.tag.equals("play")) {

                            shortViewHolder.audioBtn.tag = "pause"
                            shortViewHolder.audioBtn.setImageResource(R.drawable.pause_btn)


                            try {

                                //If Audio is paused before completion resume from the same Duration
                                if (songProgressBar?.progress!=0 && mediaPlayer.currentPosition < mediaPlayer.duration)
                                {
                                    mediaPlayer.start()
                                }
                                else {
                                    playAudioFromURL(short)
                                }
                            }
                            catch (e: IOException) {
                                // Catch the exception
                                e.printStackTrace()
                            } catch (e: IllegalArgumentException) {
                                e.printStackTrace()
                            } catch (e: SecurityException) {
                                e.printStackTrace()
                            } catch (e: IllegalStateException) {
                                e.printStackTrace()
                            }

                            mediaPlayer.setOnCompletionListener(OnCompletionListener {
                                Toast.makeText(context, "End", Toast.LENGTH_SHORT).show()
                                shortViewHolder.audioBtn.tag = "play"
                                shortViewHolder.audioBtn.setImageResource(R.drawable.play_btn)
                                mediaPlayer.reset()
                                mediaPlayer.release()

                                dismissProgressDialog()

                                //Scroll to next position in recycler view
                                MainActivity.recyclerShortsView.scrollToPosition(position+1)
                                MainActivity.recyclerShortsView.animate()
                                MainActivity.playNextAudioAutomatically = true
                                Toast.makeText(context, "Playing Next", Toast.LENGTH_SHORT).show()
                                notifyItemChanged(position+1)
                            })
                        }
//
//                        else if (it.tag.equals("resume")){
//                            mediaPlayer.
//                        }
                        else {
                            shortViewHolder.audioBtn.tag = "play"
                            shortViewHolder.audioBtn.setImageResource(R.drawable.play_btn)
                            mediaPlayer.pause()
//                            mediaPlayer.stop()
//                            mediaPlayer.reset()
//                            mediaPlayer.release()
                        }
                })


                //Play the next Audio Clip automatically after the scroll is complete
                if (MainActivity.playNextAudioAutomatically) {
                    shortViewHolder.audioBtn.callOnClick()
                    MainActivity.playNextAudioAutomatically = false
                }
            }
            LOADING -> {
                val loadingViewHolder = holder as LoadingViewHolder
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE)
            }
        }
    }



    //Media Player SetUp with URL obtained from the Shorts
    private fun playAudioFromURL(short: ShortDetails) {

        songProgressBar?.setProgress(0)
        songProgressBar?.max = 100
        updateProgressBar()
        showProgressDialog()

        mediaPlayer = MediaPlayer()

//      mediaPlayer.setDataSource("https://download.samplelib.com/mp3/sample-3s.mp3")//working
        mediaPlayer.setDataSource("https://download.samplelib.com/mp3/sample-9s.mp3")


        mediaPlayer.prepare()

        mediaPlayer.setOnPreparedListener(MediaPlayer.OnPreparedListener {
            // Start playing audio from http url after showing the loader for few seconds
            Handler().postDelayed({
                dismissProgressDialog()
                mediaPlayer.start()
            }, 1000)
        })

        // Inform user for audio streaming
        Toast.makeText(context, "Playing", Toast.LENGTH_SHORT).show()
    }


    override fun getItemCount(): Int {
        return if (shortsList == null) 0 else shortsList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == shortsList!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
        add(ShortDetails(null,"","","",null))
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position: Int = shortsList!!.size - 1
        val result: ShortDetails = getItem(position)
        if (result != null) {
            shortsList?.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun add(short: ShortDetails?) {
        if (short != null) {
            shortsList?.add(short)
        }
        notifyItemInserted(shortsList!!.size - 1)
    }

    fun addAll(shortResults: List<ShortDetails?>) {
        for (result in shortResults) {
            add(result)
        }
    }

    fun setShortList(shortList: List<ShortDetails>?) {
        this.shortsList = shortsList
    }

    fun getItem(position: Int): ShortDetails {
        return shortsList!![position]
    }

    inner class ShortsViewHolder(itemView: View) : ViewHolder(itemView) {
        val shortsTitle: TextView
        val dateCreated:TextView
        val audioBtn:ImageButton
        val seekBarAudio:SeekBar

        init {
            shortsTitle   = itemView.findViewById(R.id.cv_title) as TextView
            dateCreated  = itemView.findViewById(R.id.tv_created_on) as TextView
            audioBtn     = itemView.findViewById(R.id.audio_btn) as ImageButton
            seekBarAudio = itemView.findViewById(R.id.songProgressBar) as SeekBar
        }
    }

    inner class LoadingViewHolder(itemView: View) : ViewHolder(itemView) {
        val progressBar: ProgressBar

        init {
            progressBar = itemView.findViewById(R.id.progress_bar_item) as ProgressBar
        }
    }




    private fun showProgressDialog()
    {
            builder.setView(R.layout.custom_loader)
            dialog = builder.create()
            dialog?.show()
    }


    private fun dismissProgressDialog()
    {
        dialog?.dismiss()
    }




    /**
     *    TODO: SeekBar Progress Changes
     **/

    fun updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 10)
    }

    private val mUpdateTimeTask: Runnable = object : Runnable {
        override fun run() {
            try {
                val totalDuration: Long = mediaPlayer.getDuration().toLong()
                val currentDuration: Long = mediaPlayer.getCurrentPosition().toLong()

                // Updating progress bar
                val progress = getProgressPercentage(currentDuration, totalDuration) as Int
                songProgressBar?.setProgress(progress)

                // Running this thread after 10 milliseconds
                mHandler.postDelayed(this, 10)
            }
            catch (e:Exception)
            {

            }
        }
    }

    fun getProgressPercentage(currentDuration: Long, totalDuration: Long): Int {
        var percentage = 0.toDouble()
        val currentSeconds: Long = (currentDuration / 1000)
        val totalSeconds: Long = (totalDuration / 1000)

        // calculating percentage
        percentage = currentSeconds.toDouble() / totalSeconds * 100

        // return percentage
        return percentage.toInt()
    }



    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromTouch: Boolean) {

    }

    //When user starts moving the progress handler
    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        mHandler.removeCallbacks(mUpdateTimeTask)
    }

    //When user stops moving the progress hanlder
    override fun onStopTrackingTouch(seekBar: SeekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask)
        val totalDuration: Int = mediaPlayer.getDuration()
        val currentPosition: Int = progressToTimer(seekBar.progress, totalDuration)

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition)

        // update timer progress again
        updateProgressBar()
    }


    fun progressToTimer(progress: Int, totalDuration: Int): Int {
        var totalDuration = totalDuration
        var currentDuration = 0
        totalDuration = (totalDuration / 1000)
        currentDuration = (progress.toDouble() / 100 * totalDuration).toInt()

        // return current duration in milliseconds
        return currentDuration * 1000
    }
}