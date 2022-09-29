package com.example.alfamessanger.presentation.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.alfamessanger.R
import com.example.alfamessanger.databinding.FragmentLogInBinding
import com.example.alfamessanger.utills.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private var binding: FragmentLogInBinding? = null
    private val mBinding get() = binding!!
    private val mViewModel: LogInFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        mBinding.apply {
            confirmPhoneNumber.setOnClickListener {
                sendCode(enterPhone.text.toString().trim())
            }
        }
    }

    private fun sendCode(phone: String) {
        if(mBinding.enterPhone.text.isEmpty()){
            showToast(getString(R.string.not_correct_phone))
        }
        else{
            mViewModel.authUser(phone)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}