package com.example.alfamessanger.presentation.activities.splashActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.alfamessanger.databinding.ActivitySplashScreenBinding
import com.example.alfamessanger.presentation.activities.registerActivity.RegisterActivity
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.StatusConnection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    private lateinit var mViewModel: SplashViewModel
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        //val splashScreen = installSplashScreen()
        setContentView(view)

        initFields()
        getConnection()

//        splashScreen.setKeepOnScreenCondition {
//            true }
    }
    private fun initFields() {
        AUTH = FirebaseAuth.getInstance()
        UID = AUTH.currentUser?.uid ?: ""
        DATABASE_REFERENCE = Firebase.database.reference
        STORAGE_REFERENCE = FirebaseStorage.getInstance().reference
        mViewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        AppPreferences.getPreferences(this)
    }

    private fun getConnection() {
        if (mViewModel.checkConnectionInMoment()) {
            loadInfo()
        } else {
            binding.noConnectionLabel.visibility = View.VISIBLE
            binding.noConnection.visibility = View.VISIBLE
            observNetworkConnection()
        }
    }

    private fun observNetworkConnection() {
        mViewModel.observConnection { status ->
            if (status == StatusConnection.AVAILABLE) {
                binding.noConnectionLabel.visibility = View.INVISIBLE
                binding.connectionLabel.visibility = View.VISIBLE
                loadInfo()
            }
        }
    }

    private fun loadInfo() {
        mViewModel.getAllData {
            replaceActivity(this, RegisterActivity())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.removeListener()
    }
}