package com.example.waridiresidence.presentation.ui.client.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.waridiresidence.R
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.waridiresidence.databinding.ActivityHomeClientBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home_client.*
import kotlinx.android.synthetic.main.my_toolbar.*

@AndroidEntryPoint
class HomeClientActivity : AppCompatActivity() {

    //lateinit var navigationView: NavigationView
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var binding: ActivityHomeClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home)
        binding = ActivityHomeClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(myToolbar)
        navController = findNavController(R.id.fragmentContainerViewClient)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.id_homeClientFragment, R.id.id_profileClientFragment, R.id.id_settingsClientFragment), drawer_layout)
        setupActionBarWithNavController(navController, binding.drawerLayout)
        navigationView.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerViewClient)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}