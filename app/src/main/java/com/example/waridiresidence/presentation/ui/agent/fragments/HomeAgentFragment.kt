package com.example.waridiresidence.presentation.ui.agent.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.waridiresidence.R
import com.example.waridiresidence.databinding.FragmentHomeAgentBinding

class HomeAgentFragment:  Fragment(R.layout.fragment_home_agent) {

    lateinit var binding: FragmentHomeAgentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeAgentBinding.bind(view)

        initializations()
    }

    private fun initializations() {
        //add house
        binding.addHomeCV.setOnClickListener{
            findNavController().navigate(R.id.action_id_homeAgentFragment_to_add_house_nav_graph)
        }

        //view all houses
        binding.allHomeCV.setOnClickListener{
            findNavController().navigate(R.id.action_id_homeAgentFragment_to_view_houses_agent_graph)
        }
    }
}