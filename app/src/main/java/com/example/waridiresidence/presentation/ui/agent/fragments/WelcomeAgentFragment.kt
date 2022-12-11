package com.example.waridiresidence.presentation.ui.agent.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.waridiresidence.R
import com.example.waridiresidence.data.model.modelrequest.UserAgentProfileRequest
import com.example.waridiresidence.databinding.FragmentWelcomeBinding

import com.example.waridiresidence.presentation.ui.agent.activities.HomeAgentActivity
import com.example.waridiresidence.presentation.ui.agent.viewmodels.ProfileAgentViewModel
import com.example.waridiresidence.util.*
import com.example.waridiresidence.util.Utils.toast
import com.example.waridiresidence.util.Utils.validateProfileRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class WelcomeAgentFragment: Fragment(R.layout.fragment_login_congrats_agent) {
     val viewModel: ProfileAgentViewModel by viewModels()
    private val TAG = "Welcome22"

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

            val result = validateProfileRequest(
                firstname = Constants.fname,
                lastname = Constants.lname,
                phone = Constants.phone
            )

            if (result.successful){
                getLogin()
            }
            else{
                //toast(requireContext(),"${result.error}")
                //or
                requireContext().toast("${result.error}")
            }


        }
    }

    private fun getLogin() {
        //Log.i(TAG, "getLogin: ")
        val userRequest = UserAgentProfileRequest(
            firstName=Constants.fname,
            lastName = Constants.lname,
            phone = Constants.phone,
            profileImage =  Constants.profile_image,
            hasHouses = true
        )
        ///--------------

        viewModel.updateUser(userRequest)
        viewModel.updateUserData.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {  response ->
                when(response){
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { loginResponse ->
                            Log.i(TAG, "getLogin: SUCCESS REGISTERING HOUSES")
                            //intent to start activity
                            Intent(requireContext(), HomeAgentActivity::class.java).also {
                                requireContext().startActivity(it)
                            }
                        }
                    }

                    is Resource.Error ->{
                        hideProgressBar()
                        response.message?.let { requireContext().toast(it) }
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