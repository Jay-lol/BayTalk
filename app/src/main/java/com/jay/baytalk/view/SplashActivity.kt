package com.jay.baytalk.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.jay.baytalk.R
import com.jay.baytalk.static


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        static.activity = this      // friendlist가 모두 로딩되면. fragment에서 finish로 종료한다

        Handler().postDelayed({
            Toast.makeText(baseContext,"login",Toast.LENGTH_SHORT)
            finish()
        }, 2000)

    }

    override fun onBackPressed() {}
}
