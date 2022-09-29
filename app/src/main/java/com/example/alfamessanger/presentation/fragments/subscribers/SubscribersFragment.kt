package com.example.alfamessanger.presentation.fragments.subscribers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.SubscribersFragmentBinding
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.fragments.friendsList.FriendsListAdapter
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.ToolbarSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscribersFragment : Fragment(), SubscribersAdapter.OnItemClickListener {

    private val mViewModel: SubscribersViewModel by viewModels()

    private var binding: SubscribersFragmentBinding? = null
    private val mBinding get() = binding!!

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: SubscribersAdapter
    private lateinit var mObserverLoadSubs: Observer<MutableList<UserModel>>

    private lateinit var mChannelModel: ChannelModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SubscribersFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initFields()
        initObservers()
    }

    private fun initFields() {
        toolbarTools(ToolbarSettings.PROFILE_SETTINGS, getString(R.string.subs_label))
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowHomeEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setHomeButtonEnabled(true)
        APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
        mChannelModel = arguments?.getSerializable(KEY_SUBS) as ChannelModel
        mRecyclerView = mBinding.recyclerSubscribers
        mAdapter = SubscribersAdapter(this)
        val layoutManager = LinearLayoutManager(APP_ACTIVITY_MAIN)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.adapter = mAdapter
    }

    private fun initObservers() {
        mObserverLoadSubs = Observer { subs ->
            mAdapter.setList(mChannelModel.adminUid, subs)
        }
        mViewModel.subs.observe(APP_ACTIVITY_MAIN, mObserverLoadSubs)
        mViewModel.getSubs(mChannelModel.channelId)
    }

    override fun onItemClickAdapter(model: UserModel) {
        goToProfile(model)
    }

    private fun goToProfile(model: UserModel) {
        if (model.uid == UID) {
            APP_ACTIVITY_MAIN.mNavController.navigate(
                R.id.action_subscribersFragment_to_accountFragment
            )
        } else {
            val bundle = Bundle()
            bundle.putSerializable(KEY_PROFILE, model)
            APP_ACTIVITY_MAIN.mNavController.navigate(
                R.id.action_subscribersFragment_to_userProfileFragment,
                bundle
            )
        }
    }

}