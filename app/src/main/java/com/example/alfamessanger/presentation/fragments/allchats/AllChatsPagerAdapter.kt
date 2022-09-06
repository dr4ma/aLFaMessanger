package com.example.alfamessanger.presentation.fragments.allchats

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.alfamessanger.R
import com.example.alfamessanger.presentation.fragments.allchats.channels.ChannelsSearchFragment
import com.example.alfamessanger.presentation.fragments.allchats.chats.ChatsSearchFragment
import com.example.alfamessanger.presentation.fragments.allchats.users.UsersSearchFragment
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN

class AllChatsPagerAdapter(fm : FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle){

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
       return when (position){
            0 -> UsersSearchFragment()
            1 -> ChannelsSearchFragment()
            2 -> ChatsSearchFragment()
           else -> Fragment()
       }
    }
}