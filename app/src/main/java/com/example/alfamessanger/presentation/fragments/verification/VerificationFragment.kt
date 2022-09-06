package com.example.alfamessanger.presentation.fragments.verification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.alfamessanger.databinding.FragmentVerificationBinding
import com.example.alfamessanger.presentation.activities.MainActivity
import com.example.alfamessanger.utills.APP_ACTIVITY_REGISTER
import com.example.alfamessanger.utills.ID_AUTH_VERIFY
import com.example.alfamessanger.utills.PHONE_AUTH
import com.example.alfamessanger.utills.replaceActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerificationFragment : Fragment() {

    private var binding: FragmentVerificationBinding? = null
    private val mBinding get() = binding!!
    private var phone = ""
    private var idVerify = ""
    private val mViewModel: VerificationFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerificationBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        getArgumentsVerify()
        sendCode()
    }

    private fun getArgumentsVerify(){
        phone = arguments?.getString(PHONE_AUTH)!!
        idVerify = arguments?.getString(ID_AUTH_VERIFY)!!
    }

    private fun sendCode(){
        mBinding.codeVerify.addTextChangedListener {
            if(mBinding.codeVerify.length() == 6){
                mViewModel.enterCode(idVerify, phone, mBinding.codeVerify.text.toString()){
                    APP_ACTIVITY_REGISTER.replaceActivity(APP_ACTIVITY_REGISTER, MainActivity())
                }
            }
        }
    }

}