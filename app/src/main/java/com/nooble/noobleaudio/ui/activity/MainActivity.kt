package com.nooble.noobleaudio.ui.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.nooble.noobleaudio.R
import com.nooble.noobleaudio.data.model.ShortDetails
import com.nooble.noobleaudio.data.model.ShortResult
import com.nooble.noobleaudio.data.model.ShortsViewModel
import com.nooble.noobleaudio.data.network.ApiController
import com.nooble.noobleaudio.data.network.ApiInterface
import com.nooble.noobleaudio.ui.adapter.PaginationAdapter
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var shortsViewModel: ShortsViewModel

    private lateinit var mActivity: AppCompatActivity
    private var verifyApiCallRequest: Call<ResponseBody>? = null
    private val shortDetails = LinkedList<ShortDetails>()
    lateinit var adaptorShorts : PaginationAdapter
    lateinit var layoutManager: LinearLayoutManager

    var isLoading: Boolean = false


    companion object {
        internal lateinit var recyclerShortsView: RecyclerView
        internal  var playNextAudioAutomatically:Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity =  this
        setContentView(R.layout.activity_main)
        playNextAudioAutomatically = false

        //Hiding the ActionBar
        actionBar?.hide()


        shortsViewModel = ViewModelProvider(this).get(ShortsViewModel::class.java)

        shortsViewModel.shortsDetailsData.observe(this, {
            Log.d(TAG,"Insert the Data into Room DB : shorts_db")
        })

        //getting the shorts data from the URL
        getShortDetails()

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adaptorShorts = PaginationAdapter(this, shortDetails)
        val isLastPage: Boolean = false


        recyclerShortsView = rv_shorts_list

        rv_shorts_list.layoutManager = layoutManager
        rv_shorts_list.setHasFixedSize(true)
        rv_shorts_list.adapter = adaptorShorts

        rv_shorts_list.addOnScrollListener(object :RecyclerView.OnScrollListener(){


            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = adaptorShorts.itemCount

                if (!isLoading) {

                    if ((visibleItemCount + pastVisibleItem) >= total) {
//                        page++
                        getPage()
//                        getShortDetails()
                    }

                }

                super.onScrolled(recyclerView, dx, dy)
            }

        })

    }


    fun getPage() {
        isLoading = true
        progress_bar_popular.visibility = View.VISIBLE
//        val start = ((page) * limit) + 1
//        val end = (page + 1) * limit
//
//        for (i in start..end) {
//            shortDetails.add(shortDetails.get(i%10))
//        }
        Handler().postDelayed({
            if (::adaptorShorts.isInitialized) {
                getShortDetails()
//                adaptorShorts.notifyDataSetChanged()
            } else {
                adaptorShorts = PaginationAdapter(this, shortDetails)
                rv_shorts_list.adapter = adaptorShorts
            }
            isLoading = false
            progress_bar_popular.visibility = View.GONE
        }, 1000)

    }


    private fun getShortDetails(){
        verifyApiCallRequest = ApiController(
            mActivity
        ).apiInterface?.getShortDetails(ApiInterface.GET_SHORT_DETAILS)

        verifyApiCallRequest?.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
              val shortsRes = Gson().fromJson(response.body()?.string(), ShortResult::class.java)
//              shortDetails.clear()
              shortDetails.addAll(shortsRes.shorts)
              adaptorShorts.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onFailure GET_SHORT_DETAILS")
//                shortDetails.clear()
            }

        })

    }

}
