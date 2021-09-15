package com.turck.t1mobiledemo

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.login_form
import kotlinx.android.synthetic.main.activity_main.login_progress
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("PLUGIN_WARNING")
class MainActivity : AppCompatActivity() {

    private var mAuthTask: UserLoginTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            Toast.makeText(applicationContext, " M3 Android PDA Demo", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
        }

        btnScanner.setOnClickListener {
            startActivity(Intent(this@MainActivity, BCDScannerActivity::class.java))
            //startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

//        val retrofit = Retrofit.Builder()
//            .baseUrl("http:61.74.63.163:8080/SSMSolution/core/userManagement/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val server = retrofit.create(HowlService::class.java)

        txtPassword.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        btnLogin.setOnClickListener {
            attemptLogin()
//            server.userLogin(txtUser.text.toString(), txtPassword.text.toString()).enqueue(object: Callback<ResponseLogin> {
//                override fun onFailure(call: Call<ResponseLogin>?, t: Throwable?) {
//                    txtResult.text = "로그인에 실패 했습니다."
//                }
//
//                override fun onResponse(call: Call<ResponseLogin>?, response: Response<ResponseLogin>?) {
//                    println(response?.body().toString())
//
//                    if (response?.body()?.resultStatus == "1")
//                    {
//                        startActivity(Intent(this@MainActivity, BCDScannerActivity::class.java))
//                    }
//                    else
//                    {
//                        txtResult.text = response?.body()?.resultMessage
//                    }
//                }
//
//            })
/*
            server.selectUserId("mokhae@naver.com").enqueue(object: Callback<ResponseSel> {
                override fun onFailure(call: Call<ResponseSel>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<ResponseSel>?, response: Response<ResponseSel>?) {
                    println(response?.body().toString())
                    txtResult.setText(response?.body()?.userId)

                }

            })

 */
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegisterUser::class.java))
            //startActivity(Intent(this@MainActivity, LoginActivity::class.java))
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        txtUser.error = null
        txtPassword.error = null

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
            showProgress(true)
            mAuthTask = UserLoginTask(emailStr, passwordStr)
            mAuthTask!!.execute(null as Void?)
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    @SuppressLint("StaticFieldLeak")
    inner class UserLoginTask internal constructor(private val mEmail: String, private val mPassword: String) :
        AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean? {
            // TODO: attempt authentication against a network service.
            var ret : Boolean = false
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http:61.74.63.163:8080/SSMSolution/core/userManagement/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val server = retrofit.create(HowlService::class.java)
                server.userLogin(mEmail, mPassword).enqueue(object: Callback<ResponseLogin> {
                    override fun onFailure(call: Call<ResponseLogin>?, t: Throwable?) {
                        ret = false
                        txtResult.text = "로그인에 실패 했습니다."
                    }

                    override fun onResponse(call: Call<ResponseLogin>?, response: Response<ResponseLogin>?) {
                        println(response?.body().toString())

                        if (response?.body()?.resultStatus == "1")
                        {
                            ret = true

                        }
                        else
                        {
                            ret = false
                            txtResult.text = response?.body()?.resultMessage
                        }
                    }

                })

                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                return false
            }

            return ret
        }

        override fun onPostExecute(success: Boolean?) {
            mAuthTask = null
            showProgress(false)

            if (success!!) {
                //finish()
                startActivity(Intent(this@MainActivity, BCDScannerActivity::class.java))
            } else {
                txtPassword.error = getString(R.string.error_incorrect_password)
                txtPassword.requestFocus()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }

    companion object {

        /**
         * Id to identity READ_CONTACTS permission request.
         */
        private val REQUEST_READ_CONTACTS = 0

        /**
         * A dummy authentication store containing known user names and passwords.
         * TODO: remove after connecting to a real authentication system.
         */
        private val DUMMY_CREDENTIALS = arrayOf("foo@example.com:hello", "bar@example.com:world")
    }
}

