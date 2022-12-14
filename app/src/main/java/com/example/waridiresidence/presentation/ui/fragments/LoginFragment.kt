package com.example.waridiresidence.presentation.ui.fragments

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
import com.example.waridiresidence.databinding.FragmentLoginBinding
import com.example.waridiresidence.presentation.ui.agent.activities.HomeAgentActivity
import com.example.waridiresidence.presentation.ui.client.activities.HomeClientActivity
import com.example.waridiresidence.util.Resource
import com.example.waridiresidence.util.hideKeyboard
import com.example.waridiresidence.presentation.viewmodel.LoginViewModel
import com.example.waridiresidence.util.Constants
import com.example.waridiresidence.util.Utils.toast
import com.example.waridiresidence.util.Utils.validateLoginRequest
import com.example.waridiresidence.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {
     val viewModel: LoginViewModel by viewModels()
    lateinit var stringEmail: String
    lateinit var stringPassword: String
    lateinit var deviceId: String
    private val TAG = "LoginPage"

    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * init binding
         */
        binding = FragmentLoginBinding.bind(view)

        deviceId = Settings.Secure.getString(
            requireContext().contentResolver, Settings.Secure.ANDROID_ID
        )

        doInitializations(view)
    }

    private fun doInitializations(view: View){
        //getTextWatcherLogin()
        binding.loginButton.setOnClickListener{
            hideKeyboard()
            //stringEmail = login_username.text.toString()
            stringEmail = binding.loginUsername.text.toString()
            //stringPassword = login_password.text.toString()
            stringPassword = binding.loginPassword.text.toString()

            /**
             * invoke data validation method
             */
            val result = validateLoginRequest(stringEmail, stringPassword)

            if (result.successful){
                getLogin()
            }
            else{
                //toast(requireContext(),"${result.error}")
                //or
                requireContext().toast("${result.error}")
            }


        }

        login_signup.setOnClickListener {
            //navigate to sign up page
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    private fun getLogin() {
        //Log.i(TAG, "getLogin: ")
        val loginRequest= LoginRequest(stringEmail,stringPassword )

        viewModel.loginUser(loginRequest)
        viewModel.loginData.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {  response ->
                when(response){
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { loginResponse ->
                            //navigate to home fragment
                            //findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                            //navigate to Agent HomeActivity
                            if (Constants.userType.equals("A")){

                                checkIfHousesAlreadyExists()
                            }
                            else if (Constants.userType.equals("C")){
                                Intent(requireContext(), HomeClientActivity::class.java).also {
                                    requireContext().startActivity(it)
                                }
                            }
                            else{
                                return@let null
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

    private fun checkIfHousesAlreadyExists() {
        /**
         * If user doesnt have UserHouse Object, then create
         */
        if (Constants.hasHouses == false){ //false ...navigate to LoginCongrates Fragment so as to register User House Object
            findNavController().navigate(R.id.action_loginFragment_to_loginCongratsAgentFragment)
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