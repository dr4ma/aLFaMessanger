package com.example.alfamessanger.presentation.fragments.myChannels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.FragmentFriendsListBinding
import com.example.alfamessanger.databinding.FragmentMyChatsBinding
import com.example.alfamessanger.databinding.MyChannelsFragmentBinding
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.fragments.friendsList.FriendsListAdapter
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import com.example.alfamessanger.utills.KEY_CHANNEL
import com.example.alfamessanger.utills.enums.ToolbarSettings
import com.example.alfamessanger.utills.toolbarTools
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyChannelsFragment : Fragment(), MyChannelsAdapter.OnItemClickListener {

    private val mViewModel: MyChannelsViewModel by viewModels()
    private var binding: MyChannelsFragmentBinding? = null
    private val mBinding get() = binding!!
    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mAdapter : MyChannelsAdapter
    private lateinit var mObserverChannels: Observer<MutableList<ChannelModel>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyChannelsFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initFields()
        initObservers()
    }

    private fun initFields(){
        toolbarTools(ToolbarSettings.PROFILE_SETTINGS, getString(R.string.my_channels))
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowHomeEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setHomeButtonEnabled(true)
        APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
        mRecyclerView = mBinding.recyclerChannelsList
        mAdapter = MyChannelsAdapter(this)
        val layoutManager = LinearLayoutManager(APP_ACTIVITY_MAIN)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.adapter = mAdapter
    }

    private fun initObservers(){
        mObserverChannels = Observer {
            mRecyclerView.visibility = View.VISIBLE
            mAdapter.setList(it)
        }
        mViewModel.channels.observe(APP_ACTIVITY_MAIN, mObserverChannels)
        mViewModel.getMyChannels(){

        }
    }

    override fun onItemClickAdapter(model: ChannelModel) {
        val bundle = Bundle()
        bundle.putSerializable(KEY_CHANNEL, model)
        APP_ACTIVITY_MAIN.mNavController.navigate(
            R.id.action_myChannelsFragment_to_channelFragment,
            bundle
        )
    }

}