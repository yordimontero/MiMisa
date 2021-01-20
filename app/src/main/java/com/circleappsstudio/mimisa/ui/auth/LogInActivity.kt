package com.circleappsstudio.mimisa.ui.auth

import com.circleappsstudio.mimisa.R
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.circleappsstudio.mimisa.base.BaseActivity

class LogInActivity : BaseActivity() {

    private lateinit var navController: NavController

    override fun getLayout(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController(R.id.nav_host_fragment_login_nav_graph)

        NavigationUI.setupActionBarWithNavController(this, navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

}