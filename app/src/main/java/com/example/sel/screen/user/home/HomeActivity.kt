package com.example.sel.screen.user.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sel.R
import com.example.sel.base.BaseBindingActivity
import com.example.sel.base.Constants
import com.example.sel.base.Constants.BundleParam.Companion.ONE
import com.example.sel.base.Constants.BundleParam.Companion.THREE
import com.example.sel.base.Constants.BundleParam.Companion.TWO
import com.example.sel.base.Constants.BundleParam.Companion.ZERO
import com.example.sel.base.Utils
import com.example.sel.base.model.ItemHomeMenuConverted
import com.example.sel.base.model.ItemPostConvert
import com.example.sel.screen.user.choose_answer.ChooseAnswerActivity
import com.example.sel.databinding.ActivityHomeBinding
import com.example.sel.screen.user.blog.BlogActivity
import com.example.sel.screen.user.dictionary.DictionaryActivity
import com.example.sel.screen.user.historyexam.HistoryExamActivity
import com.example.sel.screen.user.home.BottomSheetTopic.BottomSheetTopic
import com.example.sel.screen.user.listenpodcast.ListenPodcastActivity
import com.example.sel.screen.user.login.LoginActivity
import com.example.sel.screen.user.podcast2.Podcast2Activity
import com.example.sel.screen.user.profile.ProfileActivity
import com.example.sel.screen.user.ranking.RankingActivity

class HomeActivity : BaseBindingActivity<HomeViewModel, ActivityHomeBinding>(), View.OnClickListener {

    private var bottomSheetTopic: BottomSheetTopic? = null
    var topicID : Int ? = 0
    var postID : Int ? = 0
    override fun setActivityLayout(): Int {
        return R.layout.activity_home
    }

    override fun createViewModel(): HomeViewModel {
        return ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onActivityCreated() {
        super.onActivityCreated()
        initListener()
        initAdapter()
        viewModel.loadDataFromAPI()



    }
    override fun fetchData() {
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        binding?.executePendingBindings()
    }

    override fun setActivityName(): String {
        return "HomeActivity"
    }

    override fun observeData() {
        viewModel.convertedHomeMenuList.observe(this) {
            viewModel.convertListContractEnable(it.topics)
            it.topics?.forEach{
                topicID = it.id
            }
            viewModel.onClickHomeItemMenu = :: clickChooseTypePost
        }

        viewModel.convertedListPost.observe(this){
            viewModel.convertListPost(it.topic?.topic_id)
            it.topic?.topic_id?.forEach {
                postID = it.id
            }
            viewModel.onClickItemPost = :: clickChooseTypeQuestion
        }
    }
    private fun initAdapter() {
        val gridLayoutManager = GridLayoutManager(this@HomeActivity, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                //define span size for this position
                return if (viewModel.convertedItemHomeMenuList.value?.get(position)?.viewType == 2)
                    2
                else
                    1
            }
        }

        binding?.rvHomeMenu?.layoutManager= gridLayoutManager
    }

    private fun initListener() {
        binding?.btnUser?.setOnClickListener(this)
        binding?.btnViewMore?.setOnClickListener(this)
        binding?.btnTest?.setOnClickListener(this)
        binding?.btnnavpodcast?.setOnClickListener(this)
        binding?.btnnavdictinary?.setOnClickListener(this)
        binding?.btnnavprofile?.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnUser -> {
                Utils.startActivityWithResultCode(
                    this@HomeActivity,
                    BlogActivity(),
                    0,
                    null,
                )
            }
            R.id.btnViewMore -> {
                Utils.startActivityWithResultCode(
                    this@HomeActivity,
                    RankingActivity(),
                    0,
                    null,
                )
            }
            R.id.btn_test -> {
                postID = 31 // add post lam bai kiem tra
                Utils.startActivityWithResultCode(
                    this@HomeActivity,
                    ChooseAnswerActivity(),
                    0,
                    Bundle().apply {
                        putInt(Constants.BundleParam.POST_ID,postID?:0)
                    }
                )
            }
            R.id.btnnavpodcast -> {
                Utils.startActivityWithResultCode(
                    this@HomeActivity,
                    Podcast2Activity(),
                    0,
                    null
                )
            }
            R.id.btnnavprofile -> {
            Utils.startActivityWithResultCode(
                this@HomeActivity,
                ProfileActivity(),
                0,
                null
            )
        }
            R.id.btnnavdictinary -> {
                Utils.startActivityWithResultCode(
                    this@HomeActivity,
                    DictionaryActivity(),
                    0,
                    null
                )
            }
        }
    }
    fun clickChooseTypePost(homeMenuItem: ItemHomeMenuConverted){
            topicID = homeMenuItem.id
            viewModel.loadDataFromAPIPost(topicID ?: 0)
            BottomSheetTopic.newInstance().show(supportFragmentManager, "bottomSheetTag")
    }
    fun clickChooseTypeQuestion(homeMenuItem: ItemPostConvert){
        postID = homeMenuItem.id
        Utils.startActivityWithResultCode(
            this@HomeActivity,
            ChooseAnswerActivity(),
            0,
            Bundle().apply {
                    putInt(Constants.BundleParam.POST_ID,postID?:0)
                }
        )
    }
}