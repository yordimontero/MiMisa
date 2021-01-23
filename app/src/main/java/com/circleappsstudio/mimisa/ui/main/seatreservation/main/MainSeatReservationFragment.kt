package com.circleappsstudio.mimisa.ui.main.seatreservation.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.params.ParamsDataSource
import com.circleappsstudio.mimisa.data.datasource.seatreservation.SeatReservationDataSource
import com.circleappsstudio.mimisa.domain.params.ParamsRepository
import com.circleappsstudio.mimisa.domain.seatreservation.SeatReservationRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.adapter.SeatAdapter
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryParams
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactorySeatReservation
import com.circleappsstudio.mimisa.ui.viewmodel.params.ParamsViewModel
import com.circleappsstudio.mimisa.ui.viewmodel.seatreservation.SeatReservationViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_main_seat_reservation.*

class MainSeatReservationFragment : BaseFragment(),
        UI.SeatReservationMain,
        UI.IsOnlineDialogClickButtonListener {

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

    private lateinit var seatNumber: String
    private lateinit var seatLimitNumber: String

    private var isSeatReservationAvailable = true

    override fun getLayout(): Int = R.layout.fragment_main_seat_reservation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setUpRecyclerView()

        fetchData()

        goToSeatReservation()

    }

    override fun fetchData() {

        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
            return
        }

        fetchRegisteredSeatsByUserNameObserver()

        fetchIteratorObserver()

        fetchIsSeatReservationAvailable()

    }

    override fun fetchRegisteredSeatsByUserNameObserver() {
        /*
            Método encargado de traer todos los asientos reservados por el usuario actual registrado.
        */
        if (isOnline(requireContext())) {

            seatReservationViewModel.fetchRegisteredSeatsByNameUser()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted){

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                if (resultEmitted.data.isNotEmpty()) {

                                    rv_seat_reservation.adapter = SeatAdapter(
                                            requireContext(),
                                            resultEmitted.data
                                    )

                                    hideNoRegisteredSeatsYetMessage()
                                    hideProgressBar()
                                    showRecyclerView()

                                } else {

                                    showNoRegisteredSeatsYetMessage()
                                    hideRecyclerView()
                                    hideProgressBar()

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
        if (isOnline(requireContext())) {

            paramsViewModel.fetchIterator()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted){

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

                                isSeatReservationAvailable = resultEmitted.data

                                if (!isSeatReservationAvailable) {

                                    showInfoMessage()
                                    changeTextViewToDisabledSeatReservation()
                                    hideButton()

                                } else {

                                    hideInfoMessage()
                                    showButton()

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

    override fun fetchSeatLimitObserver() {
        /*
            Método encargado de traer el número límite de asientos disponibles.
        */
        if (isOnline(requireContext())) {

            paramsViewModel.fetchSeatLimit()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted){

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {
                                seatLimitNumber = resultEmitted.data.toString()
                                checkAvailableSeats()
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

    override fun checkAvailableSeats() {
        /*
            Método encargado de verificar si hay asientos disponibles.
        */
        if (seatReservationViewModel.checkSeatLimit(seatNumber.toInt(), seatLimitNumber.toInt())) {

            showInfoMessage()
            changeTextViewToNoSeatsAvailable()
            hideButton()

        }

    }

    override fun goToSeatReservation() {
        /*
            Método encargado de navegar hacia el fragment "SeatReservation".
        */
        btn_go_to_seat_reservation.setOnClickListener {
            navController.navigate(R.id.fragmentSeatReservation)
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
        progressbar_main_seat_reservation.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
             Método encargado de ocultar un ProgressBar.
        */
        progressbar_main_seat_reservation.visibility = View.GONE
    }

    override fun setUpRecyclerView() {
        /*
            Método encargado de hacer el setup del RecyclerView.
        */
        rv_seat_reservation.layoutManager = LinearLayoutManager(requireContext())
        rv_seat_reservation.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

    }

    override fun showRecyclerView() {
        /*
            Método encargado de mostrar un RecyclerView.
        */
        layout_rv_seat_reservation.visibility = View.VISIBLE
    }

    override fun hideRecyclerView() {
        /*
            Método encargado de ocultar un RecyclerView.
        */
        layout_rv_seat_reservation.visibility = View.GONE
    }

    override fun showInfoMessage() {
        /*
             Método encargado de mostrar un mensaje de informción.
        */
        layout_show_info_message_main_seat_reservation.visibility = View.VISIBLE
    }

    override fun hideInfoMessage() {
        /*
             Método encargado de ocultar un mensaje de informción.
        */
        layout_show_info_message_main_seat_reservation.visibility = View.GONE
    }

    override fun changeTextViewToNoSeatsAvailable() {
        /*
             Método encargado de cambiar el texto de un TextView cuando no hay asientos disponibles.
        */
        txt_info_message_main_seat_reservation.text = getString(R.string.there_are_not_available_seats)
    }

    override fun changeTextViewToDisabledSeatReservation() {
        /*
             Método encargado de cambiar el texto de un TextView cuando
             la reservación de asientos está deshabilitada.
        */
        txt_info_message_main_seat_reservation.text = getString(R.string.seat_reservation_is_disabled)
    }

    override fun showNoRegisteredSeatsYetMessage() {
        /*
             Método encargado de mostrar un mensaje cuando no se ha registrado ningún asiento.
        */
        layout_no_registered_seats_yet.visibility = View.VISIBLE
    }

    override fun hideNoRegisteredSeatsYetMessage() {
        /*
             Método encargado de ocultar un mensaje cuando no se ha registrado ningún asiento.
        */
        layout_no_registered_seats_yet.visibility = View.GONE
    }

    override fun showButton() {
        /*
             Método encargado de mostrar un Button.
        */
        btn_go_to_seat_reservation.visibility = View.VISIBLE
    }

    override fun hideButton() {
        /*
             Método encargado de ocultar un Button.
        */
        btn_go_to_seat_reservation.visibility = View.GONE
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

}