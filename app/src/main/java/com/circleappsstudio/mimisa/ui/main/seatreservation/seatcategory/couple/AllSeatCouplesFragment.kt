package com.circleappsstudio.mimisa.ui.main.seatreservation.seatcategory.couple

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.auth.AuthDataSource
import com.circleappsstudio.mimisa.data.datasource.roleuser.RoleUserDataSource
import com.circleappsstudio.mimisa.data.datasource.seatreservation.SeatReservationDataSource
import com.circleappsstudio.mimisa.domain.auth.AuthRepository
import com.circleappsstudio.mimisa.domain.roleuser.RoleUserRepository
import com.circleappsstudio.mimisa.domain.seatreservation.SeatReservationRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.viewmodel.auth.AuthViewModel
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAdmin
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryAuth
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactorySeatReservation
import com.circleappsstudio.mimisa.ui.viewmodel.roleuser.RoleUserViewModel
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

    private val authViewModel by activityViewModels<AuthViewModel> {
        VMFactoryAuth(
                AuthRepository(
                        AuthDataSource()
                )
        )
    }

    private val adminViewModel by activityViewModels<RoleUserViewModel> {
        VMFactoryAdmin(
                RoleUserRepository(
                        RoleUserDataSource()
                )
        )
    }

    private var isAdmin = false

    private val emailUser by lazy { authViewModel.getEmailUser() }

    private val bundle by lazy { Bundle() }

    private lateinit var coupleId: String
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

        checkIfUserIsAdmin()

        loadAvailableCouplesObserver()

        loadNoAvailableCouplesObserver()

    }

    override fun checkIfUserIsAdmin() {

        if (isOnline(requireContext())) {

            adminViewModel.checkCreatedAdminByEmailUser(emailUser)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                isAdmin = resultEmitted.data
                                hideProgressBar()

                            }

                            is Resource.Failure -> {
                                showMessage(resultEmitted.exception.message.toString(), 2)
                                hideProgressBar()
                            }

                        }

                    })

        }


    }


    override fun checkIfIsCoupleAvailable() {

        seatReservationViewModel.checkIfIsCoupleAvailable(coupleId)
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when (resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {

                            /*if (resultEmitted.data) {
                                navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)
                            } else {
                                showMessage("La pareja seleccionada no está disponible.", 2)
                                hideProgressBar()
                            }*/

                            if (resultEmitted.data) {

                                if (isAdmin) {
                                    navController.navigate(R.id.coupleSeatCategoryFragment, bundle)
                                } else {
                                    navController.navigate(R.id.navigation_couple_seat_category_fragment, bundle)
                                }

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
                txt_couple_1_no_available.text = "¡Disponible!"
                ll_availability_couple_1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_2" -> {
                txt_couple_2_no_available.text = "¡Disponible!"
                ll_availability_couple_2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_3" -> {
                txt_couple_3_no_available.text = "¡Disponible!"
                ll_availability_couple_3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_4" -> {
                txt_couple_4_no_available.text = "¡Disponible!"
                ll_availability_couple_4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_5" -> {
                txt_couple_5_no_available.text = "¡Disponible!"
                ll_availability_couple_5.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_6" -> {
                txt_couple_6_no_available.text = "¡Disponible!"
                ll_availability_couple_6.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_7" -> {
                txt_couple_7_no_available.text = "¡Disponible!"
                ll_availability_couple_7.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_8" -> {
                txt_couple_8_no_available.text = "¡Disponible!"
                ll_availability_couple_8.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_9" -> {
                txt_couple_9_no_available.text = "¡Disponible!"
                ll_availability_couple_9.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_10" -> {
                txt_couple_10_no_available.text = "¡Disponible!"
                ll_availability_couple_10.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_11" -> {
                txt_couple_11_no_available.text = "¡Disponible!"
                ll_availability_couple_11.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_12" -> {
                txt_couple_12_no_available.text = "¡Disponible!"
                ll_availability_couple_12.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_13" -> {
                txt_couple_13_no_available.text = "¡Disponible!"
                ll_availability_couple_13.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_14" -> {
                txt_couple_14_no_available.text = "¡Disponible!"
                ll_availability_couple_14.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_15" -> {
                txt_couple_15_no_available.text = "¡Disponible!"
                ll_availability_couple_15.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_16" -> {
                txt_couple_16_no_available.text = "¡Disponible!"
                ll_availability_couple_16.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_17" -> {
                txt_couple_17_no_available.text = "¡Disponible!"
                ll_availability_couple_17.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_18" -> {
                txt_couple_18_no_available.text = "¡Disponible!"
                ll_availability_couple_18.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_19" -> {
                txt_couple_19_no_available.text = "¡Disponible!"
                ll_availability_couple_19.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_20" -> {
                txt_couple_20_no_available.text = "¡Disponible!"
                ll_availability_couple_20.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_21" -> {
                txt_couple_21_no_available.text = "¡Disponible!"
                ll_availability_couple_21.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_22" -> {
                txt_couple_22_no_available.text = "¡Disponible!"
                ll_availability_couple_22.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "couple_23" -> {
                txt_couple_23_no_available.text = "¡Disponible!"
                ll_availability_couple_23.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

        }

    }

    override fun showNoAvailableCoupleTextView(documentId: String) {

        when (documentId) {

            "couple_1" -> {
                //txt_couple_1_no_available.visibility = View.GONE
                txt_couple_1_no_available.text = "¡No disponible!"
                ll_availability_couple_1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_2" -> {
                txt_couple_2_no_available.text = "¡No disponible!"
                ll_availability_couple_2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_3" -> {
                txt_couple_3_no_available.text = "¡No disponible!"
                ll_availability_couple_3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_4" -> {
                txt_couple_4_no_available.text = "¡No disponible!"
                ll_availability_couple_4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_5" -> {
                txt_couple_5_no_available.text = "¡No disponible!"
                ll_availability_couple_5.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_6" -> {
                txt_couple_6_no_available.text = "¡No disponible!"
                ll_availability_couple_6.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_7" -> {
                txt_couple_7_no_available.text = "¡No disponible!"
                ll_availability_couple_7.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_8" -> {
                txt_couple_8_no_available.text = "¡No disponible!"
                ll_availability_couple_8.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_9" -> {
                txt_couple_9_no_available.text = "¡No disponible!"
                ll_availability_couple_9.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_10" -> {
                txt_couple_10_no_available.text = "¡No disponible!"
                ll_availability_couple_10.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_11" -> {
                txt_couple_11_no_available.text = "¡No disponible!"
                ll_availability_couple_11.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_12" -> {
                txt_couple_12_no_available.text = "¡No disponible!"
                ll_availability_couple_12.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_13" -> {
                txt_couple_13_no_available.text = "¡No disponible!"
                ll_availability_couple_13.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_14" -> {
                txt_couple_14_no_available.text = "¡No disponible!"
                ll_availability_couple_14.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_15" -> {
                txt_couple_15_no_available.text = "¡No disponible!"
                ll_availability_couple_15.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_16" -> {
                txt_couple_16_no_available.text = "¡No disponible!"
                ll_availability_couple_16.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_17" -> {
                txt_couple_17_no_available.text = "¡No disponible!"
                ll_availability_couple_17.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_18" -> {
                txt_couple_18_no_available.text = "¡No disponible!"
                ll_availability_couple_18.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_19" -> {
                txt_couple_19_no_available.text = "¡No disponible!"
                ll_availability_couple_19.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_20" -> {
                txt_couple_20_no_available.text = "¡No disponible!"
                ll_availability_couple_20.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_21" -> {
                txt_couple_21_no_available.text = "¡No disponible!"
                ll_availability_couple_21.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_22" -> {
                txt_couple_22_no_available.text = "¡No disponible!"
                ll_availability_couple_22.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "couple_23" -> {
                txt_couple_23_no_available.text = "¡No disponible!"
                ll_availability_couple_23.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
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

            coupleId = "couple_1"
            coupleNumber = "Pareja 1"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "19", "20"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple2() {

        btn_couple_2.setOnClickListener {

            coupleId = "couple_2"
            coupleNumber = "Pareja 2"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "21", "22"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple3() {

        btn_couple_3.setOnClickListener {

            coupleId = "couple_3"
            coupleNumber = "Pareja 3"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber,"26", "27"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple4() {

        btn_couple_4.setOnClickListener {

            coupleId = "couple_4"
            coupleNumber = "Pareja 4"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "28", "29"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple5() {

        btn_couple_5.setOnClickListener {

            coupleId = "couple_5"
            coupleNumber = "Pareja 5"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "33", "34"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple6() {

        btn_couple_6.setOnClickListener {

            coupleId = "couple_6"
            coupleNumber = "Pareja 6"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "35", "36"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple7() {

        btn_couple_7.setOnClickListener {

            coupleId = "couple_7"
            coupleNumber = "Pareja 7"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "40", "41"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple8() {

        btn_couple_8.setOnClickListener {

            coupleId = "couple_8"
            coupleNumber = "Pareja 8"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "42", "43"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple9() {

        btn_couple_9.setOnClickListener {

            coupleId = "couple_9"
            coupleNumber = "Pareja 9"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "44", "45"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple10() {

        btn_couple_10.setOnClickListener {

            coupleId = "couple_10"
            coupleNumber = "Pareja 10"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "46", "47"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple11() {

        btn_couple_11.setOnClickListener {

            coupleId = "couple_11"
            coupleNumber = "Pareja 11"
            bundle.putStringArrayList(coupleId, arrayListOf(coupleId, coupleNumber, "51", "52"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple12() {

        btn_couple_12.setOnClickListener {

            coupleId = "couple_12"
            coupleNumber = "Pareja 12"
            bundle.putStringArrayList(coupleId, arrayListOf(coupleId, coupleNumber, "53", "54"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple13() {

        btn_couple_13.setOnClickListener {

            coupleId = "couple_13"
            coupleNumber = "Pareja 13"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "58", "59"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple14() {

        btn_couple_14.setOnClickListener {

            coupleId = "couple_14"
            coupleNumber = "Pareja 14"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "60", "61"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple15() {

        btn_couple_15.setOnClickListener {

            coupleId = "couple_15"
            coupleNumber = "Pareja 15"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "65", "66"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple16() {

        btn_couple_16.setOnClickListener {

            coupleId = "couple_16"
            coupleNumber = "Pareja 16"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "67", "68"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple17() {

        btn_couple_17.setOnClickListener {

            coupleId = "couple_17"
            coupleNumber = "Pareja 17"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "72", "73"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple18() {

        btn_couple_18.setOnClickListener {

            coupleId = "couple_18"
            coupleNumber = "Pareja 18"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "74", "75"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple19() {

        btn_couple_19.setOnClickListener {

            coupleId = "couple_19"
            coupleNumber = "Pareja 19"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "79", "80"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple20() {

        btn_couple_20.setOnClickListener {

            coupleId = "couple_20"
            coupleNumber = "Pareja 20"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "81", "82"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple21() {

        btn_couple_21.setOnClickListener {

            coupleId = "couple_21"
            coupleNumber = "Pareja 21"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "86", "87"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple22() {

        btn_couple_22.setOnClickListener {

            coupleId = "couple_22"
            coupleNumber = "Pareja 22"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "88", "89"))
            checkIfIsCoupleAvailable()

        }

    }

    override fun goToCouple23() {

        btn_couple_23.setOnClickListener {

            coupleId = "couple_23"
            coupleNumber = "Pareja 23"
            bundle.putStringArrayList("coupleSeats", arrayListOf(coupleId, coupleNumber, "93", "94"))
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