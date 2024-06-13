package com.example.sel.screen.user.register

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.sel.R
import com.example.sel.base.BaseBindingActivity
import com.example.sel.base.Utils
import com.example.sel.base.model.RequestRegister
import com.example.sel.databinding.ActivityRegisterBinding
import com.example.sel.screen.user.home.HomeActivity
import com.example.sel.screen.user.login.LoginActivity

class RegisterActivity: BaseBindingActivity<RegisterViewModel, ActivityRegisterBinding>(), View.OnClickListener  {

    override fun setActivityLayout(): Int {
       return R.layout.activity_register
    }

    override fun onActivityCreated() {
        super.onActivityCreated()
        initListener()

    }
    override fun createViewModel(): RegisterViewModel {
        return ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    override fun fetchData() {
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        binding?.executePendingBindings()
    }

    override fun setActivityName(): String {
        return "RegisterActivity"
    }

    override fun observeData() {
        viewModel.registerSuccess.observe(this) { success ->
            if (success.status == true ) {
                Utils.startActivityWithResultCode(
                    this@RegisterActivity,
                    LoginActivity(),
                    0,
                    null,
                )
                Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "Register Fail", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun initListener() {
        binding?.btLogin?.setOnClickListener(this)
        binding?.tvGoBackLogin?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_login -> {
                val name = binding?.layoutName?.edtName?.text.toString()
                val email = binding?.layoutEmail?.edtEmail?.text.toString()
                val password = binding?.layoutPassword?.edtPassword?.text.toString()
                val password_cf = binding?.layoutPasswordCf?.edtPassword?.text.toString()
                val input = RequestRegister(
                    name = name ,
                    email =email,
                    password = password,
                    password_confirmation = password_cf
                )
                if (email.isNullOrEmpty() || password.isNullOrEmpty() || password_cf.isNullOrEmpty()) {
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.loadDataFromAPI(input)
                }
            }
            R.id.tv_go_back_login -> {
                Utils.startActivityWithResultCode(
                    this@RegisterActivity,
                    LoginActivity(),
                    0,
                    null,
                )
            }
        }
    }
}