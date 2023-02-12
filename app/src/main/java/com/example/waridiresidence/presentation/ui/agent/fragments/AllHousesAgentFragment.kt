package com.example.waridiresidence.presentation.ui.agent.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waridiresidence.R
import com.example.waridiresidence.data.model.modelresponse.house.AllHousesResult
import com.example.waridiresidence.databinding.FragmentAllHousesAgentBinding
import com.example.waridiresidence.presentation.ui.agent.adapters.AllHomesAgentAdapters
import com.example.waridiresidence.presentation.ui.agent.viewmodels.AllHousesAgentViewModel
import com.example.waridiresidence.util.Resource
import com.example.waridiresidence.util.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AllHousesAgentFragment: Fragment(R.layout.fragment_all_houses_agent){

    val viewModel: AllHousesAgentViewModel by viewModels()
    lateinit var binding: FragmentAllHousesAgentBinding

    var TAG = "AllHousesAgentFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAllHousesAgentBinding.bind(view)

        doInit(view)
    }

    private fun doInit(view: View) {
        getAllAgentHouses()
    }

    private fun getAllAgentHouses() {
        viewModel.getHouses()
        viewModel.houses.observe( viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { response ->
                when(response){
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { allHousesResponse ->
                            toast("Getting all houses success")
                            Log.i(TAG, "getAllAgentHouses: Success getting all houses ")
                            setDataToUI(allHousesResponse.results)
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        toast("Error Getting all houses")
                        Log.i(TAG, "Error  getting all houses")

                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }
        })
    }

    private fun setDataToUI(results: List<AllHousesResult>) {
        val adapter = AllHomesAgentAdapters(results, viewModel)
        binding.housesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.housesRv.adapter = adapter
    }

    private fun showProgressBar() {
        binding.progressBar.loadingProgress.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.loadingProgress.visibility = View.GONE
    }
}