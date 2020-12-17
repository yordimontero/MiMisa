package com.circleappsstudio.mimisa.ui.main.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
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
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(),
        UI.Home,
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

    private val currentVersionCode by lazy {
        fetchCurrentVersionCode()
    }
    private lateinit var fetchedVersionCode: String

    private var isAvailable = true

    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchIsAvailable()

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

                            if (!isAvailable){
                                showIsAvailableDialog()
                                showProgressBar()
                            }

                        }

                        is Resource.Failure -> {
                            showMessage(resultEmitted.exception.message.toString(), 2)
                            hideProgressBar()
                        }

                    }

                })

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
        progressbar_home.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressbar_home.visibility = View.GONE
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