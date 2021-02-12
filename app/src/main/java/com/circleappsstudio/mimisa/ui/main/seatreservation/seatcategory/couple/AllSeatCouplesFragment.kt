package com.circleappsstudio.mimisa.ui.main.seatreservation.seatcategory.couple

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_all_seat_couples.*

class AllSeatCouplesFragment : BaseFragment() {

    private lateinit var navController: NavController

    private lateinit var bundle: Bundle

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

    fun goToCouple1() {

        bundle = Bundle()

        btn_couple_1.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("19", "20"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple2() {

        bundle = Bundle()

        btn_couple_2.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("21", "22"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple3() {

        bundle = Bundle()

        btn_couple_3.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("26", "27"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple4() {

        bundle = Bundle()

        btn_couple_4.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("28", "29"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple5() {

        bundle = Bundle()

        btn_couple_5.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("33", "34"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple6() {

        bundle = Bundle()

        btn_couple_6.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("35", "36"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple7() {

        bundle = Bundle()

        btn_couple_7.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("40", "41"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple8() {

        bundle = Bundle()

        btn_couple_8.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("42", "43"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple9() {

        bundle = Bundle()

        btn_couple_9.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("44", "45"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple10() {

        bundle = Bundle()

        btn_couple_10.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("46", "47"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple11() {

        bundle = Bundle()

        btn_couple_11.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("51", "52"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple12() {

        bundle = Bundle()

        btn_couple_12.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("53", "54"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple13() {

        bundle = Bundle()

        btn_couple_13.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("58", "59"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple14() {

        bundle = Bundle()

        btn_couple_14.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("60", "61"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple15() {

        bundle = Bundle()

        btn_couple_15.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("65", "66"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple16() {

        bundle = Bundle()

        btn_couple_16.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("67", "68"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple17() {

        bundle = Bundle()

        btn_couple_17.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("72", "73"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple18() {

        bundle = Bundle()

        btn_couple_18.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("74", "75"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple19() {

        bundle = Bundle()

        btn_couple_19.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("79", "80"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple20() {

        bundle = Bundle()

        btn_couple_20.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("81", "82"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple21() {

        bundle = Bundle()

        btn_couple_21.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("86", "87"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple22() {

        bundle = Bundle()

        btn_couple_22.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("88", "89"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

    fun goToCouple23() {

        bundle = Bundle()

        btn_couple_23.setOnClickListener {

            bundle.putStringArrayList("coupleSeats", arrayListOf("93", "94"))
            navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)

        }

    }

}