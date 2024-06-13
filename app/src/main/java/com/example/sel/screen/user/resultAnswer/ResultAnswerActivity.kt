package com.example.sel.screen.user.resultAnswer

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.sel.R
import com.example.sel.base.BaseBindingActivity
import com.example.sel.base.Constants
import com.example.sel.base.Utils
import com.example.sel.base.model.AnsweredQuestion
import com.example.sel.base.model.RequestSubmitExams
import com.example.sel.databinding.ActivityFinishResultBinding
import com.example.sel.screen.user.home.HomeActivity

class ResultAnswerActivity : BaseBindingActivity<ResultAnswerViewModel, ActivityFinishResultBinding>(), View.OnClickListener{

    override fun setActivityLayout(): Int {
        return R.layout.activity_finish_result
    }
    override fun onActivityCreated() {
        super.onActivityCreated()
        initListener()
         viewModel.point.postValue((intent.extras?.getInt(Constants.BundleParam.POINT) ?: "").toString())

    }

    override fun createViewModel(): ResultAnswerViewModel {
        return ViewModelProvider(this).get(ResultAnswerViewModel::class.java)

    }

    override fun fetchData() {
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        binding?.executePendingBindings()
    }

    override fun setActivityName(): String {
        return "ResultAnswerActivity"
    }

    override fun observeData() {
    }
    private fun initListener() {
        binding?.btnSubmit?.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSubmit -> {
                var pointUser = (intent.extras?.getInt(Constants.BundleParam.POINT) ?: 0)
                val IdPost = (intent.extras?.getInt(Constants.BundleParam.IDPost) ?: 0)
                val userId = getSharedPreferences("MyPrefs", MODE_PRIVATE).getInt("USER_ID", 0)
                val userName = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("USER_NAME", "")
                val answeredQuestions = intent.getParcelableArrayListExtra<AnsweredQuestion>(Constants.BundleParam.ANSWERED_QUESTIONS)?.toList()
                val requestSubmitExams = RequestSubmitExams(
                    question_check = answeredQuestions,
                    user_id = userId,
                    score = pointUser,
                    fail_answer = 0 ,
                    correct_answer = 0,
                    name_user = userName
                )

                viewModel.getApiSubmitExams(IdPost,requestSubmitExams)
                Utils.startActivityWithResultCode(
                    this@ResultAnswerActivity,
                    HomeActivity(),
                    0,
                    null,
                )
            }
        }
    }
}