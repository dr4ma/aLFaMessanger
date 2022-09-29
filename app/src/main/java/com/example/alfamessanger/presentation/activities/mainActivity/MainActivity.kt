package com.example.alfamessanger.presentation.activities.mainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.ActivityMainBinding
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.Status
import com.example.alfamessanger.utills.enums.StatusConnection
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var mNavController: NavController
    lateinit var paramsToolbar: AppBarLayout.LayoutParams
    lateinit var tintApp : View

    private var connectionChanged = false

    private lateinit var binding : ActivityMainBinding
    private lateinit var mViewModel : MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initFields()
        observNetworkConnection()

        mNavController = Navigation.findNavController(APP_ACTIVITY_MAIN, R.id.nav_host_fragment_main)
        navigation_view.setupWithNavController(mNavController)
    }

    private fun observNetworkConnection(){
        mViewModel.observConnection{ status ->
            if(status == StatusConnection.UNAVAILABLE || status == StatusConnection.LOST){
                connectionChanged = true
                showSnackBar(tintApp, getString(R.string.internet_is_unavailable), R.color.colorRed)
            }
            else if(status == StatusConnection.AVAILABLE && connectionChanged){
                connectionChanged = false
                showSnackBar(tintApp, getString(R.string.internet_available), R.color.colorGreen)
            }
        }
    }

    private fun initFields() {
        APP_ACTIVITY_MAIN = this
        mViewModel = ViewModelProvider(APP_ACTIVITY_MAIN)[MainActivityViewModel::class.java]
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        paramsToolbar = toolbar.layoutParams as AppBarLayout.LayoutParams
        tintApp = binding.tintApp
        paramsToolbar.scrollFlags = 0
        USER = UserModel()
        UID = AUTH.currentUser?.uid!!
    }

    fun initTintApp(typeMessage : String, from : String, view: View){
        tintApp.setOnClickListener {
            it.visibility = View.GONE
            APP_ACTIVITY_MAIN.window.statusBarColor = ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.primaryColor)
            if(from == RECEIVED_MESSAGE){
                if(typeMessage == TYPE_IMAGE){
                    view.setBackgroundResource(R.drawable.image_received_message_style)
                    view.setPadding(0)
                }
                else{
                    view.setBackgroundResource(R.drawable.received_messages_style)
                }
            }
            else{
                if(typeMessage == TYPE_IMAGE){
                    view.setBackgroundResource(R.drawable.came_image_messages_style)
                    view.setPadding(0)
                }
                else{
                    view.setBackgroundResource(R.drawable.came_messages_style)
                }
            }
        }
    }



    override fun onResume() {
        super.onResume()
        if(AppPreferences.getStatus()){
            setStatus(Status.NON)
        }
        else{
            setStatus(Status.ONLINE)
        }
    }

    override fun onStop() {
        super.onStop()
        if(!AppPreferences.getStatus()){
            setStatus(Status.OFFLINE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!AppPreferences.getStatus()){
            setStatus(Status.OFFLINE)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}