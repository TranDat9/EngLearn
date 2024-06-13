package com.example.sel.screen.user

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.sel.R
import com.example.sel.base.ApiService
import com.example.sel.base.ApiService.Companion.apiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_history)

        lifecycleScope.launch {
            delay(10000)
            val response = ApiService.apiService.getHistoryByUser(17)
            if (response.isSuccessful) {
                val historyExams = response.body()
                if (!historyExams.isNullOrEmpty()) {
                    val firstExam = historyExams[1].toString()
                    Toast.makeText(this@HistoryActivity, firstExam, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@HistoryActivity, "No history exams available", Toast.LENGTH_LONG).show()
                }
            } else {
                // Xử lý lỗi
            }
        }

    }
}