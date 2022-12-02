package com.example.waridiresidence.presentation.ui.fragments

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.waridiresidence.R
import com.example.waridiresidence.databinding.FragmentProfileBinding
import com.example.waridiresidence.presentation.viewmodel.ProfileViewModel
import com.example.waridiresidence.util.UiState
import com.example.waridiresidence.util.hide
import com.example.waridiresidence.util.show
import com.example.waridiresidence.util.toast
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding

    private val TAG : String = "ProfileFragment"
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var imageUri: Uri

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
        }
    }

    private fun uploadImageToStorage() {
        if (imageUri.toString().isNotEmpty()){


            viewModel.onUploadSingleFile(imageUri){state ->
                when(state){
                    is UiState.Loading -> {
                        binding.loginProgress.loadingProgress.show()
                    }
                    is UiState.Failure -> {
                        binding.loginProgress.loadingProgress.hide()
                        toast(state.error)
                    }
                    is UiState.Success ->{
                        loadImageFromDB()
                        binding.loginProgress.loadingProgress.hide()
                        binding.uploadTV.hide()
                        binding.upLoadBtn.visibility = View.GONE

                        toast("Image Uploaded successfully.")
                    }
                }
            }
        }
    }

    private fun loadImageFromDB() {

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
            binding = FragmentProfileBinding.inflate(layoutInflater)
            return binding.root
        }
    }
}