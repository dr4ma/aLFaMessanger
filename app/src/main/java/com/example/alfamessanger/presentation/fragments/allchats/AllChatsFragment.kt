package com.example.alfamessanger.presentation.fragments.allchats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.FragmentAllChatsBinding
import com.example.alfamessanger.presentation.fragments.BaseFragment
import com.example.alfamessanger.utills.enums.ToolbarSettings
import com.example.alfamessanger.utills.toolbarTools
import com.google.android.material.tabs.TabLayoutMediator


class AllChatsFragment : BaseFragment() {

    private var binding: FragmentAllChatsBinding? = null
    private val mBinding get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllChatsBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        toolbarTools(ToolbarSettings.SEARCH_SETTINGS, null)
        initTabLayout()
    }

    private fun initTabLayout(){
        mBinding.apply {
            val adapter = AllChatsPagerAdapter(childFragmentManager, lifecycle)
            viewPagerAllChats.adapter = adapter
            TabLayoutMediator(tabAllChats, viewPagerAllChats){tab, position ->
                when(position){
                    0 -> tab.text = getString(R.string.users)
                    1 -> tab.text = getString(R.string.channels)
                    2 -> tab.text = getString(R.string.chats)
                }
            }.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}