package com.circleappsstudio.mimisa.ui.main.seatreservation.seatcategory.couple

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
import kotlinx.android.synthetic.main.fragment_all_seat_couples.*

class AllSeatCouplesFragment : BaseFragment(),
        UI.AllSeatCouples,
        UI.IsOnlineDialogClickButtonListener {

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

        fetchData()

        callAllToGoCouples()

    }

    override fun fetchData() {

        if(!isOnline(requireContext())) {
            showIsOnlineDialog()
            return
        }

        loadAvailableCouplesObserver()

        loadNoAvailableCouplesObserver()

    }

    override fun checkIfIsCoupleAvailable() {

        seatReservationViewModel.checkIfIsCoupleAvailable(coupleNumber)
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when (resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {

                            if (resultEmitted.data) {
                                navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)
                            } else {
                                showMessage("La pareja seleccionada no está disponible.", 2)
                                hideProgressBar()
                            }

                        }

                        is Resource.Failure -> {
                            showMessage(resultEmitted.exception.message.toString(), 2)
                            hideProgressBar()
                        }

                    }

                })

    }

    override fun loadAvailableCouplesObserver() {

        seatReservationViewModel.loadAvailableCouples()
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when(resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {
                            showAvailableCoupleTextView(resultEmitted.data)
                            hideProgressBar()
                        }

                        is Resource.Failure -> {
                            showMessage(resultEmitted.exception.message.toString(), 2)
                        }

                    }

                })

    }

    override fun loadNoAvailableCouplesObserver() {

        seatReservationViewModel.loadNoAvailableCouples()
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when(resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {
                            showNoAvailableCoupleTextView(resultEmitted.data)
                            hideProgressBar()
                        }

                        is Resource.Failure -> {
                            showMessage(resultEmitted.exception.message.toString(), 2)
                            hideProgressBar()
                        }

                    }

                })

    }

    override fun showAvailableCoupleTextView(documentId: String) {

        when (documentId) {

            "couple_1" -> {
                //txt_couple_1_no_available.visibility = View.VISIBLE
                txt_couple_1_no_available.text = "¡Disponible!"
            }

            "couple_2" -> {
                txt_couple_2_no_available.text = "¡Disponible!"
            }

            "couple_3" -> {
                txt_couple_3_no_available.text = "¡Disponible!"
            }

            "couple_4" -> {
                txt_couple_4_no_available.text = "¡Disponible!"
            }

            "couple_5" -> {
                txt_couple_5_no_available.text = "¡Disponible!"
            }

            "couple_6" -> {
                txt_couple_6_no_available.text = "¡Disponible!"
            }

            "couple_7" -> {
                txt_couple_7_no_available.text = "¡Disponible!"
            }

            "couple_8" -> {
                txt_couple_8_no_available.text = "¡Disponible!"
            }

            "couple_9" -> {
                txt_couple_9_no_available.text = "¡Disponible!"
            }

            "couple_10" -> {
                txt_couple_10_no_available.text = "¡Disponible!"
            }

            "couple_11" -> {
                txt_couple_11_no_available.text = "¡Disponible!"
            }

            "couple_12" -> {
                txt_couple_12_no_available.text = "¡Disponible!"
            }

            "couple_13" -> {
                txt_couple_13_no_available.text = "¡Disponible!"
            }

            "couple_14" -> {
                txt_couple_14_no_available.text = "¡Disponible!"
            }

            "couple_15" -> {
                txt_couple_15_no_available.text = "¡Disponible!"
            }

            "couple_16" -> {
                txt_couple_16_no_available.text = "¡Disponible!"
            }

            "couple_17" -> {
                txt_couple_17_no_available.text = "¡Disponible!"
            }

            "couple_18" -> {
                txt_couple_18_no_available.text = "¡Disponible!"
            }

            "couple_19" -> {
                txt_couple_19_no_available.text = "¡Disponible!"
            }

            "couple_20" -> {
                txt_couple_20_no_available.text = "¡Disponible!"
            }

            "couple_21" -> {
                txt_couple_21_no_available.text = "¡Disponible!"
            }

            "couple_22" -> {
                txt_couple_22_no_available.text = "¡Disponible!"
            }

            "couple_23" -> {
                txt_couple_23_no_available.text = "¡Disponible!"
            }

        }

    }

    override fun showNoAvailableCoupleTextView(documentId: String) {

        when (documentId) {

            "couple_1" -> {
                //txt_couple_1_no_available.visibility = View.GONE
                txt_couple_1_no_available.text = "¡No disponible!"
            }

            "couple_2" -> {
                txt_couple_2_no_available.text = "¡No disponible!"
            }

            "couple_3" -> {
                txt_couple_3_no_available.text = "¡No disponible!"
            }

            "couple_4" -> {
                txt_couple_4_no_available.text = "¡No disponible!"
            }

            "couple_5" -> {
                txt_couple_5_no_available.text = "¡No disponible!"
            }

            "couple_6" -> {
                txt_couple_6_no_available.text = "¡No disponible!"
            }

            "couple_7" -> {
                txt_couple_7_no_available.text = "¡No disponible!"
            }

            "couple_8" -> {
                txt_couple_8_no_available.text = "¡No disponible!"
            }

            "couple_9" -> {
                txt_couple_9_no_available.text = "¡No disponible!"
            }

            "couple_10" -> {
                txt_couple_10_no_available.text = "¡No disponible!"
            }

            "couple_11" -> {
                txt_couple_11_no_available.text = "¡No disponible!"
            }

            "couple_12" -> {
                txt_couple_12_no_available.text = "¡No disponible!"
            }

            "couple_13" -> {
                txt_couple_13_no_available.text = "¡No disponible!"
            }

            "couple_14" -> {
                txt_couple_14_no_available.text = "¡No disponible!"
            }

            "couple_15" -> {
                txt_couple_15_no_available.text = "¡No disponible!"
            }

            "couple_16" -> {
                txt_couple_16_no_available.text = "¡No disponible!"
            }

            "couple_17" -> {
                txt_couple_17_no_available.text = "¡No disponible!"
            }

            "couple_18" -> {
                txt_couple_18_no_available.text = "¡No disponible!"
            }

            "couple_19" -> {
                txt_couple_19_no_available.text = "¡No disponible!"
            }

            "couple_20" -> {
                txt_couple_20_no_available.text = "¡No disponible!"
            }

            "couple_21" -> {
                txt_couple_21_no_available.text = "¡No disponible!"
            }

            "couple_22" -> {
                txt_couple_22_no_available.text = "¡No disponible!"
            }

            "couple_23" -> {
                txt_couple_23_no_available.text = "¡No disponible!"
            }

        }

    }

    override fun callAllToGoCouples() {

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

    override fun goToCouple1() {

        btn_couple_1.setOnClickListener {

            coupleNumber = "couple_1"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "19", "20"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple2() {

        btn_couple_2.setOnClickListener {

            coupleNumber = "couple_2"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "21", "22"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple3() {

        btn_couple_3.setOnClickListener {

            coupleNumber = "couple_3"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "26", "27"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple4() {

        btn_couple_4.setOnClickListener {

            coupleNumber = "couple_4"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "28", "29"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple5() {

        btn_couple_5.setOnClickListener {

            coupleNumber = "couple_5"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "33", "34"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple6() {

        btn_couple_6.setOnClickListener {

            coupleNumber = "couple_6"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "35", "36"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple7() {

        btn_couple_7.setOnClickListener {

            coupleNumber = "couple_7"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "40", "41"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple8() {

        btn_couple_8.setOnClickListener {

            coupleNumber = "couple_8"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "42", "43"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple9() {

        btn_couple_9.setOnClickListener {

            coupleNumber = "couple_9"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "44", "45"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple10() {

        btn_couple_10.setOnClickListener {

            coupleNumber = "couple_10"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "46", "47"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple11() {

        btn_couple_11.setOnClickListener {

            coupleNumber = "couple_11"
            bundle.putStringArrayList(coupleNumber, arrayListOf("couple_11", "51", "52"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple12() {

        btn_couple_12.setOnClickListener {

            coupleNumber = "couple_12"
            bundle.putStringArrayList(coupleNumber, arrayListOf("couple_12", "53", "54"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple13() {

        btn_couple_13.setOnClickListener {

            coupleNumber = "couple_13"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "58", "59"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple14() {

        btn_couple_14.setOnClickListener {

            coupleNumber = "couple_14"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "60", "61"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple15() {

        btn_couple_15.setOnClickListener {

            coupleNumber = "couple_15"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "65", "66"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple16() {

        btn_couple_16.setOnClickListener {

            coupleNumber = "couple_16"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "67", "68"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple17() {

        btn_couple_17.setOnClickListener {

            coupleNumber = "couple_17"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "72", "73"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple18() {

        btn_couple_18.setOnClickListener {

            coupleNumber = "couple_18"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "74", "75"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple19() {

        btn_couple_19.setOnClickListener {

            coupleNumber = "couple_19"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "79", "80"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple20() {

        btn_couple_20.setOnClickListener {

            coupleNumber = "couple_20"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "81", "82"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple21() {

        btn_couple_21.setOnClickListener {

            coupleNumber = "couple_21"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "86", "87"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple22() {

        btn_couple_22.setOnClickListener {

            coupleNumber = "couple_22"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "88", "89"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple23() {

        btn_couple_23.setOnClickListener {

            coupleNumber = "couple_23"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleNumber, "93", "94"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun showMessage(message: String, duration: Int) {
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        progressbar_all_seat_couples.visibility = View.VISIBLE
        layout_all_seat_couples.visibility = View.GONE
    }

    override fun hideProgressBar() {
        progressbar_all_seat_couples.visibility = View.GONE
        layout_all_seat_couples.visibility = View.VISIBLE
    }

    override fun showIsOnlineDialog() = isOnlineDialog(this)

    override fun isOnlineDialogPositiveButtonClicked() {

        if (isOnline(requireContext())) {
            fetchData()
        } else {
            showIsOnlineDialog()
        }

    }

}