package com.circleappsstudio.mimisa.ui.main.admin

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.circleappsstudio.mimisa.utils.AppRate
import com.circleappsstudio.mimisa.vo.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_admin_main.*

class AdminMainActivity : BaseActivity(),
        UI.AdminMainActivity,
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

    override fun getLayout(): Int = R.layout.activity_admin_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navView: BottomNavigationView = findViewById(R.id.nav_view_admin_activity)

        navController = findNavController(R.id.nav_host_fragment_admin_main_nav_graph)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.admin_home,
                R.id.admin_seat_reservation,
                R.id.admin_intention,
                R.id.admin_more
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        setNavViewVisibility()

        fetchData()

        initAppRate()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun setNavViewVisibility() {

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            when(destination.id){

                R.id.admin_home -> showNavView()
                R.id.admin_seat_reservation -> showNavView()
                R.id.admin_intention -> showNavView()
                R.id.admin_more -> showNavView()

                else -> hideNavView()

            }

        }

    }

    override fun showNavView() {
        nav_view_admin_activity.visibility = View.VISIBLE
    }

    override fun hideNavView() {
        nav_view_admin_activity.visibility = View.GONE
    }

    override fun fetchData() {

        if (!isOnline(this)) {
            showIsOnlineDialog()
            return
        }

        fetchVersionCode()

    }

    override fun fetchVersionCode() {
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

    override fun initAppRate() {
        AppRate().initAppRate(this, this)
    }

    override fun showUpdateAppDialog() {
        updateAppDialog(this)
    }

    override fun showIsOnlineDialog() {
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