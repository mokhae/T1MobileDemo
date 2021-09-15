package com.turck.t1mobiledemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registeruser.*
import kotlinx.android.synthetic.main.activity_registeruser.btnLogin
import kotlinx.android.synthetic.main.activity_registeruser.txtPassword
import kotlinx.android.synthetic.main.activity_registeruser.txtUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterUser : AppCompatActivity() {
    val urlStr : String = "http:61.74.63.163:8080/SSMSolution/core/userManagement/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_registeruser)

        val retrofit = Retrofit.Builder()
            .baseUrl( urlStr)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val server = retrofit.create(HowlService::class.java)

        btnLogin.setOnClickListener {

            server.insertRegister( txtUser.text.toString(),  txtUser.text.toString(),
                 txtEmail.text.toString(),  txtPassword.text.toString()).enqueue(object :
                Callback<ResponseIn> {
                override fun onFailure(call: Call<ResponseIn>?, t: Throwable?) {
                    txtResult.text = "사용자 등록 실패."
                }

                override fun onResponse(call: Call<ResponseIn>?, response: Response<ResponseIn>?) {
                    println(response?.body().toString())

                    if (response?.body()?.resultStatus == "1") {
                        Toast.makeText(applicationContext, " M3 Android PDA Demo", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, "신규 사용자 등록 실패", Toast.LENGTH_LONG).show()

                        txtResult.text = response?.body()?.resultMessage
                    }
                }

            })
        }
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }


    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 4
    }

    private fun attemptInsertUser() {
//        if (mAuthTask != null) {
//            return
//        }

        // Reset errors.
        txtUser.error = null
        txtEmail.error = null
        txtPassword.error = null
        txtPassword2.error = null

        // Store values at the time of the login attempt.
        val emailStr = txtUser.text.toString()
        val passwordStr = txtPassword.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            txtPassword.error = getString(R.string.error_invalid_password)
            focusView = txtPassword
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            txtUser.error = getString(R.string.error_field_required)
            focusView = txtUser
            cancel = true
//        } else if (!isEmailValid(emailStr)) {
//            email.error = getString(R.string.error_invalid_email)
//            focusView = email
//            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true)
//            mAuthTask = UserLoginTask(emailStr, passwordStr)
//            mAuthTask!!.execute(null as Void?)
        }
    }
}
