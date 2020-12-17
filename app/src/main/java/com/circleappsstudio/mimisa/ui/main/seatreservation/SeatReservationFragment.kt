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
import com.circleappsstudio.mimisa.data.datasource.seatreservation.SeatReservationDataSource
import com.circleappsstudio.mimisa.domain.seatreservation.SeatReservationRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactorySeatReservation
import com.circleappsstudio.mimisa.ui.viewmodel.seatreservation.SeatReservationViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_seat_reservation.*

class SeatReservationFragment : BaseFragment(),
        UI.SeatReservation,
        UI.IsOnlineDialogClickButtonListener,
        UI.ConfirmDialogClickButtonListener {

    private lateinit var navController: NavController

    private val seatReservationViewModel by activityViewModels<SeatReservationViewModel> {
        VMFactorySeatReservation(
                SeatReservationRepository(
                        SeatReservationDataSource()
                )
        )
    }

    private lateinit var seatNumber: String
    private lateinit var nameUser: String
    private lateinit var idNumberUser: String
    private lateinit var seatLimitNumber: String

    override fun getLayout(): Int {
        return R.layout.fragment_seat_reservation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        fetchIteratorObserver()

        /*btn_seat_reservation.setOnClickListener {
            checkSeatSavedByIdNumberUserObserver()
        }*/

        checkSeatSavedByIdNumberUserObserver()

    }

    override fun fetchIteratorObserver() {
        /*
            Método encargado de escuchar en tiempo real el iterador de la reserva de asientos.
        */
        if (isOnline(requireContext())){

            seatReservationViewModel.fetchIterator()
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

            seatReservationViewModel.fetchSeatLimit()
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

    override fun saveSeatReservedObserver() {
        /*
            Método encargado de reservar un asiento.
        */
        if (isOnline(requireContext())){

            seatReservationViewModel.saveSeatReserved(
                    seatNumber.toInt(),
                    nameUser,
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

            seatReservationViewModel.addIterator(seatNumber.toInt())
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when (resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {
                                showMessage("Asiento reservado con éxito.", 1)
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
            Método encargado de navegar hacia el fragment inicio de reservación de asientos.
        */
        navController.navigateUp()
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
            Método encargado de mostrar un Dialog.
        */
        isOnlineDialog(this)

    }

    override fun showConfirmDialog(): AlertDialog? {
        return confirmDialog(this, "¿Desea reservar el asiento?")
    }

    override fun isOnlineDialogPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog.
        */
        if (isOnline(requireContext())){
            fetchIteratorObserver()
        } else {
            showIsOnlineDialog()
        }

    }

    override fun confirmPositiveButtonClicked() {
        checkSeatSavedByIdNumberUser()
    }

    override fun confirmNegativeButtonClicked() {
        showConfirmDialog()!!.dismiss()
    }

    override fun checkSeatSavedByIdNumberUserObserver() {
        /*
            Método encargado de verificar si una persona ya tiene reservado un asiento.
        */

        btn_seat_reservation.setOnClickListener {

            nameUser = txt_fullname_seat_reservation.text.toString()
            idNumberUser = txt_id_number_user_seat_reservation.text.toString()

            if (!isOnline(requireContext())) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkEmptyNameUser(nameUser)) {
                txt_fullname_seat_reservation.error = "Complete los campos."
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkEmptyIdNumberUser(idNumberUser)){
                txt_id_number_user_seat_reservation.error = "Complete los campos."
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkValidIdNumberUser(idNumberUser)) {
                txt_id_number_user_seat_reservation.error = "Número de cédula inválido."
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkSeatLimit(seatNumber.toInt(), seatLimitNumber.toInt())){
                showMessage("Ya no hay asientos disponibles.", 2)
                goToSeatReservationMain()
                return@setOnClickListener
            }

            showConfirmDialog()

        }

    }

    override fun checkSeatSavedByIdNumberUser() {
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
                                    showMessage("El usuario que va a reservar el asiento ya tiene un asiento reservado.", 2)
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

}