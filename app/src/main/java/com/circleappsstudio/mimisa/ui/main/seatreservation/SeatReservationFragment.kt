package com.circleappsstudio.mimisa.ui.main.seatreservation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.seatreservation.SeatReservationDataSource
import com.circleappsstudio.mimisa.domain.seatreservation.SeatReservationRepo
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactorySeatReservation
import com.circleappsstudio.mimisa.ui.viewmodel.seatreservation.SeatReservationViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_seat_reservation.*

class SeatReservationFragment : BaseFragment(), UI.SeatReservation {

    private lateinit var navController: NavController

    private lateinit var seatNumber: String
    private lateinit var nameUser: String
    private lateinit var idNumberUser: String

    private val seatReservationViewModel by activityViewModels<SeatReservationViewModel> {
        VMFactorySeatReservation(
                SeatReservationRepo(
                      SeatReservationDataSource()
                )
        )
    }

    override fun getLayout(): Int {
        return R.layout.fragment_seat_reservation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        observeFetchIterator()

        btn_seat_reservation.setOnClickListener {
            saveSeatReserved()
        }

    }

    override fun observeFetchIterator() {

        seatReservationViewModel.fetchIterator().observe(viewLifecycleOwner, Observer { resultEmmited ->

            when(resultEmmited){
                is Resource.Loading -> {
                    requireContext().toast(requireContext(), "Loading...")
                }
                is Resource.Success -> {
                    txt_iterator.text = resultEmmited.data.toString()
                }
                is Resource.Failure -> {
                    requireContext().toast(requireContext(), "Failure: ${resultEmmited.exception}")
                }
            }

        })

    }

    override fun saveSeatReserved() {

        seatNumber = txt_iterator.text.toString()
        nameUser = txt_fullname_seat_reservation.text.toString()
        idNumberUser = txt_id_number_user_seat_reservation.text.toString()

        if (!isOnline(requireContext())){
            showMessage("No hay conexión a Internet.", 2)
            return
        }

        if (seatReservationViewModel.checkEmptyFieldsForSeatReservation(nameUser, idNumberUser)){
            txt_fullname_seat_reservation.error = "Complete los campos."
            txt_id_number_user_seat_reservation.error = "Complete los campos."
            return
        }

        if (seatReservationViewModel.checkValidIdNumberUser(idNumberUser)){
            txt_id_number_user_seat_reservation.error = "Número de cédula inválido."
            return
        }

        saveSeatReservedObserver()

    }

    override fun saveSeatReservedObserver() {

        seatReservationViewModel.saveSeatReserved(seatNumber.toInt(), nameUser, idNumberUser).observe(viewLifecycleOwner, Observer { resultEmitted ->

            when(resultEmitted){

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

    override fun addIteratorObserver() {

        seatReservationViewModel.addIterator(seatNumber.toInt()).observe(viewLifecycleOwner,
            Observer { resultEmitted ->

                when (resultEmitted) {

                    is Resource.Loading -> {
                        Log.e("TAG", "observeAddIterator: Loading")
                    }

                    is Resource.Success -> {
                        showMessage("Asiento número $seatNumber reservado con éxito.", 1)
                        hideProgressBar()
                    }

                    is Resource.Failure -> {
                        Log.e("TAG", "observeAddIterator: Failure")
                    }

                }

            })

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

}