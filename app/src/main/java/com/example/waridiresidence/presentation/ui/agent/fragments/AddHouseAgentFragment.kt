package com.example.waridiresidence.presentation.ui.agent.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.waridiresidence.R
import com.example.waridiresidence.data.model.modelrequest.house.HouseRequest
import com.example.waridiresidence.databinding.FragmentAddHouseAgentBinding
import com.example.waridiresidence.presentation.ui.agent.viewmodels.AddHouseViewModel
import com.example.waridiresidence.util.Constants
import com.example.waridiresidence.util.Resource
import com.example.waridiresidence.util.Utils.toast
import com.example.waridiresidence.util.Utils.validateHouseDescription
import com.example.waridiresidence.util.hideKeyboard
import com.example.waridiresidence.util.toast
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

    private val TAG = "AddHouseAgentFragment"

    lateinit var binding: FragmentAddHouseAgentBinding
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
        binding.addHouseBtn.setOnClickListener {
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