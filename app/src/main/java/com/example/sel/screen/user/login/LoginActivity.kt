package com.example.sel.screen.user.login


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.util.Util
import com.example.sel.R
import com.example.sel.base.BaseBindingActivity
import com.example.sel.base.Constants
import com.example.sel.base.TokenManager
import com.example.sel.base.Utils
import com.example.sel.base.model.RequestLogin
import com.example.sel.databinding.ActivityLoginBinding
import com.example.sel.databinding.ActivityRegisterBinding
import com.example.sel.screen.user.home.HomeActivity
import com.example.sel.screen.user.register.RegisterActivity
import com.example.sel.screen.user.updatepasss.UpdatePassAcivity
import kotlinx.coroutines.launch

class LoginActivity : BaseBindingActivity<LoginViewModel, ActivityLoginBinding>(),View.OnClickListener {
    var userID : Int? = null
    var userName : String? = null

    var userEmail : String? = null
    override fun setActivityLayout(): Int {
        return R.layout.activity_login
    }
    override fun onActivityCreated() {
        super.onActivityCreated()
        initListener()

    }

    override fun createViewModel(): LoginViewModel {
        return ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun fetchData() {
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        binding?.executePendingBindings()
    }

    override fun setActivityName(): String {
        return "LoginActivity"
    }

    override fun observeData() {
        viewModel.loginData.observe(this) { success ->
            if (success.status == true) {
                viewModel.submitExams.observe(this) {data ->
                    TokenManager.token = data.data?.access_token
                    userID = data.data?.user?.id
                    val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putInt("USER_ID", userID ?: 0)
                    editor.apply()

                    userName = data.data?.user?.name
                    val nameUser = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    val _nameUser = nameUser.edit()
                    _nameUser.putString("USER_NAME", userName ?: "")
                    _nameUser.apply()

                    userEmail = data.data?.user?.email
                    val emailUser = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    val _emailUser = emailUser.edit()
                    _emailUser.putString("USER_EMAIL", userEmail ?: "")
                    _emailUser.apply()


                    Utils.startActivityWithResultCode(
                        this@LoginActivity,
                        HomeActivity(),
                        0,
                        null,
                    )
                }

                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Login false : Vui Lòng kiểm tra laị thông tin", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initListener() {
        binding?.tvRegister?.setOnClickListener(this)
        binding?.btnsignIn?.setOnClickListener(this)
        binding?.tvForgotPassword?.setOnClickListener(this)
    }
     override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_register -> {
                Utils.startActivityWithResultCode(
                    this@LoginActivity,
                    RegisterActivity(),
                    0,
                    null,
                )
            }

            R.id.tv_forgot_password -> {
                Utils.startActivityWithResultCode(
                    this@LoginActivity,
                    UpdatePassAcivity(),
                    0,
                    null,
                )
            }
            R.id.btnsignIn -> {
                val email = binding?.layoutEmail?.edtEmail?.text.toString()
                val password = binding?.layoutPassword?.edtPassword?.text.toString()
                val input = RequestLogin(email,password)
                if(email.isEmpty() ||password.isEmpty()){
                    Toast.makeText(this, " Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.loadDataFromAPI(input)
                }
            }
        }
    }
}


