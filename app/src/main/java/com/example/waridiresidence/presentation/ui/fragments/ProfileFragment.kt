package com.example.waridiresidence.presentation.ui.fragments

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
import com.bumptech.glide.Glide
import com.example.waridiresidence.R
import com.example.waridiresidence.databinding.FragmentProfileBinding
import com.example.waridiresidence.presentation.viewmodel.ProfileViewModel
import com.example.waridiresidence.util.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
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
                        binding.loginProgress.loadingProgress.show()
                    }
                    is UiState.Failure -> {
                        binding.loginProgress.loadingProgress.hide()
                        toast(state.error)
                    }
                    is UiState.Success ->{
                        updateImageUrlDB()
                        binding.loginProgress.loadingProgress.hide()
                        binding.uploadTV.hide()
                        binding.upLoadBtn.visibility = View.GONE

                        toast("Image Uploaded successfully.")
                    }
                }
            }
        }
    }

    private fun updateImageUrlDB() {

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