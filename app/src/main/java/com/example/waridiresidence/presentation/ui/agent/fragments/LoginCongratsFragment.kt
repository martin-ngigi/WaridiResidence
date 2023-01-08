package com.example.waridiresidence.presentation.ui.agent.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.waridiresidence.R
import com.example.waridiresidence.data.model.modelrequest.LoginRequest
import com.example.waridiresidence.data.model.modelrequest.house.UserHouseRequest
import com.example.waridiresidence.databinding.FragmentLoginCongratsAgentBinding

import com.example.waridiresidence.presentation.ui.agent.activities.HomeAgentActivity
import com.example.waridiresidence.presentation.ui.agent.viewmodels.LoginCongratsViewModel
import com.example.waridiresidence.presentation.ui.client.activities.HomeClientActivity
import com.example.waridiresidence.util.Resource
import com.example.waridiresidence.util.hideKeyboard
import com.example.waridiresidence.presentation.viewmodel.LoginViewModel
import com.example.waridiresidence.util.Constants
import com.example.waridiresidence.util.Utils.toast
import com.example.waridiresidence.util.Utils.validateLoginRequest
import com.example.waridiresidence.util.Utils.validateUserHouseRequest
import com.example.waridiresidence.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginCongratsFragment: Fragment(R.layout.fragment_login_congrats_agent) {
     val viewModel: LoginCongratsViewModel by viewModels()
    private val TAG = "LoginCongratsFragment"

    private lateinit var binding: FragmentLoginCongratsAgentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * init binding
         */
        binding = FragmentLoginCongratsAgentBinding.bind(view)

        doInitializations(view)
    }

    private fun doInitializations(view: View){
        //getTextWatcherLogin()
        binding.proceedToHomeBtn.setOnClickListener{
            hideKeyboard()
            /**
             * invoke data validation method
             */
            val result = validateUserHouseRequest(Constants.id, Constants.fname, Constants.phone)

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
        val userHouseRequest= UserHouseRequest(Constants.id, Constants.fname, Constants.phone)
        ///--------------

        viewModel.registerUserHouse22(userHouseRequest)
        viewModel.userHouse22Data.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {  response ->
                when(response){
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { loginResponse ->
                            Log.i(TAG, "getLogin: SUCCESS REGISTERING HOUSES")
                            //intent to start activity
                            checkIfHousesAlreadyExists()
                            //findNavController().navigate(R.id.action_loginCongratsAgentFragment_to_welcome223)

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

    private fun checkIfHousesAlreadyExists() {
        /**
         * If user doesnt have UserHouse Object, then create
         */
        if (Constants.hasHouses == false){ //false ...navigate to LoginCongrates Fragment so as to register User House Object
            findNavController().navigate(R.id.action_loginCongratsAgentFragment_to_welcome223)
        }
        else{
            //navigate to home
            Intent(requireContext(), HomeAgentActivity::class.java).also {
                requireContext().startActivity(it)
            }
        }
    }

    private fun showProgressBar() {
        login_progress.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        login_progress.visibility = View.GONE
    }





}