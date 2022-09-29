package com.example.alfamessanger.presentation.fragments.friendsList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.FragmentAccountBinding
import com.example.alfamessanger.databinding.FragmentFriendsListBinding
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.fragments.allchats.users.UsersSearchAdapter
import com.example.alfamessanger.presentation.fragments.allchats.users.UsersSearchViewModel
import com.example.alfamessanger.presentation.fragments.mychats.MyChatsAdapter
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.ToolbarSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsListFragment : Fragment(), FriendsListAdapter.OnItemClickListener {

    private var binding: FragmentFriendsListBinding? = null
    private val mBinding get() = binding!!
    private val mViewModel: FriendsListViewModel by viewModels()
    private lateinit var mRecyclerView : RecyclerView

    private lateinit var mAdapter : FriendsListAdapter
    private lateinit var mObserverFriends: Observer<MutableList<UserModel>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendsListBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        initFields()
        initObserver()
    }

    private fun initFields(){
        toolbarTools(ToolbarSettings.PROFILE_SETTINGS, getString(R.string.my_friends))
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowHomeEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setHomeButtonEnabled(true)
        APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
        mRecyclerView = mBinding.recyclerFriendList
        mAdapter = FriendsListAdapter(this)
        val layoutManager = LinearLayoutManager(APP_ACTIVITY_MAIN)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.adapter = mAdapter
        mRecyclerView.visibility = View.INVISIBLE
        mBinding.noFriends.visibility = View.GONE
        mBinding.friendsProgress.visibility = View.VISIBLE
    }

    private fun initObserver(){
        mObserverFriends = Observer {
            mRecyclerView.visibility = View.VISIBLE
            mBinding.friendsProgress.visibility = View.GONE
            mBinding.noFriends.visibility = View.GONE
            mAdapter.setList(it)
        }
        mViewModel.friend.observe(APP_ACTIVITY_MAIN, mObserverFriends)
        mViewModel.getFriends(){
            mBinding.friendsProgress.visibility = View.GONE
            mBinding.noFriends.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        mViewModel.friend.removeObserver(mObserverFriends)
        mViewModel.removeObservers()
    }

    override fun onItemClickAdapter(model: UserModel) {
        val bundle = Bundle()
        bundle.putSerializable(KEY_PROFILE, model)
        APP_ACTIVITY_MAIN.mNavController.navigate(
            R.id.action_friendsListFragment_to_userProfileFragment,
            bundle
        )
    }

    companion object{
        fun goToChat(model : UserModel){
            val bundle = Bundle()
            bundle.putSerializable(KEY_USER_CHAT, model)
            APP_ACTIVITY_MAIN.mNavController.navigate(
                R.id.action_friendsListFragment_to_singleChatFragment,
                bundle
            )
        }
    }
}