package com.jay.baytalk

import com.jay.baytalk.view.LoginActivity
import com.jay.baytalk.view.MainActivity
import com.jay.baytalk.view.SplashActivity

class static {
    companion object{
        var mainActivity : MainActivity? = null
        var loginActivity : LoginActivity? =  null
        var activity : SplashActivity? = null
        var frag : MakeChatroomFragment? = null
        var userName : String? = null
    }
}