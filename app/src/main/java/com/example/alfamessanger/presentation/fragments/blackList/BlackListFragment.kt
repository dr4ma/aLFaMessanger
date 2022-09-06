package com.example.alfamessanger.presentation.fragments.blackList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.BlackListFragmentBinding
import com.example.alfamessanger.domain.models.BlackListUserModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.fragments.saved.SavedAdapter
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.ToolbarSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlackListFragment : Fragment() {

    private var binding: BlackListFragmentBinding? = null
    private val mBinding get() = binding!!

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: BlackListAdapter

    private lateinit var mObserverBlack: Observer<MutableList<BlackListUserModel>>

    private val mViewModel: BlackListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BlackListFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initFields()
    }

    private fun initFields() {
        toolbarTools(ToolbarSettings.PROFILE_SETTINGS, getString(R.string.black_list))
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowHomeEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setHomeButtonEnabled(true)
        APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
        mRecyclerView = mBinding.blackListRecycler
        mRecyclerView.itemAnimator = null
        mAdapter = BlackListAdapter { model ->
            mViewModel.removeFromBlackList(model){
                mAdapter.removeFromList(model)
                if (mAdapter.itemCount == 0){
                    mBinding.blockProgress.visibility = View.GONE
                    mBinding.noBlock.visibility = View.VISIBLE
                }
            }
        }
        val layoutManager = LinearLayoutManager(APP_ACTIVITY_MAIN)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.adapter = mAdapter
        mRecyclerView.visibility = View.INVISIBLE
        mBinding.noBlock.visibility = View.GONE
        mBinding.blockProgress.visibility = View.VISIBLE

        mObserverBlack = Observer {
            mRecyclerView.visibility = View.VISIBLE
            mBinding.blockProgress.visibility = View.GONE
            mBinding.noBlock.visibility = View.GONE
            mAdapter.setList(it)

        }
        mViewModel.blackList.observe(APP_ACTIVITY_MAIN, mObserverBlack)
        mViewModel.getBlackList(){
            mBinding.blockProgress.visibility = View.GONE
            mBinding.noBlock.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.blackList.removeObserver(mObserverBlack)
        mViewModel.removeObserver()
        binding = null
    }

    companion object {
        fun goToChat(model : BlackListUserModel){
            val userModel = UserModel(
                uid = model.uid,
                phone = model.phone,
                name = model.name,
                nickname = model.nickname,
                bio = model.bio,
                status = model.status,
                iconUrl = model.iconUrl
            )
            val bundle = Bundle()
            bundle.putSerializable(KEY_USER_CHAT, userModel)
            APP_ACTIVITY_MAIN.mNavController.navigate(
                R.id.action_blackListFragment_to_singleChatFragment,
                bundle
            )
        }
        fun goToProfile(model : BlackListUserModel){
            val userModel = UserModel(
                uid = model.uid,
                phone = model.phone,
                name = model.name,
                nickname = model.nickname,
                bio = model.bio,
                status = model.status,
                iconUrl = model.iconUrl
            )
            val bundle = Bundle()
            bundle.putSerializable(KEY_PROFILE, userModel)
            APP_ACTIVITY_MAIN.mNavController.navigate(
                R.id.action_blackListFragment_to_userProfileFragment,
                bundle
            )
        }
    }
}