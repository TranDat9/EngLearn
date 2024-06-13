package com.example.sel.screen.user.choose_answer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.sel.R
import com.example.sel.base.BaseBindingActivity
import com.example.sel.base.Constants
import com.example.sel.base.Constants.BundleParam.Companion.A
import com.example.sel.base.Constants.BundleParam.Companion.B
import com.example.sel.base.Constants.BundleParam.Companion.C
import com.example.sel.base.Constants.BundleParam.Companion.D
import com.example.sel.base.Utils
import com.example.sel.base.model.AnsweredQuestion
import com.example.sel.base.model.ItemChooseAnswerConvert
import com.example.sel.databinding.ActivityChooseAnswerBinding
import com.example.sel.screen.user.home.HomeActivity
import com.example.sel.screen.user.resultAnswer.ResultAnswerActivity


class ChooseAnswerActivity : BaseBindingActivity<ChooseAnswerViewModel, ActivityChooseAnswerBinding>(), View.OnClickListener {

    private var currentQuestionIndex = 0
    private var newAnswer: String? = null
    private var answer: String? = null
    private var isAnswerSelected = false
    private var totalScore: Int = 0
    private var idPost : Int = -1
    private var listQuestion: Int = 1

    private val answeredQuestions = mutableListOf<AnsweredQuestion>()
    override fun setActivityLayout(): Int {
        return R.layout.activity_choose_answer
    }

    override fun onActivityCreated() {
        super.onActivityCreated()
        initListener()
        val ID = (intent.extras?.getInt(Constants.BundleParam.POST_ID) ?: 0)
        viewModel.getListQuestion(ID)

    }

    override fun createViewModel(): ChooseAnswerViewModel {
        return ViewModelProvider(this).get(ChooseAnswerViewModel::class.java)
    }

    override fun fetchData() {
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        binding?.executePendingBindings()
    }

    override fun setActivityName(): String {
        return "ChooseAnswerActivity"
    }

    override fun observeData() {
        viewModel.convertListQuestion.observe(this) { data ->
            data?.let { questionQuiz ->
                questionQuiz.questions?.firstOrNull()?.let {
                    viewModel.contentQuestion.postValue(it?.question)
                    viewModel.imgQuestion.postValue(it?.newImage)
                    answer = it.answer
                    binding?.txtAnswer?.text = it?.answer
                    viewModel.convertListItemAnswer(it?.choose)
                }
            }
            idPost = data.post?.id ?: 0
            listQuestion = data.questions?.size ?: 0
        }
        viewModel.onClickItemAnswer = ::onClickItemAnswer
    }

    private fun onClickItemAnswer(itemChooseAnswerConvert: ItemChooseAnswerConvert) {
        when (itemChooseAnswerConvert.type) {
            A -> {
                newAnswer = A
                isAnswerSelected = true
            }

            B -> {
                newAnswer = B
                isAnswerSelected = true
            }

            C -> {
                newAnswer = C
                isAnswerSelected = true
            }

            D -> {
                newAnswer = D
                isAnswerSelected = true
            }
        }
    }

    private fun initListener() {
        binding?.btnconfirmTN?.setOnClickListener(this)
        binding?.btnCloseQuestion?.setOnClickListener(this)

    }

    private fun showNextQuestionAnswer() {
        if (currentQuestionIndex < (viewModel.convertListQuestion.value?.questions?.size ?: 0) - 1) {
            val newQuestionAnswer = viewModel.convertListQuestion.value?.questions?.get(currentQuestionIndex)
            val isCorrect = newAnswer == answer
            answeredQuestions.add(
                AnsweredQuestion(
                    id = newQuestionAnswer?.id,
                    question = newQuestionAnswer?.question,
                    newImage = newQuestionAnswer?.newImage,
                    options = newQuestionAnswer?.options,
                    user_choose = newAnswer,
                    answer = newQuestionAnswer?.answer,
                    point = newQuestionAnswer?.point,
                    isCorrect = isCorrect
                )
            )

            if (isCorrect) {
                totalScore += newQuestionAnswer?.point ?: 0
            }
            viewModel.score.postValue(totalScore.toString())

            currentQuestionIndex++
            val nextQuestionAnswer = viewModel.convertListQuestion.value?.questions?.get(currentQuestionIndex)
            viewModel.contentQuestion.postValue(nextQuestionAnswer?.question)
            viewModel.imgQuestion.postValue(nextQuestionAnswer?.newImage)
            viewModel.convertListItemAnswer(nextQuestionAnswer?.choose)
            answer = nextQuestionAnswer?.answer
            binding?.txtAnswer?.text = nextQuestionAnswer?.answer
        } else {
            val newQuestionAnswer = viewModel.convertListQuestion.value?.questions?.get(currentQuestionIndex)
            val isCorrect = newAnswer == answer
            answeredQuestions.add(
                AnsweredQuestion(
                    id = newQuestionAnswer?.id,
                    question = newQuestionAnswer?.question,
                    newImage = newQuestionAnswer?.newImage,
                    options = newQuestionAnswer?.options,
                    user_choose = newAnswer,
                    answer = newQuestionAnswer?.answer,
                    point = newQuestionAnswer?.point,
                    isCorrect = isCorrect
                )
            )

            if (isCorrect) {
                totalScore += newQuestionAnswer?.point ?: 0
            }
            viewModel.score.postValue(totalScore.toString())

            Utils.startActivityWithResultCode(
                this@ChooseAnswerActivity,
                ResultAnswerActivity(),
                0,
                Bundle().apply {
                    putInt(Constants.BundleParam.POINT, totalScore)
                    putInt(Constants.BundleParam.IDPost, idPost)
                    putInt(Constants.BundleParam.SIZE_QUESTION, listQuestion)
                    putParcelableArrayList(Constants.BundleParam.ANSWERED_QUESTIONS, ArrayList(answeredQuestions))
                }
            )
        }
    }


    private fun showNextQuestionAnswerWithDelay(delayMillis: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            showNextQuestionAnswer()
            binding?.viewAnswer?.visibility = View.GONE // Ẩn viewAnswer
        }, delayMillis)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnconfirmTN -> {
                if (!isAnswerSelected) {
                    Toast.makeText(this, "Vui lòng chọn một đáp án", Toast.LENGTH_SHORT).show()
                    return
                }else{
                    if (newAnswer == answer) {
                        showNextQuestionAnswer()
                    } else {
                        binding?.viewAnswer?.visibility = View.VISIBLE
                        showNextQuestionAnswerWithDelay(1000)
                    }
                }
                isAnswerSelected = false
                newAnswer = null
            }
            R.id.btn_close_question -> {
                Utils.startActivityWithResultCode(
                    this@ChooseAnswerActivity,
                    HomeActivity(),
                    0,
                    null,
                )
            }
        }
    }
}