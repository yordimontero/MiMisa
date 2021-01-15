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
import com.circleappsstudio.mimisa.data.datasource.seatreservation.SeatReservationDataSource
import com.circleappsstudio.mimisa.domain.params.ParamsRepository
import com.circleappsstudio.mimisa.domain.seatreservation.SeatReservationRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryParams
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactorySeatReservation
import com.circleappsstudio.mimisa.ui.viewmodel.params.ParamsViewModel
import com.circleappsstudio.mimisa.ui.viewmodel.seatreservation.SeatReservationViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_options_admin_seat_reservation.*

class OptionsAdminSeatReservationFragment : BaseFragment(),
        UI.OptionsAdminSeatReservation,
        UI.IsOnlineDialogClickButtonListener,
        UI.ConfirmDialogClickButtonListener,
        UI.IsAvailableDialogClickButtonListener {

    private lateinit var navController: NavController

    private val seatReservationViewModel by activityViewModels<SeatReservationViewModel> {
        VMFactorySeatReservation(
                SeatReservationRepository(
                        SeatReservationDataSource()
                )
        )
    }

    private val paramsViewModel by activityViewModels<ParamsViewModel> {
        VMFactoryParams(
                ParamsRepository(
                        ParamsDataSource()
                )
        )
    }

    private lateinit var seatLimit: String

    private var isAvailable = true

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
            showProgressBar()
            return
        }

        fetchSeatLimitObserver()

        fetchIsAvailable()

    }

    override fun fetchIsAvailable() {
        /*

        */
        if (isOnline(requireContext())) {

            paramsViewModel.fetchIsAvailable()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when (resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                isAvailable = resultEmitted.data

                                if (isAvailable) {
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

            selectedButton = "btn_set_availability"
            showConfirmDialog()

        }

    }

    override fun activateToggle() {
        txt_enable_or_disable_seat_reservation.text = "Deshabilitar la reservación de asientos."
        btn_set_availability.setImageResource(R.drawable.ic_toggle_on)
        btn_set_availability.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
    }

    override fun deactivateToggle() {
        txt_enable_or_disable_seat_reservation.text = "Habilitar la reservación de asientos."
        btn_set_availability.setImageResource(R.drawable.ic_toggle_off)
        btn_set_availability.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))
    }

    override fun enableSystem() {
        /*
            Método encargado de desbloquear el funcionamiento del app.
        */
        paramsViewModel.setIsAvailable(true)
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when (resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {
                            showMessage("La reservación de asientos está habilitada.", 1)
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
        /*
            Método encargado de bloquear el funcionamiento del app.
        */
        paramsViewModel.setIsAvailable(false)
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when (resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {
                            showMessage("La reservación de asientos está deshabilitada.", 1)
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

            seatReservationViewModel.fetchSeatLimit()
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

    override fun updateSeatLimit() {
        /*
            Método encargado de actualizar el número máximo de asientos disponibles.
        */

        btn_update_seat_limit.setOnClickListener {

            selectedButton = "btn_update_seat_limit"
            hideKeyboard()
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

    override fun showIsOnlineDialog() {
        /*
            Método encargado de mostrar el Dialog "isOnlineDialog".
        */
        isOnlineDialog(this)
    }

    override fun showConfirmDialog(): AlertDialog? {

        var message = ""

        when {

            selectedButton.contains("btn_update_seat_limit") -> {
                message = "¿Actualizar el límite de asientos disponibles?"
            }

            selectedButton.contains("btn_set_availability") -> {

                message = if (isAvailable) {
                    "¿Deshabilitar la reservación de asientos?"
                } else {
                    "¿Habilitar la reservación de asientos?"
                }

            }

        }

        return confirmDialog(this, message)

    }

    override fun isOnlineDialogPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog.
        */
        if (isOnline(requireContext())) {
            fetchData()
        } else {
            showIsOnlineDialog()
        }

    }

    override fun isAvailablePositiveButtonClicked() {
        requireActivity().finish()
    }

    override fun confirmPositiveButtonClicked() {

        when {

            selectedButton.contains("btn_update_seat_limit") -> {

                if (isOnline(requireContext())) {

                    val newSeatLimit = txt_seat_limit.text.toString().toInt()

                    seatReservationViewModel.updateSeatLimit(newSeatLimit)
                            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                                when (resultEmitted) {

                                    is Resource.Loading -> {
                                        showProgressBar()
                                    }

                                    is Resource.Success -> {
                                        showMessage("El límite de asientos fue actualizado con éxito.", 2)
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

            selectedButton.contains("btn_set_availability") -> {

                if (isAvailable) {
                    disableSystem()
                } else {
                    enableSystem()
                }

            }

        }

    }

    override fun confirmNegativeButtonClicked() {
        showConfirmDialog()!!.dismiss()
    }

}