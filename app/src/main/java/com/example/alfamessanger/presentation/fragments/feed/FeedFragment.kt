package com.example.alfamessanger.presentation.fragments.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.FragmentFeedBinding
import com.example.alfamessanger.presentation.fragments.BaseFragment
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.ToolbarSettings
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

const val TOPIC = "/topics/myTopic"

@AndroidEntryPoint
class FeedFragment : BaseFragment() {

    private var binding: FragmentFeedBinding? = null
    private val mBinding get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        toolbarTools(ToolbarSettings.NORMAL_SETTINGS, getString(R.string.news))
        mBinding.noNews.visibility = View.VISIBLE
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            copyTextClipBoard(it)
        }
    }
}