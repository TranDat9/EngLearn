package com.example.sel.screen.user.listenpodcast

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.sel.R
import com.example.sel.base.BaseBindingActivity
import com.example.sel.base.Constants
import com.example.sel.base.model.PodcastItem
import com.example.sel.databinding.ActivityListenPodcastBinding


class ListenPodcastActivity : BaseBindingActivity<ListenPodcastViewModel, ActivityListenPodcastBinding>(), View.OnClickListener  {

     private lateinit var mediaPlayer: MediaPlayer
    private lateinit var handler: Handler
    private lateinit var _podcast: PodcastItem
    private var currentSegmentIndex = 0
    private var pausedPosition = 0

    override fun setActivityLayout(): Int {
        return R.layout.activity_listen_podcast
    }

    override fun createViewModel(): ListenPodcastViewModel {

        return ViewModelProvider(this).get(ListenPodcastViewModel::class.java)
    }

    override fun onActivityCreated() {
        super.onActivityCreated()
        initListener()
        val ID = (intent.extras?.getInt(Constants.BundleParam.PODCAST_ID) ?: 0)
        viewModel.loadIdPodcast(ID)
        handler = Handler()

        viewModel.reponseItemPodcast.observe(this){
            if (it?.podcast != null) {
                 _podcast = it.podcast
               // Toast.makeText(this, it.podcast.toString(), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "No podcasts data received from API", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun initListener() {
        binding?.imagebuttonplay?.setOnClickListener(this)
        binding?.imagebuttonrepeat?.setOnClickListener(this)
    }

    override fun fetchData() {
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        binding?.executePendingBindings()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imagebuttonplay -> {
                if (::mediaPlayer.isInitialized) {
                    if (mediaPlayer.isPlaying) {
                       mediaPlayer.pause()
                        binding?.imagebuttonplay?.setImageResource(R.drawable.iconplay)
                        pausedPosition = mediaPlayer.currentPosition
                    }else{
                        mediaPlayer.seekTo(pausedPosition)
                        binding?.imagebuttonplay?.setImageResource(R.drawable.iconpause)
                            mediaPlayer.start()
                            updateTranscript()
                    }

                } else {
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(_podcast.audioUrl)
                        binding?.imagebuttonplay?.setImageResource(R.drawable.iconpause)
                        setOnPreparedListener {
                       //     binding?.seekbarsong?.max = it.duration
                            start()
                            updateTranscript()
                        }
                        prepareAsync() // Use prepareAsync() to not block the main thread
                        setOnCompletionListener {
                            release()
                        }
                    }
                }
            }
//            R.id.buttonPause ->{
//                    if (::mediaPlayer.isInitialized) {
//                        if (mediaPlayer.isPlaying) {
//                            mediaPlayer.pause()
//                            pausedPosition = mediaPlayer.currentPosition
//                        } else {
//                            mediaPlayer.seekTo(pausedPosition)
//                            mediaPlayer.start()
//                            updateTranscript()
//                        }
//                }
//            }
        }
    }

    override fun setActivityName(): String {
        return "Podcast Id "
    }

    override fun observeData() {
        viewModel.reponseItemPodcast.observe(this){
            if (it?.podcast != null) {
                _podcast = it.podcast
               // Toast.makeText(this, it.podcast.toString(), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "No podcasts data received from API", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateTranscript() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                val currentPosition = mediaPlayer.currentPosition / 1000 // Chuyển đổi ms sang giây
                val currentSegment = _podcast.transcript.getOrNull(currentSegmentIndex)

                currentSegment?.let {
                    if (currentPosition in it.startTime..it.endTime) {
                        highlightCurrentSegment(it.text)
                    } else if (currentPosition > it.endTime) {
                        currentSegmentIndex++
                    }
                }

                if (mediaPlayer.isPlaying) {
                    handler.postDelayed(this, 1000) // Cập nhật mỗi giây
                }
            }
        }, 1000)
    }

    private fun highlightCurrentSegment(segmentText: String) {
        val spannableString = SpannableString(_podcast.transcript.joinToString(" ") { it.text })
        var start = 0
        for (i in 0 until currentSegmentIndex) {
            start += _podcast.transcript[i].text.length + 1
        }
        val end = start + segmentText.length
        spannableString.setSpan(ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding?.textViewTranscript?.text = spannableString
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        if (::mediaPlayer.isInitialized) {
//            mediaPlayer.release()
//        }
//    }

}