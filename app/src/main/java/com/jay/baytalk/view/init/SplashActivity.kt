package com.jay.baytalk.view.init

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jay.baytalk.InfoManager
import com.jay.baytalk.R
import com.jay.baytalk.view.MainActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        InfoManager.activity = this      // friendlist 가 모두 로딩되면. fragment에서 finish로 종료한다
        val view = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
            view.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.parseColor("#58ccff")
        } else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            window.statusBarColor = Color.BLACK
        }

        startMainActivity()
    }

    private fun startMainActivity(){
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
            finish() }, 2000
        )
    }
}
