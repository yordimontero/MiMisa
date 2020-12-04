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

class SeatReservationFragment : BaseFragment(), UI.SeatReservation {

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

        btn_seat_reservation.setOnClickListener {
            saveSeatReserved()
        }

    }

    override fun fetchIteratorObserver() {

        if (isOnline(requireContext())){

            seatReservationViewModel.fetchIterator().observe(viewLifecycleOwner, Observer { resultEmitted ->

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

        } else {
            showMessage("No hay conexión a Internet", 1)
        }

    }

    override fun fetchSeatLimitObserver() {

        if (isOnline(requireContext())){

            seatReservationViewModel.fetchSeatLimit().observe(viewLifecycleOwner, Observer { resultEmitted ->

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

        } else {
            showMessage("No hay conexión a Internet.", 1)
        }

    }

    override fun saveSeatReserved() {

        nameUser = txt_fullname_seat_reservation.text.toString()
        idNumberUser = txt_id_number_user_seat_reservation.text.toString()

        if (!isOnline(requireContext())) {
            //showMessage("No hay conexión a Internet.", 2)
            showDialog()
            return
        }

        if (seatReservationViewModel.checkEmptyFieldsForSeatReservation(nameUser, idNumberUser)) {
            txt_fullname_seat_reservation.error = "Complete los campos."
            txt_id_number_user_seat_reservation.error = "Complete los campos."
            return
        }

        if (seatReservationViewModel.checkValidIdNumberUser(idNumberUser)) {
            txt_id_number_user_seat_reservation.error = "Número de cédula inválido."
            return
        }

        if (seatReservationViewModel.checkSeatLimit(seatNumber.toInt(), seatLimitNumber.toInt())){
            showMessage("Ya no hay asientos disponibles.", 2)
            goToSeatReservationMain()
            return
        }

        saveSeatReservedObserver()

    }

    override fun saveSeatReservedObserver() {

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

        } else {
            showMessage("No hay conexión a Internet.", 1)
        }

    }

    override fun addIteratorObserver() {

        if (isOnline(requireContext())){

            seatReservationViewModel.addIterator(seatNumber.toInt()).observe(viewLifecycleOwner,
                    Observer { resultEmitted ->

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

        } else {
            showMessage("No hay conexión a Internet", 1)
        }

    }

    override fun goToSeatReservationMain() {
        navController.navigateUp()
    }

    override fun showMessage(message: String, duration: Int) {
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        progressbar_seat_reservation.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressbar_seat_reservation.visibility = View.GONE
    }

    override fun showDialog() {

        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }

        builder!!.setTitle("¡No hay conexión a Internet!")
        builder.setMessage("Verifique su conexión e inténtelo de nuevo.")

        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_wifi_off)

        builder.apply {
            setPositiveButton("Intentar de nuevo") { dialog, id ->

                if (isOnline(requireContext())){
                    fetchIteratorObserver()
                } else {
                    showDialog()
                }

            }
        }

        val dialog: AlertDialog? = builder.create()

        dialog!!.show()

    }

}