package com.example.waridiresidence.presentation.ui.agent.fragments

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.waridiresidence.R
import com.example.waridiresidence.data.model.modelrequest.UserAgentProfileRequest
import com.example.waridiresidence.databinding.FragmentProfileAgentBinding
import com.example.waridiresidence.presentation.ui.agent.viewmodels.ProfileAgentViewModel
import com.example.waridiresidence.util.*
import com.example.waridiresidence.util.Utils.validateProfileRequest
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileAgentFragment : Fragment(R.layout.fragment_profile_agent) {
    private lateinit var binding: FragmentProfileAgentBinding

    private val TAG : String = "ProfileAgentFragment"
    private val viewModel: ProfileAgentViewModel by viewModels()
    private lateinit var imageUri: Uri
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var phone: String

    //open gallery
    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK){
            binding.loginProgress.loadingProgress.hide()
            val fileUri = data?.data!!
            imageUri = fileUri

            binding.uploadTV.show()
            binding.upLoadBtn.visibility = View.VISIBLE
            binding.uploadTV.text = "Click to upload"

            Glide.with(requireContext())
                .load(imageUri)
                .into(binding.imageView)
        }
        else if (resultCode == ImagePicker.RESULT_ERROR){
            binding.loginProgress.loadingProgress.hide()
            toast(ImagePicker.getError(data))
        }
        else{
            binding.loginProgress.loadingProgress.hide()
            toast("Task Cancelled.")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding = FragmentProfileBinding.bind(view)
       updateUI()
        observer()
    }

    private fun observer() {

    }

    private fun updateUI() {


        binding.loginProgress.loadingProgress.hide()

        //get data from constants and set it in The ui
        setUIData()

        /**
         * Handle imageView click listener
         */
        binding.imageView.setOnClickListener {
            binding.loginProgress.loadingProgress.show()
            ImagePicker.with(this)
                .compress(1024)
                .galleryOnly()
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        binding.upLoadBtn.setOnClickListener {
            binding.loginProgress.loadingProgress.show()
            uploadImageToStorage()
            getUserProfileData()
        }

        binding.tvFname.setOnClickListener {
            val textName: String = binding.tvFname.text.toString()
            showDialog( binding.tvFname,textName )
        }

        binding.tvLname.setOnClickListener {
            val textName: String = binding.tvLname.text.toString()
            showDialog( binding.tvLname,textName )
        }

        binding.tvPhone.setOnClickListener {
            val textName: String = binding.tvPhone.text.toString()
            showDialog( binding.tvPhone,textName )
        }



    }

    private fun setUIData() {
        //load image to Imageview
        if (Constants.profile_image.isNotEmpty()){
            Glide
                .with(this)
                .load(Constants.profile_image)
                .into(binding.imageView)
        }
        //set textviews
        binding.tvFname.text = Constants.fname
        binding.tvLname.text = Constants.lname
        binding.tvPhone.text = Constants.phone

    }

    private fun getUserProfileData() {
        firstName = binding.tvFname.text.toString()
        lastName = binding.tvLname.text.toString()
        phone = binding.tvPhone.text.toString()
        val result = validateProfileRequest(firstname = firstName, lastname = lastName, phone = phone)
        if (result.successful){
            updateToDB()
        }
        else{
            toast(result.error)
        }
    }

    //val max = if (a > b) a else b
    private fun updateToDB() {
        val userRequest = UserAgentProfileRequest(
            firstName=firstName,
            lastName = lastName,
            phone = phone,
            profileImage =  if (imageUri.toString().isNotEmpty()) imageUri.toString() else Constants.profile_image,
            hasHouses = Constants.hasHouses
        )

        //profileImage is statement
        /**
        if (imageUri.toString().isNotEmpty()){
            profileImage = imageUri.toString()
        }
        else{
            profileImage = Constants.profile_image
        }
        */

        viewModel.updateUser(userRequest)
        viewModel.updateUserData.observe(viewLifecycleOwner, Observer { event->
            event.getContentIfNotHandled()?.let { response ->
                when(response){
                    is Resource.Success -> {
                        hideProgressBar()
                        response.message?.let {
                            toast(it)
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

    private fun showDialog(textView: TextView, textName: String) {
        val dialog = requireContext().createDialog(R.layout.single_item_dialog, true)
        val button = dialog.findViewById<MaterialButton>(R.id.tag_dialog_add)
        val editText = dialog.findViewById<EditText>(R.id.tag_dialog_et)
        editText.setText(textName)
        button.setOnClickListener{
            if (editText.text.toString().isEmpty()){
                toast("Enter Text")
            }
            else{
                val text = editText.text.toString()
                textView.text = text

                //show update button and text
                binding.uploadTV.show()
                binding.upLoadBtn.visibility = View.VISIBLE
                binding.uploadTV.text = "Click to upload"

                dialog.dismiss()
            }
        }
        dialog.show()

    }

    private fun uploadImageToStorage() {
        if (imageUri.toString().isNotEmpty()){
            viewModel.onUploadSingleFile(imageUri){state ->
                when(state){
                    is UiState.Loading -> {
                        showProgressBar()
                    }
                    is UiState.Failure -> {
                        hideProgressBar()
                        toast(state.error)
                    }
                    is UiState.Success ->{
                        //hideProgressBar()
                        binding.uploadTV.hide()
                        binding.upLoadBtn.visibility = View.GONE

                        toast("Image Uploaded successfully.")
                    }
                }
            }
        }
        else{
            toast("You have not selected any image... Or an error occurred while selecting the image")
        }
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
            binding = FragmentProfileAgentBinding.inflate(layoutInflater)
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