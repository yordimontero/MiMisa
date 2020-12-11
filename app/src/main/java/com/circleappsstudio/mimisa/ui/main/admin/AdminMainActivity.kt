package com.circleappsstudio.mimisa.ui.main.admin

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_admin_main.*

class AdminMainActivity : BaseActivity() {

    private lateinit var navController: NavController

    override fun getLayout(): Int {
        return R.layout.activity_admin_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navView: BottomNavigationView = findViewById(R.id.nav_view_admin_activity)

        navController = findNavController(R.id.nav_host_fragment_admin_main_nav_graph)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home_admin,
                R.id.navigation_seat_reservation_admin,
                R.id.navigation_intention_admin,
                R.id.navigation_more_admin
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

                /*R.id.navigation_home -> showNavView()
                R.id.navigation_seat_reservation -> showNavView()
                R.id.navigation_intention -> showNavView()
                R.id.navigation_more -> showNavView()

                else -> hideNavView()*/

            }

        }

    }

    fun showNavView(){
        nav_view_admin_activity.visibility = View.VISIBLE
    }

    fun hideNavView(){
        nav_view_admin_activity.visibility = View.GONE
    }

}