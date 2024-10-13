package com.example.eco_pjt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {
    private val TAG = "KAKAO_LOGIN"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("THIS_IS_OUR_HASH_KEY", "keyhash : ${Utility.getKeyHash(this)}")
        setContentView(R.layout.activity_main)

        val emailEditText = findViewById<EditText>(R.id.login_email)
        val passwordEditText = findViewById<EditText>(R.id.login_password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val kakaoLoginButton = findViewById<ImageButton>(R.id.kakao_login_button)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Perform login validation and authentication here
            if (email == "user@example.com" && password == "password") {
                // Login successful
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Login failed
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }

        kakaoLoginButton.setOnClickListener{
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Toast.makeText(this, "Failed to login", Toast.LENGTH_SHORT).show()
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SecondActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                Toast.makeText(this, "Login canceled by user", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        } else if (token != null) {
            Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
