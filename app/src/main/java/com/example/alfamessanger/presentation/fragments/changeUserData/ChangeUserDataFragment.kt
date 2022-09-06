package com.example.alfamessanger.presentation.fragments.changeUserData

import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.alfamessanger.R
import com.example.alfamessanger.data.firebase.UserModelRequests
import com.example.alfamessanger.databinding.FragmentChangeUserDataBinding
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.domain.repository.GetUserModelRepository
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.ToolbarSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeUserDataFragment : Fragment() {

    private var binding: FragmentChangeUserDataBinding? = null
    private val mBinding get() = binding!!
    private val mViewModel: ChangeUserDataFragmentViewModel by viewModels()
    private var listAllUsers = mutableListOf<UserModel>()
    private val userRepository: GetUserModelRepository = UserModelRequests()

    private lateinit var mObserverLoadAllUsers: Observer<MutableList<UserModel>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeUserDataBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        toolbarTools(ToolbarSettings.CHANGE_DATA_SETTINGS, getString(R.string.general_settings))
        hideKeyBoard()
        initFields()
        userRepository.getAllUsers {
        }
    }

    private fun confirmChangeUserData() {

        mBinding.apply {
            if (nameTextChange.length() != 1 && bioTextChange.length() != 0 && fullNameTextChange.length() != 0) {
                if(mViewModel.checkNickName(nameTextChange.text.toString(), listAllUsers)){
                    mViewModel.saveNewUserData(
                        bioTextChange.text.toString(),
                        nameTextChange.text.toString(),
                        fullNameTextChange.text.toString()
                    ) {
                        mBinding.progressbar.visibility = View.INVISIBLE
                        APP_ACTIVITY_MAIN.mNavController.navigate(R.id.action_changeUserDataFragment_to_accountFragment)

                    }
                }
                else{
                    mBinding.progressbar.visibility = View.INVISIBLE
                    showSnackBar(mBinding.mainConst, getString(R.string.nick_is_set_alreday), R.color.colorRed)
                }
            } else {
                showToast(getString(R.string.data_not_correct))
            }
        }

    }

    private fun initFields() {
        setHasOptionsMenu(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY_MAIN.supportActionBar?.setDisplayShowHomeEnabled(true)
        mBinding.apply {
            progressbar.visibility = View.INVISIBLE
            nameTextChange.setText(USER.nickname)
            bioTextChange.setText(USER.bio)
            fullNameTextChange.setText(USER.name)

            nameTextChange.addTextChangedListener {
                if (nameTextChange.length() == 0) {
                    nameTextChange.setText("@")
                    nameTextChange.setSelection(1)
                }
            }
        }

        mObserverLoadAllUsers = Observer {
            listAllUsers = it
        }
        userRepository.listAllUsersResult.observe(APP_ACTIVITY_MAIN, mObserverLoadAllUsers)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.change_user_data_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.confirm_change -> {
                hideKeyBoard()
                mBinding.progressbar.visibility = View.VISIBLE
                confirmChangeUserData()
            }
            android.R.id.home -> {
                APP_ACTIVITY_MAIN.onBackPressed()
            }
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}