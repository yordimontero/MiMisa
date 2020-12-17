package com.circleappsstudio.mimisa.ui.main.admin.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.params.ParamsDataSource
import com.circleappsstudio.mimisa.domain.params.ParamsRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryParams
import com.circleappsstudio.mimisa.ui.viewmodel.params.ParamsViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_admin_home.*

class AdminHomeFragment: BaseFragment(),
    UI.AdminHome,
    UI.IsOnlineDialogClickButtonListener,
    UI.IsAvailableDialogClickButtonListener,
    UI.UpdateAppDialogClickButtonListener {

    private val paramsViewModel by activityViewModels<ParamsViewModel> {
        VMFactoryParams(
            ParamsRepository(
                ParamsDataSource()
            )
        )
    }

    private var isAvailable = true

    private val currentVersionCode by lazy {
        fetchCurrentVersionCode()
    }
    private lateinit var fetchedVersionCode: String

    override fun getLayout(): Int = R.layout.fragment_admin_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchIsAvailable()

        setAvailability()

        fetchVersionCode()

    }

    override fun fetchIsAvailable() {

        if (isOnline(requireContext())) {

            paramsViewModel.fetchIsAvailable()
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when(resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {

                            isAvailable = resultEmitted.data

                            if (isAvailable){
                                activateToggle()
                            } else {
                                deactivateToggle()
                            }

                            hideProgressBar()

                        }

                        is Resource.Failure -> {
                            showMessage(resultEmitted.exception.message.toString(), 2)
                            hideProgressBar()
                        }

                    }

                })

        }

    }

    override fun setAvailability() {

        btn_set_availability.setOnClickListener {

            if (isOnline(requireContext())) {

                if (isAvailable) {
                    disableSystem()
                } else {
                    enableSystem()
                }

            }

        }

    }

    override fun fetchVersionCode() {

        if (isOnline(requireContext())) {

            paramsViewModel.fetchVersionCode()
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when(resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {

                            fetchedVersionCode = resultEmitted.data.toString()

                            if (paramsViewModel.checkVersionCode(
                                    fetchedVersionCode.toInt(),
                                    currentVersionCode)) {

                                showUpdateAppDialog()

                            }

                            hideProgressBar()

                        }

                        is Resource.Failure -> {
                            showMessage(resultEmitted.exception.message.toString(), 2)
                            hideProgressBar()
                        }

                    }

                })

        }

    }

    override fun showMessage(message: String, duration: Int) {
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        progressbar_admin_home.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressbar_admin_home.visibility = View.GONE
    }

    override fun activateToggle() {
        btn_set_availability.setImageResource(R.drawable.ic_toggle_on)
        btn_set_availability.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))

    }

    override fun deactivateToggle() {
        btn_set_availability.setImageResource(R.drawable.ic_toggle_off)
        btn_set_availability.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))
    }

    override fun enableSystem() {

        paramsViewModel.setIsAvailable(true)
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        showMessage("El sistema está habilitado", 1)
                        hideProgressBar()
                    }

                    is Resource.Failure -> {
                        showMessage(resultEmitted.exception.message.toString(), 2)
                        hideProgressBar()
                    }

                }

            })

    }

    override fun disableSystem() {

        paramsViewModel.setIsAvailable(false)
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        showMessage("El sistema está deshabilitado", 1)
                        hideProgressBar()
                    }

                    is Resource.Failure -> {
                        showMessage(resultEmitted.exception.message.toString(), 2)
                        hideProgressBar()
                    }

                }

            })

    }

    override fun showIsOnlineDialog() {
        isOnlineDialog(this)
    }

    override fun showIsAvailableDialog() {
        isAvailableDialog(this)
    }

    override fun showUpdateAppDialog() {
        updateAppDialog(this)
    }

    override fun isOnlineDialogPositiveButtonClicked() {

        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
        }

    }

    override fun isAvailablePositiveButtonClicked() {
        requireActivity().finish()
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
        requireActivity().finish()
    }

}