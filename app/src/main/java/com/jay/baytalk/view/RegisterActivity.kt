package com.jay.baytalk.view

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.jay.baytalk.R
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    private val TAG = "RegisterActivity"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth

        register.setOnClickListener{
            val id = emailRg.text.toString().trim()
            val password = passwordRg.text.toString().trim()
            val passwordCheck = passwordRg2.text.toString().trim()
            val progressDialog = ProgressDialog(this)

            if (password==passwordCheck && password.length >=6){
                Log.d(TAG, "등록버튼 $id, $password")

                progressDialog.setMessage("가입중입니다...")
                progressDialog.show()

                auth.createUserWithEmailAndPassword(id, password)
                    .addOnCompleteListener(this) { task ->
                            if(task.isSuccessful){
                                progressDialog.dismiss()
                                val user = auth.currentUser
                                val hashMap: HashMap<Any, String> = HashMap()
//                                hashMap.put("uid", user!!.uid)
                                hashMap["name"] = nameRg.text.toString().trim()
                                hashMap["email"] = user?.email!!
                                hashMap["statusMessage"] = "신규유저 발견!!"

                                // Write a message to the database
                                val database = Firebase.database
                                var myRef = database
                                    .getReference("Users")
                                myRef.child(user.uid).setValue(hashMap)

                                // Read from the database

                                Toast.makeText(baseContext, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show()

                                finish()
                            } else {
                                progressDialog.dismiss()
                                Toast.makeText(baseContext, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show()
                                //해당 메소드 진행을 멈추고 빠져나감.
                            }
                        }


            } else if(password.length<6){
                progressDialog.dismiss()
                AlertDialog.Builder(this).setTitle("Error").setMessage("비밀번호는 6자 이상이어야 합니다.")
                    .create().show()
            }
            else {
                progressDialog.dismiss()
                AlertDialog.Builder(this).setTitle("Error").setMessage("비밀번호가 서로 다릅니다")
                    .create().show()
            }
        }
    }


}
