package com.circleappsstudio.mimisa.ui.main.seatreservation.seatcategory.couple

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_couple_seat_category.*

class CoupleSeatCategoryFragment : BaseFragment() {

    private lateinit var navController: NavController

    private lateinit var getSeats: ArrayList<String>
    private lateinit var seat1: String
    private lateinit var seat2: String

    override fun getLayout(): Int = R.layout.fragment_couple_seat_category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        requireArguments().let {
            getSeats = it.getStringArrayList("coupleSeats")!!
            seat1 = getSeats[0]
            seat2 = getSeats[1]
        }

        showSeatNumber()

    }

    fun showSeatNumber() {

        txt_couple_1_seat_number.text = seat1
        txt_couple_2_seat_number.text = seat2

    }

}