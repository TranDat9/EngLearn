package com.example.sel.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.example.sel.R


abstract class BaseBindingActivity<ViewModel: BasicViewModel, V : ViewBinding?> : BasicActivity<ViewModel>() {
    abstract fun setActivityLayout(): Int

    protected var binding: V? = null
    protected var navController: NavController? = null
    protected var navHostFragment: NavHostFragment? = null
    protected open fun getNavigation(): NavController? {
        return navController
    }
    override fun onActivityCreated() {
        loadingView = setLoadingView()
        viewModel = createViewModel()
        getValue()
        observeData()
        fetchData()

    }

    protected open fun getHostFrag(): Fragment? {
        return navHostFragment
    }
    open fun setNavigation(layoutId: Int, graphId: Int, bundle: Bundle? = null) {

        navHostFragment = supportFragmentManager
            .findFragmentById(layoutId) as NavHostFragment
        navController = navHostFragment?.navController

        if (bundle != null)
            navController?.setGraph(graphId, bundle)
        else
            navController?.setGraph(graphId)
    }

    override fun setActivityView() {
        setContentView(R.layout.base_binding_activity)
//        binding = DataBindingUtil.inflate(layoutInflater,
//            setActivityLayout(),
//            rootViewBaseBinding,
//            true)
        binding = DataBindingUtil.setContentView(this, setActivityLayout())
    }


    override fun setLoadingView(): View? {
        return null
    }

    open fun navigate(actionId: Int, bundle: Bundle?){
        getNavigation()?.navigate(actionId,bundle,getNavOptions())
    }

    private fun getNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()
    }


    open fun popBackstack(bundle: Bundle? = null){
        if(getHostFrag()==null||getHostFrag()?.childFragmentManager?.backStackEntryCount?.compareTo(0) == 0){
            Utils.finishActivityWithAnim(this, Intent().apply{
                bundle?.let{
                    putExtras(it)
                }
            })
        } else {
            getNavigation()?.popBackStack()
        }
    }

    //Pop until certain screenId
    fun popBackStackById(screenId: Int) {
        getNavigation()?.popBackStack(screenId, false)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       
    }
}
