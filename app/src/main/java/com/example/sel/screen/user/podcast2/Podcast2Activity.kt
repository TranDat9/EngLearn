package com.example.sel.screen.user.podcast2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sel.R
import com.example.sel.base.BaseBindingActivity
import com.example.sel.base.Constants
import com.example.sel.base.Utils
import com.example.sel.base.model.ItemHomeMenuConverted
import com.example.sel.base.model.ItemPodCastConverted
import com.example.sel.databinding.ActivityHistoryExamBinding
import com.example.sel.databinding.ActivityPodcast2Binding
import com.example.sel.screen.user.choose_answer.ChooseAnswerActivity
import com.example.sel.screen.user.historyexam.HistoryExamActivity
import com.example.sel.screen.user.historyexam.HistoryExamViewModel
import com.example.sel.screen.user.home.BottomSheetTopic.BottomSheetTopic
import com.example.sel.screen.user.home.HomeActivity
import com.example.sel.screen.user.listenpodcast.ListenPodcastActivity

class Podcast2Activity : BaseBindingActivity<Podcast2ViewModel, ActivityPodcast2Binding>(), View.OnClickListener  {
    var podcastId : Int ? = 0
    override fun setActivityLayout(): Int {
        return R.layout.activity_podcast2
    }
    override fun onActivityCreated() {
        super.onActivityCreated()
        initListener()
        initAdapter()
        viewModel.loadDataPCFromApi()

        viewModel.reponHistoryExam.observe(this){
            if (it?.Podcasts != null) {
                // Hiển thị giá trị của responsePodcast
               // Toast.makeText(this, it.Podcasts.toString(), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "No podcasts data received from API", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(this@Podcast2Activity, LinearLayoutManager.VERTICAL, false)
        binding?.rvPodCast?.layoutManager = linearLayoutManager
    }


    private fun initListener() {
       binding?.imgVBackPodcast?.setOnClickListener(this)

    }
    override fun onClick(v: View?) {
        when (v?.id) {
              R.id.imgVBackPodcast-> {
                  Utils.startActivityWithResultCode(
                      this@Podcast2Activity,
                      HomeActivity(),
                      0,
                      null,
                      )
                  }
        }
    }

    override fun createViewModel(): Podcast2ViewModel {
        return ViewModelProvider(this).get(Podcast2ViewModel::class.java)
    }

    override fun fetchData() {
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        binding?.executePendingBindings()
    }



    override fun setActivityName(): String {
        return "Pod Cast"
    }

    override fun observeData() {
        viewModel.reponHistoryExam.observe(this){
            viewModel.convertListContractEnable(it.Podcasts)

            viewModel.onClickItemPocast = :: clickItemPostcast
        }
    }

    fun clickItemPostcast(podcastItem: ItemPodCastConverted){
        podcastId = podcastItem.id
        Utils.startActivityWithResultCode(
            this@Podcast2Activity,
            ListenPodcastActivity(),
            0,
            Bundle().apply {
                putInt(Constants.BundleParam.PODCAST_ID,podcastId?:0)
            }
        )

    }

}