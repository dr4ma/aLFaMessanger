package com.example.alfamessanger.utills

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

object AppPreferences {

    private const val INIT_THEME = "theme"
    private const val INIT_STATUS = "status"
    private const val INIT_GRAFFITI = "graffiti"
    private const val INIT_PRIVATE_ACCOUNT = "private_account"
    private const val INIT_ADAPTER = "adapter"
    private const val PREF = "preferences"

    private lateinit var mPreferences: SharedPreferences

    fun getPreferences(context: Context): SharedPreferences {
        mPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
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

    fun setAdapterMemory(adapter : String) {
        mPreferences.edit().putString(INIT_ADAPTER, adapter).apply()
    }

    fun getAdapterMemory(): String {
        return mPreferences.getString(INIT_ADAPTER, ADAPTER_CHATS).toString()
    }

    fun setPrivateAccount(private : Boolean) {
        mPreferences.edit().putBoolean(INIT_PRIVATE_ACCOUNT, private).apply()
    }

    fun getPrivateAccount(): Boolean {
        return mPreferences.getBoolean(INIT_PRIVATE_ACCOUNT, false)
    }
}