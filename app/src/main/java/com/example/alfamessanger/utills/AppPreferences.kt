package com.example.alfamessanger.utills

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

object AppPreferences {

    private const val INIT_THEME = "theme"
    private const val INIT_STATUS = "status"
    private const val INIT_GRAFFITI = "graffiti"
    private const val PREF = "preferences"

    private lateinit var mPreferences: SharedPreferences

    fun getPreferences(): SharedPreferences {
        mPreferences = APP_ACTIVITY_MAIN.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return mPreferences
    }

    fun setTheme(mode: String) {
        mPreferences.edit().putString(INIT_THEME, mode).apply()
    }

    fun getTheme(): String {
        return mPreferences.getString(INIT_THEME, LIGHT_THEME).toString()
    }

    fun setHideStatus(mode: Boolean) {
        mPreferences.edit().putBoolean(INIT_STATUS, mode).apply()
    }

    fun getStatus(): Boolean {
        return mPreferences.getBoolean(INIT_STATUS, false)
    }

    fun setGraffiti(uri: Uri) {
        mPreferences.edit().putString(INIT_GRAFFITI, uri.toString()).apply()
    }

    fun getGraffiti(): String {
        return mPreferences.getString(INIT_GRAFFITI, "").toString()
    }
}