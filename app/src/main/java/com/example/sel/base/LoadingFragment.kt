package com.example.sel.base

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.sel.R


class LoadingFragment : DialogFragment(), View.OnClickListener {

    private var mListener: OnLoadingFragmentInteractionListener? = null
    private var dismissItSelf = false

    interface OnLoadingFragmentInteractionListener {
        fun onLoadingFragmentInteraction(bundle: Bundle)
    }

    fun setListener(l: OnLoadingFragmentInteractionListener?){
        mListener = l
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (mListener == null) {
            if (context is OnLoadingFragmentInteractionListener) {
                mListener = context
            } else {
                throw RuntimeException(context.toString() + " must implement OnLoadingFragmentInteractionListener")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initValue()
    }

    private fun initValue() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(false)
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoadingFragment()
    }

    override fun onClick(v: View?) {

    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            it.attributes.windowAnimations = R.style.DialogAnimation
            it.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            it.setGravity(Gravity.CENTER_VERTICAL)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun dismissCustomStyle(){
        dismissItSelf = true
//        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!dismissItSelf){
            val bundleDismiss = Bundle()
//            bundleDismiss?.putInt(Constants.ActionMain.callAction, Constants.ActionMain.backPressed)
            mListener?.onLoadingFragmentInteraction(bundleDismiss)
        }
    }

    private fun initView() {

    }
}
