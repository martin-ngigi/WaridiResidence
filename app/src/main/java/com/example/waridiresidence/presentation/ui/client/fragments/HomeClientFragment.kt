package com.example.waridiresidence.presentation.ui.client.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.waridiresidence.R
import com.example.waridiresidence.databinding.FragmentHomeClientBinding

class HomeClientFragment:  Fragment(R.layout.fragment_home_client) {

    lateinit var binding: FragmentHomeClientBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeClientBinding.bind(view)
    }
}