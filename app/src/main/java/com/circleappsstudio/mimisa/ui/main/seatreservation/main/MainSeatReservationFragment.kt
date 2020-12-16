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
import com.circleappsstudio.mimisa.data.datasource.seatreservation.SeatReservationDataSource
import com.circleappsstudio.mimisa.domain.seatreservation.SeatReservationRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.adapter.SeatAdapter
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactorySeatReservation
import com.circleappsstudio.mimisa.ui.viewmodel.seatreservation.SeatReservationViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_main_seat_reservation.*

class MainSeatReservationFragment : BaseFragment(), UI.SeatReservationMain, UI.IsOnlineDialogClickButtonListener {

    private lateinit var navController: NavController

    private val seatReservationViewModel by activityViewModels<SeatReservationViewModel> {
        VMFactorySeatReservation(
                SeatReservationRepository(
                        SeatReservationDataSource()
                )
        )
    }

    private lateinit var seatNumber: String
    private lateinit var seatLimitNumber: String

    override fun getLayout(): Int {
        return R.layout.fragment_main_seat_reservation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setUpRecyclerView()

        fetchData()

        goToSeatReservation()

    }

    override fun fetchData() {

        if (!isOnline(requireContext())) {
            showDialog()
            showProgressBar()
            return
        }

        fetchRegisteredSeatsByUserNameObserver()

        fetchIteratorObserver()

    }

    override fun setUpRecyclerView() {
        /*
            Método encargado de hacer el setup del RecyclerView.
        */
        rv_seat_reservation.layoutManager = LinearLayoutManager(requireContext())
        rv_seat_reservation.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

    }

    override fun fetchRegisteredSeatsByUserNameObserver() {
        /*
            Método encargado de traer todos los asientos reservados por el usuario leggeado.
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
                                    hideProgressBar()
                                    showRecyclerView()

                                } else {
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

    override fun goToSeatReservation() {
        /*
            Método encargado de navegar hacia el fragment de reservación de asientos.
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

    override fun showNoSeatsAvailable() {
        /*
             Método encargado de mostrar el layout de que no hay asientos disponibles.
        */
        layout_no_seats_available.visibility = View.VISIBLE
    }

    override fun hideButton() {
        /*
             Método encargado de ocultar un Button.
        */
        btn_go_to_seat_reservation.visibility = View.GONE
    }

    override fun fetchIteratorObserver() {
        /*
            Método encargado de escuchar en tiempo real el iterador de la reserva de asientos.
        */
        if (isOnline(requireContext())) {

            seatReservationViewModel.fetchIterator()
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

    override fun fetchSeatLimitObserver() {
        /*
            Método encargado de traer el número límite de asientos disponibles.
        */
        if (isOnline(requireContext())) {

            seatReservationViewModel.fetchSeatLimit()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted){

                    is Resource.Loading -> { showProgressBar() }

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
        if (seatReservationViewModel.checkSeatLimit(seatNumber.toInt(), seatLimitNumber.toInt())){
            showNoSeatsAvailable()
            hideButton()
        }

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

    override fun showDialog() {
        isOnlineDialog(this)
    }

    override fun onPositiveButtonClicked() {

        if (isOnline(requireContext())) {
            fetchData()
        } else {
            showDialog()
        }

    }

}