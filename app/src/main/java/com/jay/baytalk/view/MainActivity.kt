package com.jay.baytalk.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.*
import com.jay.baytalk.adapter.PageAdapter
import com.jay.baytalk.base.BaseActivity
import com.jay.baytalk.presenter.MainConstract
import com.jay.baytalk.presenter.MainPresenter
import com.jay.baytalk.static.Companion.frag
import com.jay.baytalk.static.Companion.loginActivity
import com.jay.baytalk.static.Companion.userName
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.com.jay.baytalk.RTCClient

class MainActivity : BaseActivity(), MainConstract.View {
    private lateinit var user : FirebaseUser
    private lateinit var mPresenter: MainPresenter
    private lateinit var userData : ArrayList<String>

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        private const val TAG = "MainActivity"
    }

    private lateinit var rtcClient: RTCClient
    var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        static.mainActivity = this
        Log.d(TAG, "onCreate!!!!!!")
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

        mPresenter.takeView(this)

        connectAdapter()

        loadName(auth.currentUser)

        setButton()

        checkCameraPermission()
    }

    private fun connectAdapter() {
        val adapter = PageAdapter(supportFragmentManager)
        adapter.addItem(FriendFragment())
        adapter.addItem(ChatFragment())
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

    private fun loadName(currentUser: FirebaseUser?) {
        currentUser?.let {
            mPresenter.welcome(currentUser, object : MyCallback {
                override fun onCallback(value: List<Any>?) {
                    userName = value?.get(0).toString()
//                Toast.makeText(baseContext, userName + "님 환영합니다", Toast.LENGTH_LONG).show()
                    userData = arrayListOf(auth.currentUser!!.uid, value?.get(0).toString())
                }
            })
        }
    }

    override fun initPresenter() {
        mPresenter = MainPresenter()
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
        if (frag == null) {
            this.alert("앱을 종료하시겠습니까??", "종료") {
                yesButton {
                    super.onBackPressed()
                }
                noButton {
                }
            }.show()
        } else {
            supportFragmentManager.popBackStack()
            frag = null
        }
    }

    override fun makeRoom(){
        val fragmentManager = supportFragmentManager
        val newFragment = MakeChatroomFragment()
        newFragment.arguments = bundleOf(Pair("key", userData))
        val transaction = fragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.do_nothing,
            R.anim.do_nothing,
            R.anim.enter_from
        )
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun showError(error: String) {}

    override fun ShowToast(text: String) {}

    override fun setPresenter(presenter: MainConstract.Presenter) {}

    override fun showLoading() {}

}
