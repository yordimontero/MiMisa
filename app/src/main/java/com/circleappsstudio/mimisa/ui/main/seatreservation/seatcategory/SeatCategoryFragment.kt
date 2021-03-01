package com.circleappsstudio.mimisa.ui.main.seatreservation.seatcategory

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.ui.UI
import kotlinx.android.synthetic.main.fragment_seat_category.*

class SeatCategoryFragment : BaseFragment(),
    UI.SeatCategory {

    private lateinit var navController: NavController

    override fun getLayout(): Int = R.layout.fragment_seat_category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        goToCoupleSeat()

        goToThreesomeSeat()

    }

    override fun goToCoupleSeat() {

        btn_couple_seat_category.setOnClickListener {

            navController.navigate(R.id.allSeatCouplesFragment)

        }

    }

    override fun goToThreesomeSeat() {

        btn_threesome_seat_category.setOnClickListener {

            navController.navigate(R.id.action_go_to_all_threesome_seats_from_seat_category_fragment)

        }

    }

    override fun goToBubbleSeat() {

        btn_bubble_seat_category.setOnClickListener {

            //navController.navigate(, bundle)

        }

    }

}