package com.circleappsstudio.mimisa.ui.main.seatreservation.seatcategory.couple

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_all_seat_couples.*

class AllSeatCouplesFragment : BaseFragment() {

    private lateinit var navController: NavController

    private lateinit var bundle: Bundle



    private val db by lazy { FirebaseFirestore.getInstance() }



    override fun getLayout(): Int = R.layout.fragment_all_seat_couples

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        callAllToGo()

        checkCouples()

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

       db.collection("diaconia")
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

                }

    }

    fun goToCouple1() {

        bundle = Bundle()

        btn_couple_1.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_1", "19", "20"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple2() {

        bundle = Bundle()

        btn_couple_2.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_2", "21", "22"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple3() {

        bundle = Bundle()

        btn_couple_3.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_3", "26", "27"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple4() {

        bundle = Bundle()

        btn_couple_4.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_4", "28", "29"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple5() {

        bundle = Bundle()

        btn_couple_5.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_5", "33", "34"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple6() {

        bundle = Bundle()

        btn_couple_6.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_6", "35", "36"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple7() {

        bundle = Bundle()

        btn_couple_7.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_7", "40", "41"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple8() {

        bundle = Bundle()

        btn_couple_8.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_8", "42", "43"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple9() {

        bundle = Bundle()

        btn_couple_9.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_9", "44", "45"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple10() {

        bundle = Bundle()

        btn_couple_10.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_10", "46", "47"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple11() {

        bundle = Bundle()

        btn_couple_11.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_11", "51", "52"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple12() {

        bundle = Bundle()

        btn_couple_12.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_12", "53", "54"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple13() {

        bundle = Bundle()

        btn_couple_13.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_13", "58", "59"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple14() {

        bundle = Bundle()

        btn_couple_14.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_14", "60", "61"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple15() {

        bundle = Bundle()

        btn_couple_15.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_15", "65", "66"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple16() {

        bundle = Bundle()

        btn_couple_16.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_16", "67", "68"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple17() {

        bundle = Bundle()

        btn_couple_17.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_17", "72", "73"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple18() {

        bundle = Bundle()

        btn_couple_18.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_18", "74", "75"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple19() {

        bundle = Bundle()

        btn_couple_19.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_19", "79", "80"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple20() {

        bundle = Bundle()

        btn_couple_20.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_20", "81", "82"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple21() {

        bundle = Bundle()

        btn_couple_21.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_21", "86", "87"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple22() {

        bundle = Bundle()

        btn_couple_22.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_22", "88", "89"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple23() {

        bundle = Bundle()

        btn_couple_23.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("couple_23", "93", "94"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

}