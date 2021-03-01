package com.circleappsstudio.mimisa.ui.main.seatreservation.seatcategory.threesome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.fragment_all_seat_threesomes.*

class AllSeatThreesomesFragment : BaseFragment(),
    UI.AllSeatThreesomes,
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

    private lateinit var threesomeNumber: String

    override fun getLayout(): Int = R.layout.fragment_all_seat_threesomes

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        fetchData()

        callAllToGoThreesomes()

    }

    override fun fetchData() {

        if(!isOnline(requireContext())) {
            showIsOnlineDialog()
            return
        }

        loadAvailableThreesomesObserver()

        loadNoAvailableThreesomesObserver()

    }

    override fun checkIfIsThreesomeAvailable() {

        seatReservationViewModel.checkIfIsThreesomeAvailable(threesomeNumber)
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when (resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {

                        if (resultEmitted.data) {
                            navController.navigate(R.id.action_go_to_threesome_seat_category_from_seat_category_fragment, bundle)
                        } else {
                            showMessage("El trio seleccionado no está disponible.", 2)
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

    override fun loadAvailableThreesomesObserver() {

        seatReservationViewModel.loadAvailableThreesomes()
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        showAvailableThreesomeTextView(resultEmitted.data)
                        hideProgressBar()
                    }

                    is Resource.Failure -> {
                        showMessage(resultEmitted.exception.message.toString(), 2)
                    }

                }

            })

    }

    override fun loadNoAvailableThreesomesObserver() {

        seatReservationViewModel.loadNoAvailableThreesomes()
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when(resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        showNoAvailableThreesomeTextView(resultEmitted.data)
                        hideProgressBar()
                    }

                    is Resource.Failure -> {
                        showMessage(resultEmitted.exception.message.toString(), 2)
                        hideProgressBar()
                    }

                }

            })


    }

    override fun showAvailableThreesomeTextView(documentId: String) {

        when (documentId) {

            "threesome_1" -> {
                txt_threesome_1_no_available.text = "¡Disponible!"
            }

            "threesome_2" -> {
                txt_threesome_2_no_available.text = "¡Disponible!"
            }

            "threesome_3" -> {
                txt_threesome_3_no_available.text = "¡Disponible!"
            }

            "threesome_4" -> {
                txt_threesome_4_no_available.text = "¡Disponible!"
            }

            "threesome_5" -> {
                txt_threesome_5_no_available.text = "¡Disponible!"
            }

            "threesome_6" -> {
                txt_threesome_6_no_available.text = "¡Disponible!"
            }

            "threesome_7" -> {
                txt_threesome_7_no_available.text = "¡Disponible!"
            }

            "threesome_8" -> {
                txt_threesome_8_no_available.text = "¡Disponible!"
            }

            "threesome_9" -> {
                txt_threesome_9_no_available.text = "¡Disponible!"
            }

            "threesome_10" -> {
                txt_threesome_10_no_available.text = "¡Disponible!"
            }

            "threesome_11" -> {
                txt_threesome_11_no_available.text = "¡Disponible!"
            }

            "threesome_12" -> {
                txt_threesome_12_no_available.text = "¡Disponible!"
            }

            "threesome_13" -> {
                txt_threesome_13_no_available.text = "¡Disponible!"
            }

            "threesome_14" -> {
                txt_threesome_14_no_available.text = "¡Disponible!"
            }

            "threesome_15" -> {
                txt_threesome_15_no_available.text = "¡Disponible!"
            }

            "threesome_16" -> {
                txt_threesome_16_no_available.text = "¡Disponible!"
            }

        }

    }

    override fun showNoAvailableThreesomeTextView(documentId: String) {

        when (documentId) {

            "threesome_1" -> {
                //txt_couple_1_no_available.visibility = View.VISIBLE
                txt_threesome_1_no_available.text = "¡No disponible!"
            }

            "threesome_2" -> {
                txt_threesome_2_no_available.text = "¡No disponible!"
            }

            "threesome_3" -> {
                txt_threesome_3_no_available.text = "¡No disponible!"
            }

            "threesome_4" -> {
                txt_threesome_4_no_available.text = "¡No disponible!"
            }

            "threesome_5" -> {
                txt_threesome_5_no_available.text = "¡No disponible!"
            }

            "threesome_6" -> {
                txt_threesome_6_no_available.text = "¡No disponible!"
            }

            "threesome_7" -> {
                txt_threesome_7_no_available.text = "¡No disponible!"
            }

            "threesome_8" -> {
                txt_threesome_8_no_available.text = "¡No disponible!"
            }

            "threesome_9" -> {
                txt_threesome_9_no_available.text = "¡No disponible!"
            }

            "threesome_10" -> {
                txt_threesome_10_no_available.text = "¡No disponible!"
            }

            "threesome_11" -> {
                txt_threesome_11_no_available.text = "¡No disponible!"
            }

            "threesome_12" -> {
                txt_threesome_12_no_available.text = "¡No disponible!"
            }

            "threesome_13" -> {
                txt_threesome_13_no_available.text = "¡No disponible!"
            }

            "threesome_14" -> {
                txt_threesome_14_no_available.text = "¡No disponible!"
            }

            "threesome_15" -> {
                txt_threesome_15_no_available.text = "¡No disponible!"
            }

            "threesome_16" -> {
                txt_threesome_16_no_available.text = "¡No disponible!"
            }

        }

    }

    override fun callAllToGoThreesomes() {

        goToThreesome1()
        goToThreesome2()
        goToThreesome3()
        goToThreesome4()
        goToThreesome5()
        goToThreesome6()
        goToThreesome7()
        goToThreesome8()
        goToThreesome9()
        goToThreesome10()
        goToThreesome11()
        goToThreesome12()
        goToThreesome13()
        goToThreesome14()
        goToThreesome15()
        goToThreesome16()

    }

    override fun goToThreesome1() {

        btn_threesome_1.setOnClickListener {

            threesomeNumber = "threesome_1"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "1", "2", "3"))
            checkIfIsThreesomeAvailable()

        }

    }

    override fun goToThreesome2() {

        btn_threesome_2.setOnClickListener {

            threesomeNumber = "threesome_2"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "4", "5", "6"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome3() {

        btn_threesome_3.setOnClickListener {

            threesomeNumber = "threesome_3"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "7", "8", "9"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome4() {

        btn_threesome_4.setOnClickListener {

            threesomeNumber = "threesome_4"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "10", "11", "12"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome5() {

        btn_threesome_5.setOnClickListener {

            threesomeNumber = "threesome_5"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "13", "14", "15"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome6() {

        btn_threesome_6.setOnClickListener {

            threesomeNumber = "threesome_6"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "16", "17", "18"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome7() {

        btn_threesome_7.setOnClickListener {

            threesomeNumber = "threesome_7"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "23", "24", "25"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome8() {

        btn_threesome_8.setOnClickListener {

            threesomeNumber = "threesome_8"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "30", "31", "32"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome9() {

        btn_threesome_9.setOnClickListener {

            threesomeNumber = "threesome_9"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "37", "38", "39"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome10() {

        btn_threesome_10.setOnClickListener {

            threesomeNumber = "threesome_10"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "48", "49", "50"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome11() {

        btn_threesome_11.setOnClickListener {

            threesomeNumber = "threesome_11"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "55", "56", "57"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome12() {

        btn_threesome_12.setOnClickListener {

            threesomeNumber = "threesome_12"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "62", "63", "64"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome13() {

        btn_threesome_13.setOnClickListener {

            threesomeNumber = "threesome_13"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "69", "70", "71"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome14() {

        btn_threesome_14.setOnClickListener {

            threesomeNumber = "threesome_14"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "76", "77", "78"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome15() {

        btn_threesome_15.setOnClickListener {

            threesomeNumber = "threesome_15"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "83", "84", "85"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome16() {

        btn_threesome_16.setOnClickListener {

            threesomeNumber = "threesome_16"
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeNumber, "90", "91", "92"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun showMessage(message: String, duration: Int) {
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        progressbar_all_seat_threesomes.visibility = View.VISIBLE
        layout_all_seat_threesomes.visibility = View.GONE
    }

    override fun hideProgressBar() {
        progressbar_all_seat_threesomes.visibility = View.GONE
        layout_all_seat_threesomes.visibility = View.VISIBLE
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