package com.example.waridiresidence.presentation.ui.agent.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.waridiresidence.R
import com.example.waridiresidence.data.model.modelrequest.house.UserHouseRequest
import com.example.waridiresidence.databinding.FragmentLoginCongratsAgentBinding
import com.example.waridiresidence.presentation.ui.agent.activities.HomeAgentActivity
import com.example.waridiresidence.presentation.ui.agent.viewmodels.LoginCongratsAgentViewModel
import com.example.waridiresidence.util.*
import com.example.waridiresidence.util.Utils.validateProfileRequest
import com.example.waridiresidence.util.Utils.validateUserHouseRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginCongratsAgentFragments : Fragment(R.layout.fragment_login_congrats_agent) {
    private lateinit var binding: FragmentLoginCongratsAgentBinding

    private val TAG : String = "LoginCongratsAgent"
    private val viewModel: LoginCongratsAgentViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding = FragmentProfileBinding.bind(view)
       updateUI()
        observer()
    }

    private fun observer() {

    }

    private fun updateUI() {
        binding.proceedToHomeBtn.setOnClickListener {
            getUserProfileData()
        }



    }


    private fun getUserProfileData() {
        val result = validateUserHouseRequest(agentId = Constants.id, agentName = Constants.lname, phone = Constants.phone)
        if (result.successful){
            updateToDB()
        }
        else{
            toast(result.error)
        }
    }

    //val max = if (a > b) a else b
    private fun updateToDB() {
        Log.i(TAG, "I AM HERE 0")
        val userRequest = UserHouseRequest(
            agentId = Constants.id,
            agentName =Constants.fname+" "+Constants.lname,
            phone = Constants.phone
        )

        Log.i(TAG, "I AM HERE 1")
        viewModel.registerUserHouse(userRequest)
        Log.i(TAG, "I AM HERE 2")
        viewModel.registerUserHouseData.observe(viewLifecycleOwner, Observer { event->
            Log.i(TAG, "I AM HERE 3")
            //THIS IS LOGICAL ERROR SINCE  INTENT SHOULD BE ADDED INSIDE   is Resource.Success -> {}
            Intent(requireContext(), HomeAgentActivity::class.java).also {
                startActivity(it)
            }

            event.getContentIfNotHandled()?.let { response ->
                when(response){
                    is Resource.Success -> {
                        hideProgressBar()
                        response.message?.let {
                            toast("Yeeeey!!! $it")
                            Intent(requireContext(), HomeAgentActivity::class.java).also {
                                startActivity(it)
                            }
                            Log.i(TAG, "updateToDB: MARTIN UPDATED SUCCESSFULLY")
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let {
                            toast(it)
                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }
        })
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (this:: binding.isInitialized){
            return binding.root
        }
        else{
            binding = FragmentLoginCongratsAgentBinding.inflate(layoutInflater)
            return binding.root
        }
    }


    private fun showProgressBar() {
        binding.loginProgress.loadingProgress.show()
    }

    private fun hideProgressBar() {
        binding.loginProgress.loadingProgress.hide()
    }
}