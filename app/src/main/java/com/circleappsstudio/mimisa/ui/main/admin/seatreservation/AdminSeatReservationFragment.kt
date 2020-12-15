package com.circleappsstudio.mimisa.ui.main.admin.seatreservation

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
import kotlinx.android.synthetic.main.fragment_admin_seat_reservation.*

class AdminSeatReservationFragment : BaseFragment(), UI.AdminSeatReservation {

    private lateinit var navController: NavController

    private val seatReservationViewModel by activityViewModels<SeatReservationViewModel> {
        VMFactorySeatReservation(
            SeatReservationRepository(
                SeatReservationDataSource()
            )
        )
    }

    private lateinit var seatLimit: String

    override fun getLayout(): Int = R.layout.fragment_admin_seat_reservation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setupRecyclerView()

        fetchSavedSeats()

        fetchSeatLimitObserver()

        updateSeatLimit()

    }

    override fun fetchSavedSeats() {
        /*
            Método encargado de traer todos los asientos reservados en la base de datos.
        */
        seatReservationViewModel.fetchAllRegisteredSeats()
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {

                        if (resultEmitted.data.isNotEmpty()) {

                            rv_admin_seat_reservation.adapter = SeatAdapter(requireContext(), resultEmitted.data)
                            showRecyclerView()
                            hideProgressBar()

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

    override fun fetchSeatLimitObserver() {
        /*
            Método encargado de traer el número límite de asientos disponibles.
        */
        seatReservationViewModel.fetchSeatLimit()
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when(resultEmitted) {

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

    override fun updateSeatLimit() {
        /*
            Método encargado de actualizar el número máximo de asientos disponibles.
        */

        btn_update_seat_limit.setOnClickListener {

            if (isOnline(requireContext())) {

                val newSeatLimit = txt_seat_limit.text.toString().toInt()

                seatReservationViewModel.updateSeatLimit(newSeatLimit)
                        .observe(viewLifecycleOwner, Observer { resultEmitted ->

                            when(resultEmitted) {

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

    }

    override fun setupRecyclerView() {
        /*
            Método encargado de hacer el setup del RecyclerView.
        */
        rv_admin_seat_reservation.layoutManager = LinearLayoutManager(requireContext())
        rv_admin_seat_reservation.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
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

}