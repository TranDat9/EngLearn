package com.example.sel.screen.user.profile

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.sel.R
import com.example.sel.base.BaseBindingActivity
import com.example.sel.base.Utils
import com.example.sel.base.model.RequesPasword
import com.example.sel.databinding.ActivityUpdateAccountBinding
import com.example.sel.screen.user.home.HomeActivity
import com.example.sel.screen.user.login.LoginActivity

class ProfileActivity : BaseBindingActivity<ProfileViewModel, ActivityUpdateAccountBinding>(), View.OnClickListener  {
    override fun setActivityLayout(): Int {
        return R.layout.activity_update_account
    }
    override fun onActivityCreated() {
        super.onActivityCreated()
        initListener()
        val userName = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("USER_NAME", "")
        val userEmail = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("USER_EMAIL", "")
        binding?.layoutName?.edtName?.setText(userName)
        binding?.layoutEmail?.edtEmail?.setText(userEmail)
    }

    override fun createViewModel(): ProfileViewModel {
        return ViewModelProvider(this).get(ProfileViewModel::class.java)

    }

    override fun fetchData() {
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        binding?.executePendingBindings()

    }
    override fun setActivityName(): String {
        return "ProfileActivity"
    }

    private fun initListener() {
        binding?.buttonCapNhat?.setOnClickListener(this)
        binding?.buttonLogOut?.setOnClickListener(this)
        binding?.imgHOME?.setOnClickListener(this)
    }
    override fun observeData() {
        viewModel.updaterSuccess.observe(this) { success ->
            if (success) {
                Utils.startActivityWithResultCode(
                    this@ProfileActivity,
                    HomeActivity(),
                    0,
                    null,
                )
                Toast.makeText(this, "Cập nhật thành công ", Toast.LENGTH_SHORT).show()
            }else viewModel.updaterFail.observe(this) { message ->
                message?.let {
                    Toast.makeText(this, "Vui lòng kiểm tra lại ", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgHOME -> {
                Utils.startActivityWithResultCode(
                    this@ProfileActivity,
                    HomeActivity(),
                    0,
                    null,
                )
            }
            R.id.buttonLogOut -> {
                Utils.startActivityWithResultCode(
                    this@ProfileActivity,
                    LoginActivity(),
                    0,
                    null,
                )
            }
            R.id.buttonCapNhat -> {
                val name = binding?.layoutName?.edtName?.text.toString()
                val email = binding?.layoutEmail?.edtEmail?.text.toString()
                val input = RequesPasword(
                  email=email, name = name)
                val userId = getSharedPreferences("MyPrefs", MODE_PRIVATE).getInt("USER_ID", 0)
                if (name.isNullOrEmpty() || email.isNullOrEmpty()) {
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.loadDataFromAPI(userId, input)
                }
            }
        }
    }
}