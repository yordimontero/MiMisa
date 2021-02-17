package com.circleappsstudio.mimisa.ui.main.seatreservation.seatcategory.couple

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.seatreservation.SeatReservationDataSource
import com.circleappsstudio.mimisa.domain.seatreservation.SeatReservationRepository
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactorySeatReservation
import com.circleappsstudio.mimisa.ui.viewmodel.seatreservation.SeatReservationViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_all_seat_couples.*

class AllSeatCouplesFragment : BaseFragment() {

    private lateinit var navController: NavController

    private val seatReservationViewModel by activityViewModels<SeatReservationViewModel> {
        VMFactorySeatReservation(
                SeatReservationRepository(
                        SeatReservationDataSource()
                )
        )
    }

    private val bundle by lazy { Bundle() }

    private lateinit var coupleNumber: String

    override fun getLayout(): Int = R.layout.fragment_all_seat_couples

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        callAllToGo()

    }

    fun callAllToGo() {

        goToCouple1()
        goToCouple2()
        goToCouple3()
        goToCouple4()
        goToCouple5()
        goToCouple6()
        goToCouple7()
        goToCouple8()
        goToCouple9()
        goToCouple10()
        goToCouple11()
        goToCouple12()
        goToCouple13()
        goToCouple14()
        goToCouple15()
        goToCouple16()
        goToCouple17()
        goToCouple18()
        goToCouple19()
        goToCouple20()
        goToCouple21()
        goToCouple22()
        goToCouple23()

    }

    fun checkCouples() {

        seatReservationViewModel.checkCouples(coupleNumber)
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when (resultEmitted) {

                        is Resource.Loading -> {

                            Toast.makeText(requireContext(),
                                    "Loading...",
                                    Toast.LENGTH_SHORT)
                                    .show()

                        }

                        is Resource.Success -> {

                            if (resultEmitted.data) {

                                navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

                            } else {

                                Toast.makeText(requireContext(),
                                        "La pareja seleccionada no está disponible.",
                                        Toast.LENGTH_SHORT)
                                        .show()

                            }

                        }

                        is Resource.Failure -> {

                            Toast.makeText(requireContext(),
                                    resultEmitted.exception.message.toString(),
                                    Toast.LENGTH_SHORT)
                                    .show()

                        }

                    }

                })

       /*db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("data")
                .collection("couples")
                .get().addOnSuccessListener { documents ->

                    for (document in documents) {

                        if (document.exists()) {

                            if (document.data["isAvailable"] == false) {

                                when (document.id) {

                                    "couple_1" -> {
                                        btn_couple_1.visibility = View.GONE
                                    }

                                    "couple_2" -> {
                                        btn_couple_2.visibility = View.GONE
                                    }

                                    "couple_3" -> {
                                        btn_couple_3.visibility = View.GONE
                                    }

                                    "couple_4" -> {
                                        btn_couple_4.visibility = View.GONE
                                    }

                                    "couple_5" -> {
                                        btn_couple_5.visibility = View.GONE
                                    }

                                    "couple_6" -> {
                                        btn_couple_6.visibility = View.GONE
                                    }

                                    "couple_7" -> {
                                        btn_couple_7.visibility = View.GONE
                                    }

                                    "couple_8" -> {
                                        btn_couple_8.visibility = View.GONE
                                    }

                                    "couple_9" -> {
                                        btn_couple_9.visibility = View.GONE
                                    }

                                    "couple_10" -> {
                                        btn_couple_10.visibility = View.GONE
                                    }

                                    "couple_11" -> {
                                        btn_couple_11.visibility = View.GONE
                                    }

                                    "couple_12" -> {
                                        btn_couple_12.visibility = View.GONE
                                    }

                                    "couple_13" -> {
                                        btn_couple_13.visibility = View.GONE
                                    }

                                    "couple_14" -> {
                                        btn_couple_14.visibility = View.GONE
                                    }

                                    "couple_15" -> {
                                        btn_couple_15.visibility = View.GONE
                                    }

                                    "couple_16" -> {
                                        btn_couple_16.visibility = View.GONE
                                    }

                                    "couple_17" -> {
                                        btn_couple_17.visibility = View.GONE
                                    }

                                    "couple_18" -> {
                                        btn_couple_18.visibility = View.GONE
                                    }

                                    "couple_19" -> {
                                        btn_couple_19.visibility = View.GONE
                                    }

                                    "couple_20" -> {
                                        btn_couple_20.visibility = View.GONE
                                    }

                                    "couple_21" -> {
                                        btn_couple_21.visibility = View.GONE
                                    }

                                    "couple_22" -> {
                                        btn_couple_22.visibility = View.GONE
                                    }

                                    "couple_23" -> {
                                        btn_couple_23.visibility = View.GONE
                                    }

                                }

                            }

                        }

                    }

                }*/

    }

    fun goToCouple1() {

        btn_couple_1.setOnClickListener {

            coupleNumber = "couple_1"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "19", "20"))
            checkCouples()

        }

    }

    fun goToCouple2() {

        btn_couple_2.setOnClickListener {

            coupleNumber = "couple_2"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "21", "22"))
            checkCouples()

        }

    }

    fun goToCouple3() {

        btn_couple_3.setOnClickListener {

            coupleNumber = "couple_3"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "26", "27"))
            checkCouples()

        }

    }

    fun goToCouple4() {

        btn_couple_4.setOnClickListener {

            coupleNumber = "couple_4"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "28", "29"))
            checkCouples()

        }

    }

    fun goToCouple5() {

        btn_couple_5.setOnClickListener {

            coupleNumber = "couple_5"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "33", "34"))
            checkCouples()

        }

    }

    fun goToCouple6() {

        btn_couple_6.setOnClickListener {

            coupleNumber = "couple_6"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "35", "36"))
            checkCouples()

        }

    }

    fun goToCouple7() {

        btn_couple_7.setOnClickListener {

            coupleNumber = "couple_7"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "40", "41"))
            checkCouples()

        }

    }

    fun goToCouple8() {

        btn_couple_8.setOnClickListener {

            coupleNumber = "couple_8"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "42", "43"))
            checkCouples()

        }

    }

    fun goToCouple9() {

        btn_couple_9.setOnClickListener {

            coupleNumber = "couple_9"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "44", "45"))
            checkCouples()

        }

    }

    fun goToCouple10() {

        btn_couple_10.setOnClickListener {

            coupleNumber = "couple_10"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "46", "47"))
            checkCouples()

        }

    }

    fun goToCouple11() {

        btn_couple_11.setOnClickListener {

            coupleNumber = "couple_11"
            bundle.putStringArrayList(coupleNumber, arrayListOf("couple_11", "51", "52"))
            checkCouples()

        }

    }

    fun goToCouple12() {

        btn_couple_12.setOnClickListener {

            coupleNumber = "couple_12"
            bundle.putStringArrayList(coupleNumber, arrayListOf("couple_12", "53", "54"))
            checkCouples()

        }

    }

    fun goToCouple13() {

        btn_couple_13.setOnClickListener {

            coupleNumber = "couple_13"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "58", "59"))
            checkCouples()

        }

    }

    fun goToCouple14() {

        btn_couple_14.setOnClickListener {

            coupleNumber = "couple_14"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "60", "61"))
            checkCouples()

        }

    }

    fun goToCouple15() {

        btn_couple_15.setOnClickListener {

            coupleNumber = "couple_15"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "65", "66"))
            checkCouples()

        }

    }

    fun goToCouple16() {

        btn_couple_16.setOnClickListener {

            coupleNumber = "couple_16"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "67", "68"))
            checkCouples()

        }

    }

    fun goToCouple17() {

        btn_couple_17.setOnClickListener {

            coupleNumber = "couple_17"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "72", "73"))
            checkCouples()

        }

    }

    fun goToCouple18() {

        btn_couple_18.setOnClickListener {

            coupleNumber = "couple_18"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "74", "75"))
            checkCouples()

        }

    }

    fun goToCouple19() {

        btn_couple_19.setOnClickListener {

            coupleNumber = "couple_19"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "79", "80"))
            checkCouples()

        }

    }

    fun goToCouple20() {

        btn_couple_20.setOnClickListener {

            coupleNumber = "couple_20"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "81", "82"))
            checkCouples()

        }

    }

    fun goToCouple21() {

        btn_couple_21.setOnClickListener {

            coupleNumber = "couple_21"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "86", "87"))
            checkCouples()

        }

    }

    fun goToCouple22() {

        btn_couple_22.setOnClickListener {

            coupleNumber = "couple_22"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "88", "89"))
            checkCouples()

        }

    }

    fun goToCouple23() {

        btn_couple_23.setOnClickListener {

            coupleNumber = "couple_23"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "93", "94"))
            checkCouples()

        }

    }

}