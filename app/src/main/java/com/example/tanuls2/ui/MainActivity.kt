package com.example.tanuls2.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tanuls2.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.findNavController()
        val navigationView = findViewById<BottomNavigationView>(R.id.navView)
        navigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            when (navDestination.id) {
                R.id.combat, R.id.inventory, R.id.skills  -> navigationView.visibility = VISIBLE
                R.id.itemDetails -> navigationView.visibility = GONE
            }
        }

    }
}









