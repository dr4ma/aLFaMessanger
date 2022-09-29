package com.example.alfamessanger.presentation.activities.registerActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.alfamessanger.R
import com.example.alfamessanger.presentation.activities.mainActivity.MainActivity
import com.example.alfamessanger.utills.*
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initFields()
        initTheme()
        if (AUTH.currentUser != null) {
            replaceActivity(this, MainActivity())
        }

        mNavController = Navigation.findNavController(APP_ACTIVITY_REGISTER, R.id.nav_host_fragment)

        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
    }

    private fun initFields() {
        APP_ACTIVITY_REGISTER = this
    }

    private fun initTheme() {
        if (AppPreferences.getTheme() == NIGHT_THEME) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}