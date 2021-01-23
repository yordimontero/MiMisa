package com.circleappsstudio.mimisa.ui.main.admin.seatreservation.options

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.params.ParamsDataSource
import com.circleappsstudio.mimisa.domain.params.ParamsRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryParams
import com.circleappsstudio.mimisa.ui.viewmodel.params.ParamsViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_options_admin_seat_reservation.*

class OptionsAdminSeatReservationFragment : BaseFragment(),
        UI.OptionsAdminSeatReservation,
        UI.IsOnlineDialogClickButtonListener,
        UI.ConfirmDialogClickButtonListener {

    private lateinit var navController: NavController

    private val paramsViewModel by activityViewModels<ParamsViewModel> {
        VMFactoryParams(
                ParamsRepository(
                        ParamsDataSource()
                )
        )
    }

    private lateinit var seatLimit: String

    private var isSeatReservationAvailable = true

    private lateinit var selectedButton: String

    override fun getLayout(): Int = R.layout.fragment_options_admin_seat_reservation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        fetchData()

        updateSeatLimit()

        setAvailability()

    }

    override fun fetchData() {

        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
            return
        }

        fetchIsSeatReservationAvailableObserver()

        fetchSeatLimitObserver()

    }

    override fun fetchIsSeatReservationAvailableObserver() {
        /*
            Método encargado de escuchar en tiempo real si la reservación de asientos esta habilitada
            o deshabilitada.
        */
        if (isOnline(requireContext())) {

            paramsViewModel.fetchIsSeatReservationAvailable()
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

        btn_set_seat_reservation_availability.setOnClickListener {

            selectedButton = "btn_set_availability"
            showConfirmDialog()

        }

    }

    override fun enableSeatReservation() {
        /*
            Método encargado de habilitar la reservación de asientos.
        */
        paramsViewModel.setIsSeatReservationAvailable(true)
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when (resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {
                            showMessage(getString(R.string.seat_reservation_has_been_enabled), 2)
                            hideProgressBar()
                        }

                        is Resource.Failure -> {
                            showMessage(resultEmitted.exception.message.toString(), 2)
                            hideProgressBar()
                        }

                    }

                })

    }

    override fun disableSeatReservation() {
        /*
            Método encargado de habilitar la reservación de asientos.
        */
        paramsViewModel.setIsSeatReservationAvailable(false)
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when (resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {
                            showMessage(getString(R.string.seat_reservation_has_been_disabled), 2)
                            hideProgressBar()
                        }

                        is Resource.Failure -> {
                            showMessage(resultEmitted.exception.message.toString(), 2)
                            hideProgressBar()
                        }

                    }

                })

    }

    override fun fetchSeatLimitObserver() {
        /*
            Método encargado de traer el número límite de asientos disponibles.
        */
        if (isOnline(requireContext())) {

            paramsViewModel.fetchSeatLimit()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when (resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {
                                seatLimit = resultEmitted.data.toString()
                                txt_seat_limit.setText(seatLimit)
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

    override fun updateSeatLimitObserver() {
        /*
            Método encargado de actualizar el número máximo de asientos disponibles.
        */
        if (isOnline(requireContext())) {

            val newSeatLimit = txt_seat_limit.text.toString().toInt()

            paramsViewModel.updateSeatLimit(newSeatLimit)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when (resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {
                                showMessage(getString(R.string.seat_limit_has_been_updated_successfully), 2)
                                fetchSeatLimitObserver()
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

    override fun updateSeatLimit() {

        btn_update_seat_limit.setOnClickListener {

            hideKeyboard()

            if (!isOnline(requireContext())) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            if (paramsViewModel.checkEmptySeatLimit(txt_seat_limit.text.toString())) {
                txt_seat_limit.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            selectedButton = "btn_update_seat_limit"
            showConfirmDialog()

        }

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
        progressbar_options_admin_seat_reservation.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un ProgressBar.
        */
        progressbar_options_admin_seat_reservation.visibility = View.GONE
    }

    override fun activateToggle() {
        /*
            Método encargado de activar un Toggle.
        */
        txt_enable_or_disable_seat_reservation.text = getString(R.string.disable_seat_reservation)
        btn_set_seat_reservation_availability.setImageResource(R.drawable.ic_toggle_on)
        btn_set_seat_reservation_availability.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
    }

    override fun deactivateToggle() {
        /*
            Método encargado de desactivar un Toggle.
        */
        txt_enable_or_disable_seat_reservation.text = getString(R.string.enable_seat_reservation)
        btn_set_seat_reservation_availability.setImageResource(R.drawable.ic_toggle_off)
        btn_set_seat_reservation_availability.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))
    }


    override fun showIsOnlineDialog() {
        /*
            Método encargado de mostrar el Dialog "isOnlineDialog".
        */
        isOnlineDialog(this)
    }

    override fun isOnlineDialogPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "isOnlineDialog".
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

            selectedButton.contains("btn_update_seat_limit") -> {
                message = getString(R.string.do_you_want_to_update_seat_limit)
            }

            selectedButton.contains("btn_set_availability") -> {

                message = if (isSeatReservationAvailable) {
                    getString(R.string.do_you_want_to_disable_seat_reservation)
                } else {
                    getString(R.string.do_you_want_to_enable_seat_reservation)
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

            selectedButton.contains("btn_update_seat_limit") -> {
                updateSeatLimitObserver()
            }

            selectedButton.contains("btn_set_availability") -> {

                if (isSeatReservationAvailable) {
                    disableSeatReservation()
                } else {
                    enableSeatReservation()
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