package com.jay.baytalk.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.InfoManager
import com.jay.baytalk.InfoManager.frag
import com.jay.baytalk.InfoManager.loginActivity
import com.jay.baytalk.InfoManager.userData
import com.jay.baytalk.InfoManager.userName
import com.jay.baytalk.R
import com.jay.baytalk.adapter.PageAdapter
import com.jay.baytalk.base.BaseActivity
import com.jay.baytalk.contract.MainConstract
import com.jay.baytalk.extension.showToaster
import com.jay.baytalk.presenter.MainPresenter
import com.jay.baytalk.view.init.LoginActivity
import com.jay.baytalk.view.init.SplashActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.com.jay.baytalk.RTCClient

class MainActivity : BaseActivity(), MainConstract.View {

    private lateinit var mPresenter: MainConstract.Presenter
    private lateinit var functions: FirebaseFunctions

    private val CAMERA_PERMISSION = Manifest.permission.CAMERA
    private val CAMERA_PERMISSION_REQUEST_CODE = 1

    private lateinit var rtcClient: RTCClient
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InfoManager.mainActivity = this
        InfoManager.mIsInForegroundMode = true
        auth.currentUser
        if (auth.currentUser != null) {
            startActivity(Intent(this, SplashActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        setContentView(R.layout.activity_main)
        loginActivity?.finish()

        connectAdapter()

        loadName(auth.currentUser)

        functions = Firebase.functions

        setButton()
        setFcm()
        checkCameraPermission()
    }

    private fun setFcm() {
        mPresenter.setFcm(applicationContext)
    }

    private fun connectAdapter() {
        val adapter = PageAdapter(supportFragmentManager)
        adapter.addItem(FriendFragment())
        adapter.addItem(RoomListFragment())
        main_viewPager.adapter = adapter
        main_tabLayout.setupWithViewPager(main_viewPager)

        main_tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_person_24)
        main_tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_chat_24)
    }

    private fun setButton() {
        faceChat.setOnClickListener {
            AlertDialog.Builder(this).setTitle("Error").setMessage(userName+"님 서버가 닫혀있습니다")
                .create().show()
        }
    }

    private fun addMessage(text: String): Task<String> {
        // Create the arguments to the callable function.
        val data = hashMapOf(
            "text" to text,
            "push" to true
        )

        return functions
            .getHttpsCallable("addMessage")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as String
                result
            }
    }

    override fun onResume() {
        super.onResume()
        InfoManager.mIsInForegroundMode = true
    }
    override fun onPause(){
        super.onPause()
        InfoManager.mIsInForegroundMode = false
    }

    private fun loadName(currentUser: FirebaseUser?) {
        currentUser?:return
        mPresenter.welcome(currentUser)
    }

    override fun welcomeMent(name : String){
        userName = name
        this.showToaster("${userName}님 환영합니다")
        userData = arrayListOf(auth.currentUser!!.uid, userName!!)
    }

    override fun initPresenter() {
        mPresenter = MainPresenter()
        mPresenter.takeView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.dropView()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                CAMERA_PERMISSION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
            onCameraPermissionGranted()
        } else onCameraPermissionGranted()
    }

    private fun onCameraPermissionGranted() {
//        rtcClient = RTCClient(application, local_view)
//        rtcClient.startLocalVideoCapture()
    }


    override fun onBackPressed() {
        // FCM관련 처리
        if (frag == null) {
            InfoManager.mIsInForegroundMode = false
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
            frag = null
        }
    }

    override fun showError(error: String) {}

    override fun showToast(msg: String) {
        this.showToaster(msg)
    }

    override fun showLoading() {}

}
