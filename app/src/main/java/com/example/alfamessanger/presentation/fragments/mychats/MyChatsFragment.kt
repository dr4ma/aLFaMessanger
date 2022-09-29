package com.example.alfamessanger.presentation.fragments.mychats

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.FragmentMyChatsBinding
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.ChannelMyChatsModel
import com.example.alfamessanger.domain.models.ChatModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.fragments.BaseFragment
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.ToolbarSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class MyChatsFragment : BaseFragment(), MyChatsAdapter.OnItemClickListener {

    private var binding: FragmentMyChatsBinding? = null
    private val mBinding get() = binding!!
    private val mViewModel: MyChatsFragmentViewModel by viewModels()

    private lateinit var mAdapter: MyChatsAdapter
    @Inject
    lateinit var mAdapterChannels: MyChatsChannelsAdapter
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var rotateOpen : Animation
    private lateinit var rotateClose : Animation
    private lateinit var fromBottom : Animation
    private lateinit var toBottom : Animation

    private var listChannelResult = mutableListOf<ChannelMyChatsModel>()
    private var listChatResult = mutableListOf<ChatModel>()

    private lateinit var mObserverChats: Observer<MutableList<ChatModel>>
    private lateinit var mObserverChannels: Observer<MutableList<ChannelMyChatsModel>>
    private var clickedAdd = false

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
        initAnimation()
    }

    private fun initFields() {
        APP_ACTIVITY_MAIN.window.statusBarColor =
            ContextCompat.getColor(APP_ACTIVITY_MAIN, R.color.primaryColor)
        toolbarTools(ToolbarSettings.NORMAL_SETTINGS, getString(R.string.my_chats))
        APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
        listChannelResult = CHANNELS_LIST
        listChatResult = CHAT_LIST
        setHasOptionsMenu(true)
        mRecyclerView = mBinding.recyclerChats
        mAdapter = MyChatsAdapter(this)
        val layoutManager = LinearLayoutManager(APP_ACTIVITY_MAIN)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.isNestedScrollingEnabled = false
        mAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        mViewModel.getUserModel()

        mBinding.mainFloatingBtn.setOnClickListener {
            onAddButtonClicked()
        }
        mBinding.createChannel.setOnClickListener {
            APP_ACTIVITY_MAIN.mNavController.navigate(R.id.action_myChatsFragment_to_createChannelFragment)
        }
        if(AppPreferences.getAdapterMemory() == ADAPTER_CHANNELS){
            if(listChannelResult.isEmpty()){
                mBinding.noChannels.visibility = View.VISIBLE
                mBinding.noChats.visibility = View.GONE
            }
            else{
                mBinding.noChannels.visibility = View.GONE
                mRecyclerView.adapter = mAdapterChannels
                mAdapterChannels.setList(listChannelResult)
                APP_ACTIVITY_MAIN.toolbar.label_toolbar.text = getString(R.string.my_channels_my_chats)
            }
        }
        else{
            if(listChatResult.isEmpty()){
                mBinding.noChats.visibility = View.VISIBLE
                mBinding.noChannels.visibility = View.GONE
            }
            else{
                mBinding.noChats.visibility = View.GONE
                mRecyclerView.adapter = mAdapter
                mAdapter.setList(listChatResult)
                APP_ACTIVITY_MAIN.toolbar.label_toolbar.text = getString(R.string.my_chats)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        clickedAdd = true
        setVisibility(clickedAdd)
        setAnimation(clickedAdd)
        setClicked(clickedAdd)
        clickedAdd = false
    }

    private fun initObservers(){
        mObserverChats = Observer {
            if(it.isNotEmpty()){
                mAdapter.setList(it)
                listChatResult = it
                mBinding.noChats.visibility = View.GONE
            }
        }
        mViewModel.chat.observe(APP_ACTIVITY_MAIN, mObserverChats)
        mViewModel.getAllChats {
        }

        mObserverChannels = Observer {
            if(it.isNotEmpty()){
                mAdapterChannels.setList(it)
                listChannelResult = it
                mBinding.noChannels.visibility = View.GONE
            }
        }
        mViewModel.channels.observe(APP_ACTIVITY_MAIN, mObserverChannels)
        mViewModel.getChannelsSubscribed()
    }

    private fun initAnimation(){
        rotateOpen = AnimationUtils.loadAnimation(APP_ACTIVITY_MAIN, R.anim.rotate_open_anim)
        rotateClose = AnimationUtils.loadAnimation(APP_ACTIVITY_MAIN, R.anim.rotate_close_anim)
        fromBottom = AnimationUtils.loadAnimation(APP_ACTIVITY_MAIN, R.anim.from_bottom_anim)
        toBottom = AnimationUtils.loadAnimation(APP_ACTIVITY_MAIN, R.anim.to_bottom_anim)
    }

    private fun onAddButtonClicked(){
        setVisibility(clickedAdd)
        setAnimation(clickedAdd)
        setClicked(clickedAdd)
        clickedAdd = !clickedAdd
    }

    private fun setAnimation(clicked : Boolean) {
        if (!clicked){
            mBinding.mainFloatingBtn.startAnimation(rotateOpen)
            mBinding.createChat.startAnimation(fromBottom)
            mBinding.createChannel.startAnimation(fromBottom)
        }
        else{
            mBinding.mainFloatingBtn.startAnimation(rotateClose)
            mBinding.createChat.startAnimation(toBottom)
            mBinding.createChannel.startAnimation(toBottom)
        }
    }

    private fun setVisibility(clicked : Boolean) {
        if(!clicked){
            mBinding.createChat.visibility = View.VISIBLE
            mBinding.createChannel.visibility = View.VISIBLE
        }
        else{
            mBinding.createChat.visibility = View.INVISIBLE
            mBinding.createChannel.visibility = View.INVISIBLE
        }
    }

    private fun setClicked(clicked: Boolean){
        if(!clicked){
            mBinding.createChat.isClickable = true
            mBinding.createChannel.isClickable = true
        }
        else{
            mBinding.createChat.isClickable = false
            mBinding.createChannel.isClickable = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
            mViewModel.chat.removeObserver(mObserverChats)
            mViewModel.channels.removeObserver(mObserverChannels)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.swap_my_chats_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_adapter -> {
                if(AppPreferences.getAdapterMemory() == ADAPTER_CHANNELS){
                    APP_ACTIVITY_MAIN.toolbar.label_toolbar.text = getString(R.string.my_chats)
                    if(listChatResult.isEmpty()){
                        mBinding.noChats.visibility = View.VISIBLE
                        mBinding.noChannels.visibility = View.GONE
                        AppPreferences.setAdapterMemory(ADAPTER_CHATS)
                    }
                    else{
                        mBinding.noChats.visibility = View.GONE
                        mRecyclerView.adapter = mAdapter
                        mAdapter.setList(listChatResult)
                        AppPreferences.setAdapterMemory(ADAPTER_CHATS)
                    }
                }
                else{
                    APP_ACTIVITY_MAIN.toolbar.label_toolbar.text = getString(R.string.my_channels_my_chats)
                    if(listChannelResult.isEmpty()){
                        mBinding.noChannels.visibility = View.VISIBLE
                        mBinding.noChats.visibility = View.GONE
                        AppPreferences.setAdapterMemory(ADAPTER_CHANNELS)
                    }
                    else{
                        mBinding.noChannels.visibility = View.GONE
                        mRecyclerView.adapter = mAdapterChannels
                        mAdapterChannels.setList(listChannelResult)
                        AppPreferences.setAdapterMemory(ADAPTER_CHANNELS)
                    }
                }
            }
        }

        return true
    }

    companion object{
        fun openChannel(model : ChannelModel){
            val bundle = Bundle()
            bundle.putSerializable(KEY_CHANNEL, model)
            APP_ACTIVITY_MAIN.mNavController.navigate(
                R.id.action_myChatsFragment_to_channelFragment,
                bundle
            )
        }
    }
}