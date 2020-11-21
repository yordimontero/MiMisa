package com.circleappsstudio.mimisa.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.ui.UI
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), UI.Home {

    private lateinit var navController: NavController

    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        goToSeatReservation()

    }

    override fun goToSeatReservation() {
        btn_go_to_seat_reservation.setOnClickListener {
            navController.navigate(R.id.action_navigation_home_to_seatReservationFragment)
        }
    }

}