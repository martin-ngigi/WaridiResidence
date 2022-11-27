package com.example.waridiresidence.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.waridiresidence.R
import com.example.waridiresidence.data.model.modelrequest.UserRequest
import com.example.waridiresidence.databinding.FragmentSignupBinding
import com.example.waridiresidence.presentation.viewmodel.RegisterViewModel
import com.example.waridiresidence.util.Resource
import com.example.waridiresidence.util.Utils.validateRegisterRequest
import com.example.waridiresidence.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.login_progress


@AndroidEntryPoint
class SignupFragment: BaseFragment<FragmentSignupBinding, RegisterViewModel>() {

    override val viewModel: RegisterViewModel by viewModels()
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var password: String
    private lateinit var userType: String


    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_signin.setOnClickListener {
            //navigate to login
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        doInitializations(view)

        //listener for usertype
        setOnCheckedChangeListener()
    }

    private fun doInitializations(view: View) = with(binding) {
        register_button.setOnClickListener{
            hideKeyboard()
            firstName = fName.text.toString()
            lastName = lName.text.toString()
            email= email_tie.editableText.toString()
            phone = phone_tie.editableText.toString()
            password = register_password.editableText.toString()
            //userType = fName.text.toString()

            val checkUserTypeButtonId =rgUser.checkedRadioButtonId
            val userTypeRadioGroup = view.findViewById<RadioButton>(checkUserTypeButtonId)
            userType = userTypeRadioGroup.text.toString()

            val result = validateRegisterRequest(firstname = firstName, lastname = lastName, email = email, phone = phone, password =  password)
            /**
             * invoke data validation method
             */
            if (result.successful){
                getRegister(userType)
            }
            else{
                toast("${result.error}")
            }
        }
    }

    private fun getRegister(userType: String) {
        val userRequest = UserRequest(email=email,
            firstName = firstName,
            lastName = lastName,
            password=password,
            phone=phone,
            userType = userType.get(0).toString(),
            username = email)

        viewModel.registerUser(userRequest)
        viewModel.registerData.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { response ->
                when(response){
                    is Resource.Success ->{
                        hideProgressBar()
                        response.data?.let {
                            userResponse ->
                            //navigate to login
                            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                        }
                    }
                    is Resource.Error ->{
                        hideKeyboard()
                        response.message?.let { toast(it) }
                    }

                    is Resource.Loading ->{
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

    /**
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container,false)
    }
    */



    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentSignupBinding.inflate(inflater, container, false)

    private fun setOnCheckedChangeListener() {
        rgUser.setOnCheckedChangeListener { group, checkedId ->
//            val text = "You selected: " + if (R.id.rbAgent == checkedId) "Agent" else  "female"
            val text = "You selected: " +(if (R.id.rbAgent == checkedId) "\nAgent" else "")+ //if true.. answer is agent else its empty
                    (if (R.id.rbClient == checkedId) "\nClient" else "")
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        }
    }
}