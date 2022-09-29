package com.example.alfamessanger.presentation.fragments.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.FragmentFriendsListBinding
import com.example.alfamessanger.databinding.NotificationFragmentBinding
import com.example.alfamessanger.presentation.fragments.notifications.notificationRecyclerView.views.AppViewNotificationFactory
import com.example.alfamessanger.presentation.fragments.singleChat.SingleChatAdapter
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import com.example.alfamessanger.utills.TAG
import com.example.alfamessanger.utills.enums.ToolbarSettings
import com.example.alfamessanger.utills.toolbarTools
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : Fragment() {
    
    private val mViewModel: NotificationViewModel by viewModels()
    private var binding: NotificationFragmentBinding? = null
    private val mBinding get() = binding!!

    @Inject
    lateinit var mAdapter: NotificationAdapter

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotificationFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        initFields()
    }

    private fun initFields(){
        toolbarTools(ToolbarSettings.PROFILE_SETTINGS, APP_ACTIVITY_MAIN.getString(R.string.notifications_feed))
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowHomeEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setHomeButtonEnabled(true)
        APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
        layoutManager = LinearLayoutManager(APP_ACTIVITY_MAIN)

        mRecyclerView = mBinding.notificationRecycler
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.isNestedScrollingEnabled = false

        mAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        mRecyclerView.adapter = mAdapter

        mViewModel.getNotificationsList {
            it.forEach { model ->
                mAdapter.addToList(AppViewNotificationFactory.getView(model))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}