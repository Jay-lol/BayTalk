package com.jay.baytalk.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.R
import com.jay.baytalk.InfoManager
import com.jay.baytalk.InfoManager.loginActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginActivity = this
        InfoManager.activity?.finish()

        login.setOnClickListener {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("로그인중")
            progressDialog.show()
            login.isClickable = false
            if (username.text.toString().trim() != "" && password.text.toString().trim() != "") {
                Firebase.auth.signInWithEmailAndPassword(
                    username.text.toString().trim(),
                    password.text.toString().trim())
                    .addOnCompleteListener(this) {
                        progressDialog.dismiss()
                        if (it.isSuccessful) {
                            InfoManager.activity?.finish()
                            startActivity(Intent(this, MainActivity::class.java))
                            login.isClickable = true
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", it.exception)
                            Toast.makeText(
                                baseContext, "아이디와 비밀번호를 확인해주세요!",
                                Toast.LENGTH_SHORT
                            ).show()
                            login.isClickable = true
                        }
                    }
            } else {
                progressDialog.dismiss()
                login.isClickable = true
                Toast.makeText(
                    baseContext, "아이디와 비밀번호를 확인해주세요!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        textView3.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }
}
