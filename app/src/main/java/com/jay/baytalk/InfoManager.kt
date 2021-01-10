package com.jay.baytalk

import com.jay.baytalk.view.MainActivity
import com.jay.baytalk.view.MakeChatroomFragment
import com.jay.baytalk.view.init.LoginActivity
import com.jay.baytalk.view.init.SplashActivity

object InfoManager {
    var mainActivity: MainActivity? = null
    var loginActivity: LoginActivity? = null
    var activity: SplashActivity? = null
    var frag: MakeChatroomFragment? = null
    var userName: String? = null
    var mIsInForegroundMode: Boolean = false
    lateinit var userData : ArrayList<String>
}