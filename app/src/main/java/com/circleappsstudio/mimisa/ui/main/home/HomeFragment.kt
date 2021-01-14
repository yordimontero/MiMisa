package com.circleappsstudio.mimisa.ui.main.home

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

    override fun getLayout(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()

    }

    override fun fetchData() {

        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
            return
        }

        fetchVersionCode()

    }

    override fun fetchVersionCode() {
        /*
            Método encargado de escuchar en tiempo real el versionCode en la base de datos.
        */
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
        layout_fragment_home.visibility = View.GONE
    }

    override fun hideProgressBar() {
        progressbar_home.visibility = View.GONE
        layout_fragment_home.visibility = View.VISIBLE
    }

    override fun showIsOnlineDialog() {
        isOnlineDialog(this)
    }

    override fun showUpdateAppDialog() {
        updateAppDialog(this)
    }

    override fun isOnlineDialogPositiveButtonClicked() {

        if (isOnline(requireContext())) {
            fetchData()
        } else {
            showIsOnlineDialog()
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
        requireActivity().finish()
    }

}