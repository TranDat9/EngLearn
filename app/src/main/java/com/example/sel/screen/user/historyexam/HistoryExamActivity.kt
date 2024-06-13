package com.example.sel.screen.user.historyexam

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sel.R
import com.example.sel.base.BaseBindingActivity
import com.example.sel.databinding.ActivityHistoryExamBinding


class HistoryExamActivity: BaseBindingActivity<HistoryExamViewModel, ActivityHistoryExamBinding>(), View.OnClickListener  {
    var historyExamId : Int ? = 0
    override fun setActivityLayout(): Int {
      return R.layout.activity_history_exam
    }
    override fun onActivityCreated() {
        super.onActivityCreated()
        initListener()
        initAdapter()
        val userId = getSharedPreferences("MyPrefs", MODE_PRIVATE).getInt("USER_ID", 0)
        viewModel.loadDataExamFromApi(userId)
    }

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(this@HistoryExamActivity, LinearLayoutManager.VERTICAL, false)
        binding?.rvHistoryExam?.layoutManager = linearLayoutManager
    }


    private fun initListener() {

    }

    override fun createViewModel(): HistoryExamViewModel {
        return ViewModelProvider(this).get(HistoryExamViewModel::class.java)
    }

    override fun fetchData() {
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        binding?.executePendingBindings()
    }



    override fun setActivityName(): String {
        return "History Exams"
    }

    override fun observeData() {
       viewModel.reponHistoryExam.observe(this){
         viewModel.convertListContractEnable(it.historyExams)
           it.historyExams?.forEach{
               historyExamId = it.id
           }
           //viewModel.onClickHomeItemMenu = :: clickChooseTypePost
       }
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }




}