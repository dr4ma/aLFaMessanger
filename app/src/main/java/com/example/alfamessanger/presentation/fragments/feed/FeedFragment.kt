package com.example.alfamessanger.presentation.fragments.feed

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.alfamessanger.R
import com.example.alfamessanger.app.App
import com.example.alfamessanger.databinding.FragmentFeedBinding
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.FeedNewsModel
import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.fragments.BaseFragment
import com.example.alfamessanger.presentation.fragments.subscribers.SubscribersAdapter
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.ToolbarSettings
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : BaseFragment() {

    private val mViewModel: FeedViewModel by viewModels()
    private var binding: FragmentFeedBinding? = null
    private val mBinding get() = binding!!
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var mRecyclerView: RecyclerView

    @Inject
    lateinit var mAdapter: FeedAdapter

    private lateinit var mObserverLoadFeedNews: Observer<MutableList<FeedNewsModel>>
    private lateinit var swipeLayout: SwipeRefreshLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        initFields()
    }

    private fun initFields() {
        toolbarTools(ToolbarSettings.NORMAL_SETTINGS, getString(R.string.news))
        APP_ACTIVITY_MAIN.paramsToolbar.scrollFlags = 0
        swipeLayout = mBinding.refreshFeed
        mRecyclerView = mBinding.recyclerChannelFeed
        layoutManager = LinearLayoutManager(APP_ACTIVITY_MAIN)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.isNestedScrollingEnabled = false
        setHasOptionsMenu(true)

        mAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        if(mRecyclerView.adapter == null){
            mRecyclerView.adapter = mAdapter
        }

        if(FEED_LIST.isEmpty()){
            mBinding.noNews.visibility = View.VISIBLE
        }
        else{
            mAdapter.setList(FEED_LIST.asReversed())
        }

        swipeLayout.setOnRefreshListener {
            mViewModel.getFeedNews {
                mAdapter.setList(it.asReversed())
                FEED_LIST = it
                swipeLayout.isRefreshing = false
                mBinding.noNews.visibility = View.GONE
                swipeLayout.isRefreshing = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        mViewModel.getForNewNotifications {
            if(it){
                activity?.menuInflater?.inflate(R.menu.feed_menu, menu)
            }
            else{
                activity?.menuInflater?.inflate(R.menu.feed_menu_unwatched, menu)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.open_notifications -> {
                mViewModel.watchNotifications()
                APP_ACTIVITY_MAIN.mNavController.navigate(R.id.action_feedFragment_to_notificationFragment)
            }
        }
        return true
    }

    companion object {
        fun openFullScreenImage(url: String) {
            val bundle = Bundle()
            val model = SavedPhotoModel(image_url = url)
            val list = mutableListOf(model)
            bundle.putParcelableArrayList(KEY_CHAT_IMAGE, list as ArrayList<SavedPhotoModel>)
            bundle.putInt(KEY_POSITION, 0)
            APP_ACTIVITY_MAIN.mNavController.navigate(
                R.id.action_feedFragment_to_imageFullScreenFragment,
                bundle
            )
        }

        fun openChannel(model: ChannelModel) {
            val bundle = Bundle()
            bundle.putSerializable(KEY_CHANNEL, model)
            APP_ACTIVITY_MAIN.mNavController.navigate(
                R.id.action_feedFragment_to_channelFragment,
                bundle
            )
        }
    }
}