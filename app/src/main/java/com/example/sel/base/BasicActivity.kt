package com.example.sel.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Space
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.sel.R
import com.example.sel.base.Utils.Companion.hideKeyboard

import java.util.*


abstract class BasicActivity<ViewModel : AndroidViewModel> : AppCompatActivity(),
    LoadingFragment.OnLoadingFragmentInteractionListener {
    //Live Data
    lateinit var viewModel: ViewModel
    abstract fun createViewModel(): ViewModel
    abstract fun fetchData()
    abstract fun onClick(v: View?)
    abstract fun onActivityCreated()


    /**
     * create getValue() to receive data from bundle before fetchData()
     * */
    open fun getValue(){}
    private var errorCodeMapTracker: HashMap<String, String>? = null
    protected var mMessengerErrorService: Messenger? = null
    protected var mErrorServiceBound: Boolean = false
    var mLocalBroadcastReceiver: BroadcastReceiver? = null
    var activityName = ""
    var firstTimeStart = true

    abstract fun setActivityView()

    abstract fun setActivityName(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityName = setActivityName()
        setActivityView()
        onActivityCreated()
    }


    abstract fun observeData()
    //End Live Data

    //Start Loading
    abstract fun setLoadingView(): View?

    protected var loadingView: View? = null

    public fun hideProgressing() {
        loadingView?.visibility = View.GONE
    }

    fun showProgressing() {
        loadingView?.visibility = View.VISIBLE
    }



    fun showLoadingView(value : Boolean){
        if (value){
            loadingView = setLoadingView()
            return
        }
        loadingView = null
    }

    protected var pendingForNextResumeDueToDeepLink = false
    override fun onResume() {
        super.onResume()

    }
    override fun onLoadingFragmentInteraction(bundle: Bundle) {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //update popup item

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()

    }
}


