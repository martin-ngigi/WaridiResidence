package com.example.waridiresidence.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.waridiresidence.R
import com.example.waridiresidence.data.model.modelrequest.LoginRequest
import com.example.waridiresidence.databinding.FragmentLoginBinding
import com.example.waridiresidence.util.Resource
import com.example.waridiresidence.util.hideKeyboard
import com.example.waridiresidence.presentation.viewmodel.LoginViewModel
import com.example.waridiresidence.util.Utils.validateLoginRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment: BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override val viewModel: LoginViewModel by viewModels()
    lateinit var stringEmail: String
    lateinit var stringPassword: String
    lateinit var deviceId: String
    private val TAG = "LoginPage"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceId = Settings.Secure.getString(
            requireContext().contentResolver, Settings.Secure.ANDROID_ID
        )

        doInitializations()
    }

    private fun doInitializations() = with(binding){
        //getTextWatcherLogin()
        login_button.setOnClickListener{
            hideKeyboard()
            stringEmail = login_username.text.toString()
            stringPassword = login_password.text.toString()

            /**
             * invoke data validation method
             */
            val result = validateLoginRequest(stringEmail, stringPassword)

            if (result.successful){
                getLogin()
            }
            else{
                toast("${result.error}")
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
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        }
                    }

                    is Resource.Error ->{
                        hideProgressBar()
                        response.message?.let { toast(it) }
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

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)


}