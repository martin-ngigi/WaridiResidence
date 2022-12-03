package com.example.waridiresidence.presentation.ui.agent.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.waridiresidence.R
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.waridiresidence.databinding.ActivityHomeAgentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home_client.*
import kotlinx.android.synthetic.main.my_toolbar.*

@AndroidEntryPoint
class HomeAgentActivity : AppCompatActivity() {

    //lateinit var navigationView: NavigationView
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var binding: ActivityHomeAgentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)
        binding = ActivityHomeAgentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(myToolbar)
        navController = findNavController(R.id.fragmentContainerViewAgent)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.id_homeAgentFragment, R.id.id_profileAgentFragment, R.id.id_settingsAgentFragment), drawer_layout)
        setupActionBarWithNavController(navController, drawer_layout)
        navigationView.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerViewAgent)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}