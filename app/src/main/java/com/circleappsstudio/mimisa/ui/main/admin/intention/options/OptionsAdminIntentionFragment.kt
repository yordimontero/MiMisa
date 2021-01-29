package com.circleappsstudio.mimisa.ui.main.admin.intention.options

import android.app.AlertDialog
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
import kotlinx.android.synthetic.main.fragment_options_admin_intention.*

class OptionsAdminIntentionFragment : BaseFragment(),
            UI.OptionsAdminIntentions,
            UI.IsOnlineDialogClickButtonListener,
            UI.ConfirmDialogClickButtonListener {

    private val paramsViewModel by activityViewModels<ParamsViewModel> {
        VMFactoryParams(
            ParamsRepository(
                ParamsDataSource()
            )
        )
    }

    private var isSeatReservationAvailable = true

    private lateinit var selectedButton: String

    override fun getLayout(): Int = R.layout.fragment_options_admin_intention

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()

        setAvailability()

    }

    override fun fetchData() {

        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
            return
        }

        fetchIsRegisterIntentionAvailableObserver()

    }

    override fun fetchIsRegisterIntentionAvailableObserver() {
        /*
            Método encargado de escuchar en tiempo real si el registro de intenciones esta habilitado
            o deshabilitado.
        */
        if (isOnline(requireContext())) {

            paramsViewModel.fetchIsRegisterIntentionAvailable()
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when (resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {

                            isSeatReservationAvailable = resultEmitted.data

                            if (isSeatReservationAvailable) {
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

        btn_set_register_intention_availability.setOnClickListener {

            if (!isOnline(requireContext())) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            selectedButton = "btn_set_availability"
            showConfirmDialog()

        }

    }

    override fun enableRegisterIntention() {
        /*
            Método encargado de habilitar el registro de intenciones.
        */
        paramsViewModel.setIsRegisterIntentionAvailable(true)
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when (resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        showMessage(getString(R.string.register_intention_has_been_enabled), 2)
                        hideProgressBar()
                    }

                    is Resource.Failure -> {
                        showMessage(resultEmitted.exception.message.toString(), 2)
                        hideProgressBar()
                    }

                }

            })

    }

    override fun disableRegisterIntention() {
        /*
            Método encargado de deshabilitar el registro de intenciones.
        */
        paramsViewModel.setIsRegisterIntentionAvailable(false)
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when (resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        showMessage(getString(R.string.register_intention_has_been_disabled), 2)
                        hideProgressBar()
                    }

                    is Resource.Failure -> {
                        showMessage(resultEmitted.exception.message.toString(), 2)
                        hideProgressBar()
                    }

                }

            })

    }

    override fun activateToggle() {
        /*
            Método encargado de activar un Toggle.
        */
        txt_enable_or_disable_register_intention.text = getString(R.string.disable_register_intention)
        btn_set_register_intention_availability.setImageResource(R.drawable.ic_toggle_on)
        btn_set_register_intention_availability.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
    }

    override fun deactivateToggle() {
        /*
            Método encargado de desactivar un Toggle.
        */
        txt_enable_or_disable_register_intention.text = getString(R.string.enable_register_intention)
        btn_set_register_intention_availability.setImageResource(R.drawable.ic_toggle_off)
        btn_set_register_intention_availability.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))
    }

    override fun showMessage(message: String, duration: Int) {
        /*
            Método encargado de mostrar un Toast.
        */
        requireContext().toast(requireContext(), message, duration)

    }

    override fun showProgressBar() {
        /*
            Método encargado de mostrar un ProgressBar.
        */
        progressbar_options_admin_intention.visibility = View.VISIBLE

    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un ProgressBar.
        */
        progressbar_options_admin_intention.visibility = View.GONE

    }

    override fun showIsOnlineDialog() {
        /*
            Método encargado de mostrar el Dialog "IsOnlineDialog".
        */
        isOnlineDialog(this)
    }

    override fun isOnlineDialogPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "IsOnlineDialog".
        */
        if (isOnline(requireContext())) {
            fetchData()
        } else {
            showIsOnlineDialog()
        }

    }

    override fun showConfirmDialog(): AlertDialog? {
        /*
            Método encargado de mostrar el Dialog "ConfirmDialog".
        */
        var message = ""

        when {

            selectedButton.contains("btn_set_availability") -> {

                message = if (isSeatReservationAvailable) {
                    getString(R.string.do_you_want_to_disable_register_intention)
                } else {
                    getString(R.string.do_you_want_to_enable_register_intention)
                }

            }

        }

        return confirmDialog(this, message)

    }

    override fun confirmPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "ConfirmDialog".
        */
        when {

            selectedButton.contains("btn_set_availability") -> {

                if (isSeatReservationAvailable) {
                    disableRegisterIntention()
                } else {
                    enableRegisterIntention()
                }

            }

        }

    }

    override fun confirmNegativeButtonClicked() {
        /*
            Método encargado de controlar el botón negativo del Dialog "isOnlineDialog".
        */
        showConfirmDialog()!!.dismiss()
    }

}