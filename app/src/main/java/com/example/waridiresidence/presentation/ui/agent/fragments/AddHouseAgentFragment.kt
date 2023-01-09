package com.example.waridiresidence.presentation.ui.agent.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.waridiresidence.R
import com.example.waridiresidence.data.model.modelrequest.house.HouseImageRequest
import com.example.waridiresidence.data.model.modelrequest.house.HouseRequest
import com.example.waridiresidence.databinding.FragmentAddHouseAgentBinding
import com.example.waridiresidence.presentation.ui.agent.activities.HomeAgentActivity
import com.example.waridiresidence.presentation.ui.agent.viewmodels.AddHouseViewModel
import com.example.waridiresidence.util.*
import com.example.waridiresidence.util.Utils.toast
import com.example.waridiresidence.util.Utils.validateHouseDescription
import com.example.waridiresidence.util.Utils.validateHouseImages
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddHouseAgentFragment: Fragment(R.layout.fragment_add_house_agent){

    val viewModel: AddHouseViewModel by viewModels()
    //initialize
    lateinit var category: String
    lateinit var title: String
    lateinit var description: String
    lateinit var location: String
    lateinit var price: String
    lateinit var imageTitle: String
    lateinit var imageDescription: String
    private lateinit var imageUri: Uri

    private val TAG = "AddHouseAgentFragment"

    lateinit var binding: FragmentAddHouseAgentBinding

    //open gallery
    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK){
            hideProgressBar()
            val fileUri = data?.data!!
            imageUri = fileUri
            binding.imageCoverRL.visibility = View.GONE

            Glide.with(requireContext())
                .load(imageUri)
                .into(binding.houseImageView)
        }
        else if (resultCode == ImagePicker.RESULT_ERROR){
            hideProgressBar()
            toast(ImagePicker.getError(data))
        }
        else{
            hideProgressBar()
            toast("Task Cancelled.")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddHouseAgentBinding.bind(view)

        doInit(view)

        //listen for Category
        setOnCheckedChangeListener()
    }

    private fun setOnCheckedChangeListener() {
//        binding.categorySpinner.setOn
        //By default, spinner will use the strings in R>Strings>array... But if we want to set the programmatically in the code
        //we can still comment it if we want to use R.Strings.months_array
        val customList = listOf("Single Room", "BedSitter", "One Bedroom", "Two Bedroom", "Three Bedroom", "Four Bedroom")
        //val adapter = ArrayAdapter<String>(this, com.karumi.dexter.R.layout.support_simple_spinner_dropdown_item, customList)
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, customList)
        binding.categorySpinner.adapter = adapter

        //handle spinner
        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(requireContext(), "You selected  ${adapterView?.getItemAtPosition(position).toString()}", Toast.LENGTH_SHORT).show()
                category = adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun doInit(view: View) {
        //add houses description
        binding.addHouseDescBtn.setOnClickListener {
            hideKeyboard()
            //get data from ui
            //category = binding.categoryTv.text.toString()
            title = binding.titleEt.text.toString()
            description = binding.descriptionEt.text.toString()
            location = binding.locationEt.text.toString()
            price = binding.priceEt.text.toString()//Convert to integer

            val result = validateHouseDescription(title, description, location, price)

            if (result.successful){
                getHouseDescription()
            }
            else{
                requireContext().toast("${result.error}")
            }
        }

        //add houses image
        binding.houseImageView.setOnClickListener{
            showProgressBar()
            ImagePicker.with(this)
                .compress(1024)
                .galleryOnly()
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        //upload images
        binding.addHomeImagesBtn.setOnClickListener{
            hideKeyboard()
            imageTitle = binding.titleImageEt.text.toString()
            imageDescription = binding.descriptionImageTv.text.toString()

            val result = validateHouseImages(imageTitle, imageDescription)
            if (result.successful){
                showProgressBar()
                uploadImageToStorage()
                getHouseImages("one")
            }
            else{
                requireContext().toast("${result.error}")
            }

        }

        //proceed to home
        binding.proceedHomeBtn.setOnClickListener{
            Intent(requireContext(), HomeAgentActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.addHomeMultipleImagesBtn.setOnClickListener {
            hideKeyboard()
            imageTitle = binding.titleImageEt.text.toString()
            imageDescription = binding.descriptionImageTv.text.toString()

            val result = validateHouseImages(imageTitle, imageDescription)
            if (result.successful){
                showProgressBar()
                uploadImageToStorage()
                getHouseImages("two")
            }
            else{
                requireContext().toast("${result.error}")
            }
        }

        binding.nextBtn.setOnClickListener{
            binding.addHomeImagesRL.visibility = View.GONE
            binding.congratsRL.visibility = View.VISIBLE
        }

    }

    private fun getHouseImages(text: String) {
        val houseImageRequest = HouseImageRequest(
            imageTitle,
            Constants.currentHouseId,
            imageDescription,
            imageUri.toString()
        )
        viewModel.addHouseImages(houseImageRequest)
        viewModel.addHouseImages.observe( viewLifecycleOwner, Observer {  event ->
            event.getContentIfNotHandled()?.let { response ->
                when(response){
                    is Resource.Success ->{
                        hideProgressBar()
                        response.data?.let { addHouseImagesResponse ->
                            toast("House Images added Successfully ;-)")
                            Log.i(TAG, "House Images added Successfully ;-)")

                            if (text.equals("one")){
                                binding.addHomeImagesRL.visibility = View.GONE
                                binding.congratsRL.visibility = View.VISIBLE
                            }
                            else if (text.equals("two")){
                                binding.titleImageEt.setText("")
                                binding.descriptionImageTv.setText("")
                                binding.houseImageView.setImageDrawable(resources.getDrawable(R.drawable.house))
                                binding.nextBtn.visibility = View.VISIBLE
                            }

                        }
                    }
                    is Resource.Error ->{
                        hideProgressBar()
                        toast("Error adding house Images")
                        Log.e(TAG, "Error adding House Images ", )
                        response.message?.let { requireContext().toast(it) }

                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }
        })
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
                        hideProgressBar()
                        toast("Image Uploaded successfully.")
                    }
                }
            }
        }
        else{
            toast("You have not selected any image... Or an error occurred while selecting the image")
        }
    }

    private fun getHouseDescription() {
        val houseRequest = HouseRequest(
            category,
            description,
            location,
            price.toInt(),
            "Vacant",
            title,
            Constants.idUserHouse)

        viewModel.addHouseDescription(houseRequest)
        viewModel.addHouseData.observe( viewLifecycleOwner, Observer {  event ->
            event.getContentIfNotHandled()?.let { response ->
                when(response){
                    is Resource.Success ->{
                        hideProgressBar()
                        response.data?.let { addHouseResponse ->
                            toast("House added Successfully ;-)")
                            Log.i(TAG, "House added Successfully ;-)")
                            binding.addHomeDesRL.visibility = View.GONE
                            binding.addHomeImagesRL.visibility = View.VISIBLE
                        }
                    }
                    is Resource.Error ->{
                       hideProgressBar()
                        toast("Error adding house")
                        //Log.e(TAG, "Error adding House: ", )
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
        binding.progressBar.loadingProgress.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.loadingProgress.visibility = View.GONE
    }
}