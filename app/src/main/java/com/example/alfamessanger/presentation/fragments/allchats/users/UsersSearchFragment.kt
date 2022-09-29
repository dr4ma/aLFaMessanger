package com.example.alfamessanger.presentation.fragments.allchats.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.FragmentUsersSearchBinding
import com.example.alfamessanger.domain.models.SearchHistoryUserModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.utills.*
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_users_search.view.*

@AndroidEntryPoint
class UsersSearchFragment : Fragment(), UsersSearchAdapter.OnItemClickListener {

    private var binding: FragmentUsersSearchBinding? = null
    private val mBinding get() = binding!!

    private val mViewModel: UsersSearchViewModel by viewModels()
    private lateinit var mAdapter: UsersSearchAdapter
    private lateinit var mAdapterHistory: HistoryAdapter
    private lateinit var mRecyclerView: RecyclerView

    private var flag = false

    private lateinit var mObserverHistoryUsers: Observer<MutableList<SearchHistoryUserModel>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersSearchBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        initFields()
        initObservers()

        mViewModel.getHistoryUser()
        mViewModel.getAllUsers()
    }

    private fun initFields() {
        mRecyclerView = mBinding.recyclerUsersSearch
        val layout = LinearLayoutManager(APP_ACTIVITY_MAIN)
        mRecyclerView.layoutManager = layout
        mRecyclerView.hideShowScrollListener()
        mAdapter = UsersSearchAdapter(this)
        mAdapterHistory = HistoryAdapter()
        APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
        APP_ACTIVITY_MAIN.tintApp.visibility = View.GONE

        initClearTextButton()

        APP_ACTIVITY_MAIN.toolbar.clear_text.setOnClickListener {
            APP_ACTIVITY_MAIN.search_toolbar.text.clear()
        }

        if (APP_ACTIVITY_MAIN.toolbar.search_toolbar.text.isEmpty()) {
            mBinding.recentSearchLabel.visibility = View.VISIBLE
            mRecyclerView.adapter = mAdapterHistory
            APP_ACTIVITY_MAIN.toolbar.clear_text.visibility = View.INVISIBLE
        } else {
            mBinding.recentSearchLabel.visibility = View.INVISIBLE
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
                mBinding.recentSearchLabel.visibility = View.VISIBLE
                APP_ACTIVITY_MAIN.toolbar.clear_text.visibility = View.INVISIBLE
            } else {
                if (!flag) {
                    flag = true
                    mBinding.recentSearchLabel.visibility = View.INVISIBLE
                    if (APP_ACTIVITY_MAIN.toolbar.clear_text.visibility == View.INVISIBLE) {
                        APP_ACTIVITY_MAIN.toolbar.clear_text.startAnimation(animZoom)
                    }
                    APP_ACTIVITY_MAIN.toolbar.clear_text.visibility = View.VISIBLE
                }
            }
            initSearch()
        }
    }

    override fun onItemClickAdapter(model: UserModel) {
        mViewModel.insertDataInHistory(model)
        openChat(model)
    }

    private fun initObservers() {
        mObserverHistoryUsers = Observer {
            mAdapterHistory.setList(it)
        }
        mViewModel.listHistory.observe(APP_ACTIVITY_MAIN, mObserverHistoryUsers)
    }

    private fun initSearch() {
        mViewModel.searchUser(APP_ACTIVITY_MAIN.toolbar.search_toolbar.text.toString()) { list ->
            mRecyclerView.adapter = mAdapter
            mAdapter.setList(list)
            if (list.size > 5 && APP_ACTIVITY_MAIN.toolbar.search_toolbar.text.toString() != "") {
                APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags =
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
            } else {
                APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
            }
            if (APP_ACTIVITY_MAIN.toolbar.search_toolbar.text?.isEmpty() == true) {
                mRecyclerView.adapter = mAdapterHistory
                APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
            }
        }
    }

    private fun openChat(model: UserModel) {
        val bundle = Bundle()
        bundle.putSerializable(KEY_USER_CHAT, model)
        APP_ACTIVITY_MAIN.mNavController.navigate(
            R.id.action_allChatsFragment_to_singleChatFragment,
            bundle
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.removeObservers()
    }

    companion object {
        fun openChat(model: SearchHistoryUserModel) {

            val newModel = UserModel(
                uid = model.uid,
                phone = model.phone,
                name = model.name,
                nickname = model.nickname,
                bio = model.bio,
                status = model.status,
                iconUrl = model.iconUrl
            )

            val bundle = Bundle()
            bundle.putSerializable(KEY_USER_CHAT, newModel)
            APP_ACTIVITY_MAIN.mNavController.navigate(
                R.id.action_allChatsFragment_to_singleChatFragment,
                bundle
            )
        }
    }

}