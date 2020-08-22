package com.jay.baytalk.view

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.jay.baytalk.R
import com.jay.baytalk.static


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        static.activity = this      // friendlist가 모두 로딩되면. fragment에서 finish로 종료한다
        val view = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
            view.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.parseColor("#58ccff")
        } else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            window.statusBarColor = Color.BLACK
        }
        Handler().postDelayed({
            Toast.makeText(baseContext,"login",Toast.LENGTH_SHORT)
            finish()
        }, 2000)

    }

    override fun onBackPressed() {}
}
