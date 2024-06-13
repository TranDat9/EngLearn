package com.example.sel.screen.user.home.BottomSheetTopic

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.sel.R
import com.example.sel.databinding.BottomDialogLayoutBinding
import com.example.sel.interfaces.CallToAction
import com.example.sel.screen.user.home.HomeViewModel

class BottomSheetTopic : DialogFragment(), View.OnClickListener {
    private lateinit var binding: BottomDialogLayoutBinding
    private lateinit var mContext: Context

    private var viewModel: HomeViewModel? = null
    companion object {
        @JvmStatic
        fun newInstance() =
            BottomSheetTopic().apply{
                isCancelable = false
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.bottom_dialog_layout, container, true
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.attributes.windowAnimations = R.style.DialogAnimation
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setGravity(Gravity.BOTTOM)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.dismissDialog = {
//            dismiss()
//        }
        initListener()
    }


    private fun initListener() {
        binding.btnClose?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnClose -> {
                dismiss()
            }
        }
    }
}