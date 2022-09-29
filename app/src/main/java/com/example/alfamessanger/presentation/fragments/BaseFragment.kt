package com.example.alfamessanger.presentation.fragments

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.alfamessanger.R
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import com.example.alfamessanger.utills.hideKeyBoard


open class BaseFragment : Fragment() {

    override fun onStart() {
        super.onStart()

        hideKeyBoard()
        APP_ACTIVITY_MAIN.setSupportActionBar(APP_ACTIVITY_MAIN.findViewById(R.id.toolbar))
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowTitleEnabled(false)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowHomeEnabled(false)
        APP_ACTIVITY_MAIN.supportActionBar?.setHomeButtonEnabled(false)
        APP_ACTIVITY_MAIN.window.statusBarColor = ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.primaryColor)
    }
}