package com.circleappsstudio.mimisa.ui.main

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var navController: NavController

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment_main_nav_graph)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                    R.id.navigation_home, R.id.navigation_seat_reservation, R.id.navigation_intention, R.id.navigation_more
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        setNavViewVisibility()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    fun setNavViewVisibility(){

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            when(destination.id){

                R.id.navigation_home -> showNavView()
                R.id.navigation_seat_reservation -> showNavView()
                R.id.navigation_intention -> showNavView()
                R.id.navigation_more -> showNavView()

                else -> hideNavView()

            }

        }

    }

    fun showNavView(){
        nav_view.visibility = View.VISIBLE
    }

    fun hideNavView(){
        nav_view.visibility = View.GONE
    }

}