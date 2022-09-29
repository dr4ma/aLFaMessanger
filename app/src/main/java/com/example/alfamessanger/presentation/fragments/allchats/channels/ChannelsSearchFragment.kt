package com.example.alfamessanger.presentation.fragments.allchats.channels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.ChannelFragmentBinding
import com.example.alfamessanger.databinding.ChannelsFeedItemBinding
import com.example.alfamessanger.databinding.FragmentChannelsSearchBinding
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.presentation.fragments.allchats.users.HistoryAdapter
import com.example.alfamessanger.presentation.fragments.allchats.users.UsersSearchAdapter
import com.example.alfamessanger.presentation.fragments.allchats.users.UsersSearchViewModel
import com.example.alfamessanger.utills.*
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*

@AndroidEntryPoint
class ChannelsSearchFragment : Fragment(), ChannelsSearchAdapter.OnItemClickListener {

    private val mViewModel: ChannelsSearchViewModel by viewModels()
    private lateinit var mAdapter: ChannelsSearchAdapter
    private lateinit var mRecyclerView: RecyclerView

    private var binding: FragmentChannelsSearchBinding? = null
    private val mBinding get() = binding!!

    private var flag = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChannelsSearchBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initFields()
        mViewModel.getAllChannels()
    }

    private fun initFields(){
        mRecyclerView = mBinding.recyclerChannelsSearch
        val layout = LinearLayoutManager(APP_ACTIVITY_MAIN)
        mRecyclerView.layoutManager = layout
        mRecyclerView.hideShowScrollListener()
        mAdapter = ChannelsSearchAdapter(this)
        mRecyclerView.adapter = mAdapter
        APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
        APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE

        initClearTextButton()

        APP_ACTIVITY_MAIN.toolbar.clear_text.setOnClickListener {
            APP_ACTIVITY_MAIN.search_toolbar.text.clear()
        }
        if (APP_ACTIVITY_MAIN.toolbar.search_toolbar.text.isEmpty()) {
            APP_ACTIVITY_MAIN.toolbar.clear_text.visibility = View.INVISIBLE
        } else {
            APP_ACTIVITY_MAIN.toolbar.clear_text.visibility = View.VISIBLE
            initSearch()
        }
        if(mRecyclerView.adapter?.itemCount!! <= 5){
            APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
        }
    }

    private fun initClearTextButton(){

        val animZoom = AnimationUtils.loadAnimation(APP_ACTIVITY_MAIN, R.anim.send_message_btn_anim)
        APP_ACTIVITY_MAIN.toolbar.search_toolbar.addTextChangedListener {
            if (it?.isEmpty() == true) {
                flag = false
                APP_ACTIVITY_MAIN.toolbar.clear_text.visibility = View.INVISIBLE
                mAdapter.clearList()
            } else {
                if (!flag) {
                    flag = true
                    if (APP_ACTIVITY_MAIN.toolbar.clear_text.visibility == View.INVISIBLE) {
                        APP_ACTIVITY_MAIN.toolbar.clear_text.startAnimation(animZoom)
                    }
                    APP_ACTIVITY_MAIN.toolbar.clear_text.visibility = View.VISIBLE
                    initSearch()
                }
            }

        }
    }
    private fun initSearch() {
        mViewModel.searchChannel(APP_ACTIVITY_MAIN.toolbar.search_toolbar.text.toString()) { list ->
            mAdapter.setList(list)
            if (list.size > 5 && APP_ACTIVITY_MAIN.toolbar.search_toolbar.text.toString() != "") {
                APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags =
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
            } else {
                APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
            }
//            if (APP_ACTIVITY_MAIN.toolbar.search_toolbar.text?.isEmpty() == true) {
//                mRecyclerView.adapter = mAdapterHistory
//                APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
//            }
        }
    }

    override fun onItemClickAdapter(model: ChannelModel) {
        val bundle = Bundle()
        bundle.putSerializable(KEY_CHANNEL, model)
        APP_ACTIVITY_MAIN.mNavController.navigate(
            R.id.action_allChatsFragment_to_channelFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}