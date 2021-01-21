package com.circleappsstudio.mimisa.ui.main.seatreservation

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
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
import kotlinx.android.synthetic.main.fragment_seat_reservation.*

class SeatReservationFragment : BaseFragment(),
        UI.SeatReservation,
        UI.IsOnlineDialogClickButtonListener,
        UI.ConfirmDialogClickButtonListener,
        UI.IsSeatReservationAvailableDialogClickButtonListener {

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

    private var isAvailable = true

    private lateinit var seatNumber: String
    private lateinit var nameUser: String
    private lateinit var lastNameUser: String
    private lateinit var idNumberUser: String
    private lateinit var seatLimitNumber: String

    override fun getLayout(): Int = R.layout.fragment_seat_reservation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        fetchData()

        saveSeatReserved()

    }

    override fun fetchData() {

        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
            return
        }

        fetchIsSeatReservationAvailable()

        fetchIteratorObserver()

    }

    override fun fetchIsSeatReservationAvailable() {
        /*
            Método encargado de escuchar en tiempo real si la reservación de asientos esta habilitada
            o deshabilitada.
        */
        if (isOnline(requireContext())) {

            paramsViewModel.fetchIsSeatReservationAvailable()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                isAvailable = resultEmitted.data

                                if (!isAvailable){
                                    showIsSeatReservationAvailableDialog()
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

    override fun fetchIteratorObserver() {
        /*
            Método encargado de escuchar en tiempo real el iterador de la reserva de asientos.
        */
        if (isOnline(requireContext())){

            paramsViewModel.fetchIterator()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when (resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        seatNumber = resultEmitted.data.toString()
                        fetchSeatLimitObserver()
                    }

                    is Resource.Failure -> {
                        showMessage(resultEmitted.exception.message.toString(), 2)
                        hideProgressBar()
                    }

                }

            })

        }

    }

    override fun fetchSeatLimitObserver() {
        /*
            Método encargado de traer el número límite de asientos disponibles.
        */
        if (isOnline(requireContext())){

            paramsViewModel.fetchSeatLimit()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when (resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        seatLimitNumber = resultEmitted.data.toString()
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

    override fun saveSeatReserved() {

        btn_seat_reservation.setOnClickListener {

            hideKeyboard()

            nameUser = txt_name_seat_reservation.text.toString()
            lastNameUser = txt_lastname_seat_reservation.text.toString().trim()
            idNumberUser = txt_id_number_user_seat_reservation.text.toString()

            if (!isOnline(requireContext())) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkEmptyNameUser(nameUser)) {
                txt_name_seat_reservation.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkEmptyLastNameUser(lastNameUser)) {
                txt_lastname_seat_reservation.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkEmptyIdNumberUser(idNumberUser)){
                txt_id_number_user_seat_reservation.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkValidIdNumberUser(idNumberUser)) {
                txt_id_number_user_seat_reservation.error = getString(R.string.invalid_id_number)
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkSeatLimit(seatNumber.toInt(), seatLimitNumber.toInt())){
                showMessage(getString(R.string.there_are_not_available_seats), 2)
                goToSeatReservationMain()
                return@setOnClickListener
            }

            showConfirmDialog()

        }

    }

    override fun saveSeatReservedObserver() {
        /*
            Método encargado de reservar un asiento.
        */
        if (isOnline(requireContext())){

            seatReservationViewModel.saveSeatReserved(
                    seatNumber.toInt(),
                    nameUser,
                    lastNameUser,
                    idNumberUser).observe(viewLifecycleOwner, Observer { resultEmitted ->

                when (resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        addIteratorObserver()
                    }

                    is Resource.Failure -> {
                        showMessage(resultEmitted.exception.message.toString(), 2)
                        hideProgressBar()
                    }

                }

            })

        }

    }

    override fun addIteratorObserver() {
        /*
            Método encargado de aumentar el iterador al reservar un asiento.
        */
        if (isOnline(requireContext())){

            paramsViewModel.addIterator(seatNumber.toInt())
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when (resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {
                                showMessage(getString(R.string.seat_reserved_successfully), 2)
                                goToSeatReservationMain()
                            }

                            is Resource.Failure -> {
                                showMessage(resultEmitted.exception.message.toString(), 2)
                                hideProgressBar()
                            }

                        }

                    })

        }

    }

    override fun goToSeatReservationMain() {
        /*
            Método encargado de navegar hacia el fragment "MainSeatReservation".
        */
        navController.navigateUp()
    }

    override fun checkSeatSavedByIdNumberUserObserver() {
        /*
            Método encargado de verificar si una persona ya tiene reservado un asiento.
        */
        if (isOnline(requireContext())) {

            seatReservationViewModel.checkSeatSavedByIdNumberUser(idNumberUser)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                if (resultEmitted.data) {
                                    showMessage(getString(R.string.user_has_a_reserved_seat), 2)
                                    hideProgressBar()
                                } else {
                                    saveSeatReservedObserver()
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
        progressbar_seat_reservation.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un ProgressBar.
        */
        progressbar_seat_reservation.visibility = View.GONE
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
        if (isOnline(requireContext())){
            fetchData()
        } else {
            showIsOnlineDialog()
        }

    }

    override fun showIsSeatReservationAvailableDialog() {
        /*
            Método encargado de mostrar el Dialog "isSeatReservationAvailableDialog".
        */
        isSeatReservationAvailableDialog(this)
    }

    override fun isSeatReservationAvailablePositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "isSeatReservationAvailableDialog".
        */
        navController.navigateUp()
    }

    override fun showConfirmDialog(): AlertDialog? {
        /*
            Método encargado de mostrar el Dialog "confirmDialog".
        */
        return confirmDialog(this, getString(R.string.do_you_want_to_reserve_seat))
    }

    override fun confirmPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "confirmDialog".
        */
        checkSeatSavedByIdNumberUserObserver()
    }

    override fun confirmNegativeButtonClicked() {
        /*
            Método encargado de controlar el botón negativo del Dialog "confirmDialog".
        */
        showConfirmDialog()!!.dismiss()
    }

}