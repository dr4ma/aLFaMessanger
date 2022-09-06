package com.example.alfamessanger.presentation.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.alfamessanger.R
import com.example.alfamessanger.utills.APP_ACTIVITY_REGISTER
import com.example.alfamessanger.utills.AUTH
import com.example.alfamessanger.utills.replaceActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        replaceActivity(this, RegisterActivity())
    }
}