package com.example.alfamessanger.presentation.fragments.createChannel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.CreateChannelFragmentBinding
import com.example.alfamessanger.domain.models.ChannelModel
import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.ToolbarSettings
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateChannelFragment : Fragment() {

    private val mViewModel: CreateChannelViewModel by viewModels()
    private var binding: CreateChannelFragmentBinding? = null
    private val mBinding get() = binding!!

    private var typeChannel: String = PUBLIC
    private lateinit var iconUrl: Uri
    private var iconUriDatabase: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateChannelFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        initFields()
    }

    private fun initFields() {
        toolbarTools(ToolbarSettings.PROFILE_SETTINGS, getString(R.string.creating_channel))
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowHomeEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setHomeButtonEnabled(true)
        setHasOptionsMenu(true)
        mBinding.apply {
            publicRadioChannel.setOnClickListener {
                privateRadioChannel.isChecked = false
                closeRadioChannel.isChecked = false
                typeChannel = PUBLIC
            }
            privateRadioChannel.setOnClickListener {
                publicRadioChannel.isChecked = false
                closeRadioChannel.isChecked = false
                typeChannel = PRIVATE
            }
            closeRadioChannel.setOnClickListener {
                privateRadioChannel.isChecked = false
                publicRadioChannel.isChecked = false
                typeChannel = CLOSE
            }
            publicRadioLabel.setOnClickListener {
                publicRadioChannel.isChecked = true
                privateRadioChannel.isChecked = false
                closeRadioChannel.isChecked = false
                typeChannel = PUBLIC
            }
            privateRadioLabel.setOnClickListener {
                publicRadioChannel.isChecked = false
                privateRadioChannel.isChecked = true
                closeRadioChannel.isChecked = false
                typeChannel = PRIVATE
            }
            closeRadioLabel.setOnClickListener {
                publicRadioChannel.isChecked = false
                privateRadioChannel.isChecked = false
                closeRadioChannel.isChecked = true
                typeChannel = CLOSE
            }
            mBinding.channelIcon.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_GALLERY)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.create_channel_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.create_channel_menu -> {
                if (mBinding.channelName.text.isEmpty() || mBinding.channelName.text.equals(" ")) {
                    showSnackBar(
                        mBinding.boxDescriptionChannel,
                        getString(R.string.enter_correct_channel_name),
                        R.color.colorError
                    )
                } else {
                    mBinding.progressbar.visibility = View.VISIBLE
                    mViewModel.createChannel(
                        USER,
                        typeChannel,
                        mBinding.channelName.text.toString(),
                        mBinding.channelDesc.text.toString(),
                        iconUriDatabase
                    ) { model ->
                        loadChannelIcon(model)
                        mBinding.progressbar.visibility = View.GONE
                        hideKeyBoard()
                        iconUriDatabase = ""
                        val bundle = Bundle()
                        bundle.putSerializable(KEY_CHANNEL, model)
                        APP_ACTIVITY_MAIN.mNavController.navigate(
                            R.id.action_createChannelFragment_to_channelFragment,
                            bundle
                        )
                    }
                }
            }
            android.R.id.home -> {
                APP_ACTIVITY_MAIN.onBackPressed()
            }
        }
        return true
    }

    private fun loadChannelIcon(model: ChannelModel) {
        if(iconUriDatabase != ""){
            val fileName: String = UUID.randomUUID().toString()
            val path = STORAGE_REFERENCE.child(FOLDER_CHANNELS_ICON).child(fileName)
            mViewModel.uploadChannelIcon(model, iconUrl, path)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            iconUrl = data.data!!
            mBinding.channelIcon.downloadAndSetImage(true, iconUrl.toString())
            iconUriDatabase = iconUrl.toString()
        }
    }

}