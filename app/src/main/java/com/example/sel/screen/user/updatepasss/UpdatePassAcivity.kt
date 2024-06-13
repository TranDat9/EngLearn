package com.example.sel.screen.user.updatepasss

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.sel.R
import com.example.sel.base.BaseBindingActivity
import com.example.sel.base.Utils
import com.example.sel.base.model.RequestUpdatePass
import com.example.sel.databinding.AcivityUpdatePasssBinding
import com.example.sel.databinding.ActivityFogotPassBinding
import com.example.sel.screen.user.home.HomeActivity
import com.example.sel.screen.user.login.LoginActivity
import com.example.sel.screen.user.login.LoginViewModel
import com.example.sel.screen.user.profile.ProfileActivity

class UpdatePassAcivity : BaseBindingActivity<UpdatePassViewModel, ActivityFogotPassBinding>(),
    View.OnClickListener {

    override fun setActivityLayout(): Int {
        return R.layout.activity_fogot_pass
    }

    override fun onActivityCreated() {
        super.onActivityCreated()
        initListener()

    }

    override fun createViewModel(): UpdatePassViewModel {
        return ViewModelProvider(this).get(UpdatePassViewModel::class.java)
    }

    override fun fetchData() {
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        binding?.executePendingBindings()
    }

    override fun setActivityName(): String {
        return "UpdatePassAcivity"
    }

    override fun observeData() {
        viewModel.registerSuccess.observe(this) { success ->
            if (success) {
                viewModel.updatePass.observe(this){
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    Utils.startActivityWithResultCode(
                        this@UpdatePassAcivity,
                        LoginActivity(),
                        0,
                        null,
                    )
                }
            } else
                viewModel.errorMessage.observe(this) { message ->
                message?.let {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun initListener() {
        binding?.imgBack?.setOnClickListener(this)
        binding?.btLogin?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_login -> {
                val name = binding?.layoutName?.edtName?.text.toString()
                val email = binding?.layoutEmail?.edtEmail?.text.toString()
                val password_cf = binding?.layoutPasswordCf?.edtPassword?.text.toString()
                val input = RequestUpdatePass(email, name, password_cf)

                if (email.isNullOrEmpty() || password_cf.isNullOrEmpty() || name.isNullOrEmpty()) {
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.loadDataFromAPI(input)
                }
            }
            R.id.img_back -> {
                Utils.startActivityWithResultCode(
                    this@UpdatePassAcivity,
                    LoginActivity(),
                    0,
                    null,
                )
            }
        }
    }
}

