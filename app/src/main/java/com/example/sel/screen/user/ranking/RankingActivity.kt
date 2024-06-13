package com.example.sel.screen.user.ranking

import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.sel.R
import com.example.sel.base.BaseBindingActivity
import com.example.sel.base.Utils
import com.example.sel.base.model.ResponseRank
import com.example.sel.databinding.ActivityHomeBinding
import com.example.sel.databinding.ActivityRankingBinding
import com.example.sel.screen.user.historyexam.HistoryExamActivity
import com.example.sel.screen.user.home.HomeActivity
import com.example.sel.screen.user.home.HomeViewModel

class RankingActivity : BaseBindingActivity<RankingViewModel, ActivityRankingBinding>(), View.OnClickListener{
    override fun setActivityLayout(): Int {
        return R.layout.activity_ranking
    }

    override fun createViewModel(): RankingViewModel {
        return ViewModelProvider(this).get(RankingViewModel::class.java)

    }
    override fun onActivityCreated() {
        super.onActivityCreated()
        initListener()
        viewModel.loadDataFromAPI()
    }

    private fun initListener() {
        binding?.imgVBackRank?.setOnClickListener(this)
        binding?.imghistory?.setOnClickListener(this)
    }
    override fun fetchData() {
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        binding?.executePendingBindings()

    }
    override fun setActivityName(): String {
        return "RankingActivity"
    }

    override fun observeData() {
        viewModel.responseRank.observe(this) {responseRank ->
            if (responseRank != null) {
                val topThreeScores = getTopThreeMaxScores(responseRank)
                if (topThreeScores.isNotEmpty()) {
                    viewModel.topScore1.postValue(topThreeScores.getOrNull(0).toString())
                    viewModel.topScore2.postValue(topThreeScores.getOrNull(1).toString())
                    viewModel.topScore3.postValue(topThreeScores.getOrNull(2).toString())
                }
            }

            viewModel.convertListContractEnable(responseRank)
        }
    }
    private fun getTopThreeMaxScores(responseRanks: List<ResponseRank>): List<Int> {
        return responseRanks
            .mapNotNull { it.max_score }
            .sortedDescending()
            .take(3)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgVBackRank -> {
                Utils.startActivityWithResultCode(
                    this@RankingActivity,
                    HomeActivity(),
                    0,
                    null,
                )
            }
            R.id.imghistory -> {
                Utils.startActivityWithResultCode(
                    this@RankingActivity,
                    HistoryExamActivity(),
                    0,
                    null,
                )
            }
        }
    }
}