package com.example.sel.screen.user.listenpodcast

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.sel.R
import com.example.sel.base.BaseBindingActivity
import com.example.sel.base.Constants
import com.example.sel.base.Utils
import com.example.sel.base.model.PodcastItem
import com.example.sel.databinding.ActivityListenPodcastBinding
import com.example.sel.screen.user.podcast2.Podcast2Activity
import java.util.concurrent.TimeUnit
class ListenPodcastActivity : BaseBindingActivity<ListenPodcastViewModel, ActivityListenPodcastBinding>(), View.OnClickListener {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var handler: Handler
    private lateinit var _podcast: PodcastItem
    private var currentSegmentIndex = 0
    private var pausedPosition = 0

    private val handler2 = Handler(Looper.getMainLooper())

    private val updateSeekBar = object : Runnable {
        override fun run() {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    val currentPosition = player.currentPosition
                    binding?.seekbarsong?.progress = currentPosition
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(currentPosition.toLong())
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(currentPosition.toLong()) % 60
                    binding?.textviewtimesong?.text = String.format("%02d:%02d", minutes, seconds)
                    handler2.postDelayed(this, 1000)
                }
            }
        }
    }

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

        viewModel.reponseItemPodcast.observe(this) {
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
        binding?.imgVBackPodcast?.setOnClickListener(this)

        binding?.seekbarsong?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(progress.toLong())
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(progress.toLong()) % 60
                    binding?.textviewtimesong?.text = String.format("%02d:%02d", minutes, seconds)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun fetchData() {
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        binding?.executePendingBindings()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imagebuttonplay -> {
                mediaPlayer?.let { player ->
                    val duration = player.duration
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % 60
                    binding?.textviewtotaltimesong?.text = String.format("%02d:%02d", minutes, seconds)
                    binding?.seekbarsong?.max = duration

                    if (player.isPlaying) {
                        player.pause()
                        binding?.imagebuttonplay?.setImageResource(R.drawable.iconplay)
                        pausedPosition = player.currentPosition
                    } else {
                        player.seekTo(pausedPosition)
                        binding?.imagebuttonplay?.setImageResource(R.drawable.iconpause)
                        player.start()
                        handler2.post(updateSeekBar)
                        updateTranscript()
                    }
                } ?: run {
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(_podcast.audioUrl)
                        binding?.imagebuttonplay?.setImageResource(R.drawable.iconpause)
                        setOnPreparedListener {
                            start()
                            val duration = it.duration
                            val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toLong())
                            val seconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % 60
                            binding?.textviewtotaltimesong?.text = String.format("%02d:%02d", minutes, seconds)
                            binding?.seekbarsong?.max = duration
                            handler2.post(updateSeekBar)
                            updateTranscript()
                        }
                        prepareAsync() // Use prepareAsync() to not block the main thread
                        setOnCompletionListener {
                            binding?.imagebuttonplay?.setImageResource(R.drawable.iconplay)
                            handler2.removeCallbacks(updateSeekBar)
                            release()
                            mediaPlayer = null
                        }
                    }
                }
            }
            R.id.imgVBackPodcast -> {
                Utils.startActivityWithResultCode(
                    this@ListenPodcastActivity,
                    Podcast2Activity(),
                    0,
                    null
                )
            }
        }
    }

    override fun setActivityName(): String {
        return "Podcast Id "
    }

    override fun observeData() {
        viewModel.reponseItemPodcast.observe(this) {
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
                mediaPlayer?.let { player ->
                    val currentPosition = player.currentPosition / 1000 // Chuyển đổi ms sang giây
                    val currentSegment = _podcast.transcript.getOrNull(currentSegmentIndex)

                    currentSegment?.let {
                        if (currentPosition in it.startTime..it.endTime) {
                            highlightCurrentSegment(it.text)
                        } else if (currentPosition > it.endTime) {
                            currentSegmentIndex++
                        }
                    }

                    if (player.isPlaying) {
                        handler.postDelayed(this, 1000) // Cập nhật mỗi giây
                    }
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

    override fun onPause() {
        super.onPause()
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }
        handler2.removeCallbacks(updateSeekBar)
    }
}
