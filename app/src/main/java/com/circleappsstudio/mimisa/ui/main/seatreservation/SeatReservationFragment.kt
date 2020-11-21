package com.circleappsstudio.mimisa.ui.main.seatreservation

import android.os.Bundle
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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_seat_reservation.*

class SeatReservationFragment : BaseFragment(), UI.SeatReservation {

    private lateinit var navController: NavController

    private val seatReservationView by activityViewModels<SeatReservationViewModel> {
        VMFactorySeatReservation(
                SeatReservationRepo(
                      SeatReservationDataSource()
                )
        )
    }

    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun getLayout(): Int {
        return R.layout.fragment_seat_reservation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        fetchIterator()

    }

    override fun fetchIterator() {

        seatReservationView.fetchIterator().observe(viewLifecycleOwner, Observer { resultEmmited ->

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

}