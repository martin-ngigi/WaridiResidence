package com.example.waridiresidence.presentation.ui.agent.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.waridiresidence.R
import com.example.waridiresidence.data.model.modelrequest.UserProfileRequest
import com.example.waridiresidence.databinding.FragmentWelcomeBinding

import com.example.waridiresidence.presentation.ui.agent.activities.HomeAgentActivity
import com.example.waridiresidence.presentation.ui.agent.viewmodels.ProfileAgentViewModel
import com.example.waridiresidence.util.Resource
import com.example.waridiresidence.util.hideKeyboard
import com.example.waridiresidence.util.Constants
import com.example.waridiresidence.util.Utils.validateProfileRequest
import com.example.waridiresidence.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class WelcomeFragment: Fragment(R.layout.fragment_welcome) {
    val viewModel: ProfileAgentViewModel by viewModels()
    private val TAG = "WelcomeFragment"

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * init binding
         */
        binding = FragmentWelcomeBinding.bind(view)

        doInitializations(view)
    }

    private fun doInitializations(view: View){
        //getTextWatcherLogin()
        binding.proceedToHomeBtn.setOnClickListener{
            hideKeyboard()
            /**
             * invoke data validation method
             */
            val result = validateProfileRequest(firstname = Constants.fname, lastname = Constants.lname, phone = Constants.phone)
            if (result.successful){
                updateUserHouseObjectToTrue()
            }
            else{
                toast(result.error)
            }


        }
    }

    private fun updateUserHouseObjectToTrue() {

        val userRequest = UserProfileRequest(
            firstName=Constants.fname,
            lastName = Constants.lname,
            phone = Constants.phone,
            profileImage =  Constants.profile_image,
            hasHouses = true
        )


        viewModel.updateUser(userRequest)
        viewModel.updateUserData.observe(viewLifecycleOwner, Observer { event->
            event.getContentIfNotHandled()?.let { response ->
                when(response){
                    is Resource.Success -> {
                        hideProgressBar()
                        response.message?.let {
                            toast(it)
                            //also update constants as well
                            //Constants.hasHouses=true
                            //intent to start activity
                            Log.i(TAG, "updateUserHouseObjectToTrue: SUCCESS UPDATING HOUSE")
                            Intent(requireContext(), HomeAgentActivity::class.java).also {
                                requireContext().startActivity(it)
                            }

                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let {
                            toast(it)
                            Log.e(TAG, "updateUserHouseObjectToTrue: Error ${it}", )
                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }
        })
    }

    private fun showProgressBar() {
        login_progress.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        login_progress.visibility = View.GONE
    }


}