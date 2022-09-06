package com.example.alfamessanger.presentation.fragments.mychats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.FragmentMyChatsBinding
import com.example.alfamessanger.domain.models.ChatModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.fragments.BaseFragment
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.ToolbarSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyChatsFragment : BaseFragment(), MyChatsAdapter.OnItemClickListener {

    private var binding: FragmentMyChatsBinding? = null
    private val mBinding get() = binding!!
    private val mViewModel: MyChatsFragmentViewModel by viewModels()

    private lateinit var mAdapter: MyChatsAdapter
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mObserverChats: Observer<MutableList<ChatModel>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyChatsBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        initFields()
        initObservers()
    }

    private fun initFields() {
        APP_ACTIVITY_MAIN.window.statusBarColor =
            ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.primaryColor)
        toolbarTools(ToolbarSettings.NORMAL_SETTINGS, getString(R.string.my_chats))
        APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
        mRecyclerView = mBinding.recyclerChats
        mAdapter = MyChatsAdapter(this)
        val layoutManager = LinearLayoutManager(APP_ACTIVITY_MAIN)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.adapter = mAdapter
        mRecyclerView.visibility = View.INVISIBLE
        mBinding.noChats.visibility = View.GONE
        mBinding.chatsProgress.visibility = View.VISIBLE
        mViewModel.getUserModel()
    }

    private fun initObservers(){
        mObserverChats = Observer {
            mRecyclerView.visibility = View.VISIBLE
            mBinding.chatsProgress.visibility = View.GONE
            mBinding.noChats.visibility = View.GONE
            mAdapter.setList(it)
        }
        mViewModel.chat.observe(APP_ACTIVITY_MAIN, mObserverChats)
        mViewModel.getAllChats(){
            mBinding.chatsProgress.visibility = View.GONE
            mBinding.noChats.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.chat.removeObserver(mObserverChats)
        mViewModel.removeObservers()
    }

    override fun onItemClickAdapter(model: ChatModel) {
        val userModel = UserModel(uid = model.id, iconUrl = model.iconUrl, name = model.name)
        val bundle = Bundle()
        bundle.putSerializable(KEY_USER_CHAT, userModel)
        APP_ACTIVITY_MAIN.mNavController.navigate(
            R.id.action_myChatsFragment_to_singleChatFragment,
            bundle
        )
    }
}