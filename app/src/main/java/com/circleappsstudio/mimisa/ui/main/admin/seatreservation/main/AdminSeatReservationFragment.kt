package com.circleappsstudio.mimisa.ui.main.admin.seatreservation.main

import android.os.Bundle
import android.view.View
import android.widget.SearchView
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
import kotlinx.android.synthetic.main.fragment_admin_seat_reservation.*

class AdminSeatReservationFragment : BaseFragment(),
        UI.AdminSeatReservation,
        UI.IsOnlineDialogClickButtonListener {

    private lateinit var navController: NavController

    private val seatReservationViewModel by activityViewModels<SeatReservationViewModel> {
        VMFactorySeatReservation(
            SeatReservationRepository(
                SeatReservationDataSource()
            )
        )
    }

    override fun getLayout(): Int = R.layout.fragment_admin_seat_reservation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setupRecyclerView()

        setupSearchView()

        fetchData()

        goToOptionAdminSeatReservation()

        addListenerRadioButtons()

    }

    override fun addListenerRadioButtons() {
        /*
            Método encargado de escuchar cuál RadioButton está seleccionado.
        */
        radiogroup_search_reserved_seat.setOnCheckedChangeListener { radioGroup, i ->

            when(i){

                R.id.rd_btn_search_by_name -> {}

                R.id.rd_btn_search_by_seat_number -> {}

            }

        }

    }

    override fun fetchData() {

        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
            showProgressBar()
            return
        }

        fetchSavedSeats()

    }

    override fun setupSearchView() {

        searchview.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {

                hideKeyboard()

                if (!rd_btn_search_by_name.isChecked && !rd_btn_search_by_seat_number.isChecked){
                    showMessage("Seleccione el filtro de búsqueda.", 2)
                }

                if (rd_btn_search_by_name.isChecked){
                    fetchRegisteredSeatByRegisteredPersonObserver(p0.toString())
                }

                if (rd_btn_search_by_seat_number.isChecked){

                    if (!checkIfIsLetter(p0.toString())) {
                        fetchRegisteredSeatBySeatNumberObserver(p0.toString().toInt())
                    } else {
                        showMessage("Error de búsqueda.", 2)
                    }

                }

                return false

            }

            override fun onQueryTextChange(p0: String?): Boolean {

                if (p0.toString().isEmpty()) {
                    fetchData()
                }

                return false
            }

        })

    }

    override fun goToOptionAdminSeatReservation() {

        btn_go_to_options_admin_seat_reservation.setOnClickListener {
            navController.navigate(R.id.navigation_options_admin_seat_reservation)
        }

    }

    override fun fetchRegisteredSeatByRegisteredPersonObserver(registeredPerson: String) {

        if (isOnline(requireContext())) {

            seatReservationViewModel.fetchRegisteredSeatByRegisteredPerson(registeredPerson)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                if (resultEmitted.data.isNotEmpty()) {

                                    rv_admin_seat_reservation.adapter = SeatAdapter(
                                            requireContext(),
                                            resultEmitted.data
                                    )

                                    hideNotSeatFoundedMessage()
                                    showRecyclerView()
                                    hideProgressBar()

                                } else {

                                    hideRecyclerView()
                                    showNotSeatFoundedMessage()
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

    override fun fetchRegisteredSeatBySeatNumberObserver(seatNumber: Int) {

        if (isOnline(requireContext())) {

            seatReservationViewModel.fetchRegisteredSeatBySeatNumber(seatNumber)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                if (resultEmitted.data.isNotEmpty()) {

                                    rv_admin_seat_reservation.adapter = SeatAdapter(
                                            requireContext(),
                                            resultEmitted.data
                                    )

                                    hideNotSeatFoundedMessage()
                                    showRecyclerView()
                                    hideProgressBar()

                                } else {

                                    hideRecyclerView()
                                    showNotSeatFoundedMessage()
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

    override fun setupRecyclerView() {
        /*
            Método encargado de hacer el setup del RecyclerView.
        */
        rv_admin_seat_reservation.layoutManager = LinearLayoutManager(requireContext())
        rv_admin_seat_reservation.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        rv_admin_seat_reservation.setHasFixedSize(true)
    }

    override fun fetchSavedSeats() {
        /*
            Método encargado de traer todos los asientos reservados en la base de datos.
        */

        if (isOnline(requireContext())) {

            seatReservationViewModel.fetchAllRegisteredSeats()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                if (resultEmitted.data.isNotEmpty()) {

                                    rv_admin_seat_reservation.adapter = SeatAdapter(
                                            requireContext(),
                                            resultEmitted.data
                                    )

                                    hideNotSeatFoundedMessage()
                                    showRecyclerView()
                                    hideProgressBar()

                                } else {

                                    showNotSeatFoundedMessage()
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
        progressbar_admin_seat_reservation.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un ProgressBar.
        */
        progressbar_admin_seat_reservation.visibility = View.GONE
    }

    override fun showRecyclerView() {
        /*
            Método encargado de mostrar un RecyclerView.
        */
        layout_rv_admin_seat_reservation.visibility = View.VISIBLE
    }

    override fun hideRecyclerView() {
        /*
            Método encargado de ocultar un RecyclerView.
        */
        layout_rv_admin_seat_reservation.visibility = View.GONE
    }

    override fun showNotSeatFoundedMessage() {
        /*
            Método encargado de mostrar un mensaje cuando no se encontró ningún asiento.
        */
        layout_not_registered_seat_founded.visibility = View.VISIBLE
    }

    override fun hideNotSeatFoundedMessage() {
        /*
            Método encargado de ocultar el mensaje cuando no se encontró ningún asiento.
        */
        layout_not_registered_seat_founded.visibility = View.GONE
    }

    override fun showIsOnlineDialog() {
        /*
            Método encargado de mostrar el Dialog "isOnlineDialog".
        */
        isOnlineDialog(this)
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

}