package com.circleappsstudio.mimisa.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseActivity
import com.circleappsstudio.mimisa.data.datasource.params.ParamsDataSource
import com.circleappsstudio.mimisa.domain.params.ParamsRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryParams
import com.circleappsstudio.mimisa.ui.viewmodel.params.ParamsViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(),
        UI.IsOnlineDialogClickButtonListener,
        UI.UpdateAppDialogClickButtonListener {

    private lateinit var navController: NavController

    private val paramsViewModel by lazy {
        ViewModelProvider(this,
                VMFactoryParams(
                        ParamsRepository(
                                ParamsDataSource()
                        )
                )
        ).get(ParamsViewModel::class.java)
    }

    private val currentVersionCode by lazy {
        fetchCurrentVersionCode()
    }

    private lateinit var fetchedVersionCode: String

    override fun getLayout(): Int = R.layout.activity_main

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

        fetchData()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun setNavViewVisibility() {

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            when (destination.id) {

                R.id.navigation_home -> showNavView()
                R.id.navigation_seat_reservation -> showNavView()
                R.id.navigation_intention -> showNavView()
                R.id.navigation_more -> showNavView()

                else -> hideNavView()

            }

        }

    }

    private fun showNavView() {
        nav_view.visibility = View.VISIBLE
    }

    private fun hideNavView() {
        nav_view.visibility = View.GONE
    }

    private fun fetchData() {

        if (!isOnline(this)) {
            showIsOnlineDialog()
            return
        }

        fetchVersionCode()

    }

    private fun fetchVersionCode() {
        /*
            Método encargado de escuchar en tiempo real el versionCode en la base de datos.
        */
        if (isOnline(this)) {

            paramsViewModel.fetchVersionCode()
                    .observe(this, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {}

                            is Resource.Success -> {

                                fetchedVersionCode = resultEmitted.data.toString()

                                if (paramsViewModel.checkVersionCode(
                                                fetchedVersionCode.toInt(),
                                                currentVersionCode)) {

                                    showUpdateAppDialog()

                                }

                            }

                            is Resource.Failure -> {
                                this.toast(this, resultEmitted.exception.message.toString(), 2)
                            }

                        }

                    })

        }

    }

    private fun showUpdateAppDialog() {
        updateAppDialog(this)
    }

    private fun showIsOnlineDialog(){
        isOnlineDialog(this, this)
    }

    override fun isOnlineDialogPositiveButtonClicked() {

        if (isOnline(this)) {
            fetchData()
        }

    }

    override fun updateAppPositiveButtonClicked() {

        goToPlayStore()

        if (paramsViewModel.checkVersionCode(
                        fetchedVersionCode.toInt(),
                        currentVersionCode)) {

            showUpdateAppDialog()

        }

    }

    override fun updateAppNegativeButtonClicked() {
        finish()
    }

}