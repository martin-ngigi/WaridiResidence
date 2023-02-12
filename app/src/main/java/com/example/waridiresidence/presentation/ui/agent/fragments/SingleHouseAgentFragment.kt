package com.example.waridiresidence.presentation.ui.agent.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.waridiresidence.R
import com.example.waridiresidence.databinding.FragmentSingleHouseAgentBinding
import com.example.waridiresidence.util.Constants

class SingleHouseAgentFragment: Fragment(R.layout.fragment_single_house_agent) {
    lateinit var binding: FragmentSingleHouseAgentBinding

    var TAG ="SingleHouseAgentFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSingleHouseAgentBinding.bind(view)

        doInit(view)
    }

    private fun doInit(view: View) {
        setDataToUI()
    }

    private fun setDataToUI() {
        binding.titleTv.text = Constants.HOUSE_OBJECT.title
        binding.categoryTv.text = Constants.HOUSE_OBJECT.category
        binding.descriptionTv.text = Constants.HOUSE_OBJECT.description
        binding.locationTv.text = Constants.HOUSE_OBJECT.location
        binding.monthlyPriceTv.text = Constants.HOUSE_OBJECT.monthlyPrice.toString()
        binding.statusTv.text = Constants.HOUSE_OBJECT.status
    }
}