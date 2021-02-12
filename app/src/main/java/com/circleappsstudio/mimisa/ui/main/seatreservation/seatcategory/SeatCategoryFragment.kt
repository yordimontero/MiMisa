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

    }

    override fun goToCoupleSeat() {

        btn_couple_seat_category.setOnClickListener {

            navController.navigate(R.id.allSeatCouplesFragment)

        }

    }

    override fun goToThreesomeSeat() {

        val bundle = Bundle()

        bundle.putString("seatCategory", "couple")

        btn_threesome_seat_category.setOnClickListener {

            //navController.navigate(, bundle)

        }

    }

    override fun goToBubbleSeat() {

        val bundle = Bundle()

        bundle.putString("seatCategory", "couple")

        btn_bubble_seat_category.setOnClickListener {

            //navController.navigate(, bundle)

        }

    }

}