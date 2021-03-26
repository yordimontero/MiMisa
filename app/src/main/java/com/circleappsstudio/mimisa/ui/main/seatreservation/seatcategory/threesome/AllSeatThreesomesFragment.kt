package com.circleappsstudio.mimisa.ui.main.seatreservation.seatcategory.threesome

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

    private lateinit var threesomeId: String
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

        checkIfUserIsAdmin()

        loadAvailableThreesomesObserver()

        loadNoAvailableThreesomesObserver()

    }

    override fun checkIfUserIsAdmin() {
        /*
            Método encargado de verificar si el usuario actual es un administrador.
        */
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


    override fun checkIfIsThreesomeAvailable() {
        /*
            Método encargado de verificar si un trío se encuentra disponible.
        */
        if (isOnline(requireContext())) {

            seatReservationViewModel.checkIfIsThreesomeAvailable(threesomeId)
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when (resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {

                            if (resultEmitted.data) {

                                if (isAdmin) {
                                    navController.navigate(R.id.admin_threesome_seat_category_fragment, bundle)
                                } else {
                                    navController.navigate(R.id.threesome_seat_category_fragment, bundle)
                                }

                            } else {
                                showMessage(getString(R.string.threesome_no_available), 2)
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

    }

    override fun loadAvailableThreesomesObserver() {
        /*
            Método encargado de cargar todas los tríos que se encuentran disponibles.
        */

        if (isOnline(requireContext())) {

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

    }

    override fun loadNoAvailableThreesomesObserver() {
        /*
            Método encargado de cargar todas los tríos que no se encuentran disponibles.
        */
        if (isOnline(requireContext())) {

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

    }

    override fun showAvailableThreesomeTextView(documentId: String) {
        /*
            Método encargado de mostrar un LinearLayout verde a los tríos disponibles.
        */
        when (documentId) {

            "threesome_1" -> {
                ll_availability_threesome_1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_2" -> {
                ll_availability_threesome_2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_3" -> {
                ll_availability_threesome_3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_4" -> {
                ll_availability_threesome_4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_5" -> {
                ll_availability_threesome_5.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_6" -> {
                ll_availability_threesome_6.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_7" -> {
                ll_availability_threesome_7.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_8" -> {
                ll_availability_threesome_8.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_9" -> {
                ll_availability_threesome_9.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_10" -> {
                ll_availability_threesome_10.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_11" -> {
                ll_availability_threesome_11.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_12" -> {
                ll_availability_threesome_12.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_13" -> {
                ll_availability_threesome_13.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_14" -> {
                ll_availability_threesome_14.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_15" -> {
                ll_availability_threesome_15.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "threesome_16" -> {
                ll_availability_threesome_16.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

        }

    }

    override fun showNoAvailableThreesomeTextView(documentId: String) {
        /*
            Método encargado de mostrar un LinearLayout rojo a las parejas no disponibles.
        */
        when (documentId) {

            "threesome_1" -> {
                ll_availability_threesome_1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_2" -> {
                ll_availability_threesome_2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_3" -> {
                ll_availability_threesome_3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_4" -> {
                ll_availability_threesome_4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_5" -> {
                ll_availability_threesome_5.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_6" -> {
                ll_availability_threesome_6.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_7" -> {
                ll_availability_threesome_7.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_8" -> {
                ll_availability_threesome_8.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_9" -> {
                ll_availability_threesome_9.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_10" -> {
                ll_availability_threesome_10.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_11" -> {
                ll_availability_threesome_11.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_12" -> {
                ll_availability_threesome_12.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_13" -> {
                ll_availability_threesome_13.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_14" -> {
                ll_availability_threesome_14.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_15" -> {
                ll_availability_threesome_15.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "threesome_16" -> {
                ll_availability_threesome_16.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
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
        /*
            Método encargado de navegar hacia el trío 1
        */
        btn_threesome_1.setOnClickListener {

            threesomeId = "threesome_1"
            threesomeNumber = getString(R.string.txt_threesome_1)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "1", "2", "3"))
            checkIfIsThreesomeAvailable()

        }

    }

    override fun goToThreesome2() {
        /*
            Método encargado de navegar hacia el trío 2
        */
        btn_threesome_2.setOnClickListener {

            threesomeId = "threesome_2"
            threesomeNumber = getString(R.string.txt_threesome_2)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "4", "5", "6"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome3() {
        /*
            Método encargado de navegar hacia el trío 3
        */
        btn_threesome_3.setOnClickListener {

            threesomeId = "threesome_3"
            threesomeNumber = getString(R.string.txt_threesome_3)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "7", "8", "9"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome4() {
        /*
            Método encargado de navegar hacia el trío 4
        */
        btn_threesome_4.setOnClickListener {

            threesomeId = "threesome_4"
            threesomeNumber = getString(R.string.txt_threesome_4)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "10", "11", "12"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome5() {
        /*
            Método encargado de navegar hacia el trío 5
        */
        btn_threesome_5.setOnClickListener {

            threesomeId = "threesome_5"
            threesomeNumber = getString(R.string.txt_threesome_5)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "13", "14", "15"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome6() {
        /*
            Método encargado de navegar hacia el trío 6
        */
        btn_threesome_6.setOnClickListener {

            threesomeId = "threesome_6"
            threesomeNumber = getString(R.string.txt_threesome_6)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "16", "17", "18"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome7() {
        /*
            Método encargado de navegar hacia el trío 7
        */
        btn_threesome_7.setOnClickListener {

            threesomeId = "threesome_7"
            threesomeNumber = getString(R.string.txt_threesome_7)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "23", "24", "25"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome8() {
        /*
            Método encargado de navegar hacia el trío 8
        */
        btn_threesome_8.setOnClickListener {

            threesomeId = "threesome_8"
            threesomeNumber = getString(R.string.txt_threesome_8)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "30", "31", "32"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome9() {
        /*
            Método encargado de navegar hacia el trío 9
        */
        btn_threesome_9.setOnClickListener {

            threesomeId = "threesome_9"
            threesomeNumber = getString(R.string.txt_threesome_9)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "37", "38", "39"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome10() {
        /*
            Método encargado de navegar hacia el trío 10
        */
        btn_threesome_10.setOnClickListener {

            threesomeId = "threesome_10"
            threesomeNumber = getString(R.string.txt_threesome_10)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "48", "49", "50"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome11() {
        /*
            Método encargado de navegar hacia el trío 11
        */
        btn_threesome_11.setOnClickListener {

            threesomeId = "threesome_11"
            threesomeNumber = getString(R.string.txt_threesome_11)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "55", "56", "57"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome12() {
        /*
            Método encargado de navegar hacia el trío 12
        */
        btn_threesome_12.setOnClickListener {

            threesomeId = "threesome_12"
            threesomeNumber = getString(R.string.txt_threesome_12)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "62", "63", "64"))
            checkIfIsThreesomeAvailable()

        }


    }

    override fun goToThreesome13() {
        /*
            Método encargado de navegar hacia el trío 13
        */
        btn_threesome_13.setOnClickListener {

            threesomeId = "threesome_13"
            threesomeNumber = getString(R.string.txt_threesome_13)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "69", "70", "71"))
            checkIfIsThreesomeAvailable()

        }

    }

    override fun goToThreesome14() {
        /*
            Método encargado de navegar hacia el trío 14
        */
        btn_threesome_14.setOnClickListener {

            threesomeId = "threesome_14"
            threesomeNumber = getString(R.string.txt_threesome_14)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "76", "77", "78"))
            checkIfIsThreesomeAvailable()

        }

    }

    override fun goToThreesome15() {
        /*
            Método encargado de navegar hacia el trío 15
        */
        btn_threesome_15.setOnClickListener {

            threesomeId = "threesome_15"
            threesomeNumber = getString(R.string.txt_threesome_15)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "83", "84", "85"))
            checkIfIsThreesomeAvailable()

        }

    }

    override fun goToThreesome16() {
        /*
            Método encargado de navegar hacia el trío 16
        */
        btn_threesome_16.setOnClickListener {

            threesomeId = "threesome_16"
            threesomeNumber = getString(R.string.txt_threesome_16)
            bundle.putStringArrayList("threesomeSeats", arrayListOf(threesomeId, threesomeNumber, "90", "91", "92"))
            checkIfIsThreesomeAvailable()

        }

    }

    override fun showMessage(message: String, duration: Int) {
        /*
             Método encargado de mostrar un Toast.
        */
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        /*
             Método encargado de mostrar un ProgressBar.
        */
        progressbar_all_seat_threesomes.visibility = View.VISIBLE
        layout_all_seat_threesomes.visibility = View.GONE
    }

    override fun hideProgressBar() {
        /*
             Método encargado de ocultar un ProgressBar.
        */
        progressbar_all_seat_threesomes.visibility = View.GONE
        layout_all_seat_threesomes.visibility = View.VISIBLE
    }

    /*
        Método encargado de mostrar el Dialog "IsOnlineDialog".
    */
    override fun showIsOnlineDialog() = isOnlineDialog(this)

    override fun isOnlineDialogPositiveButtonClicked() {
        /*
             Método encargado de controlar el botón positivo del Dialog "IsOnlineDialog".
        */
        if (isOnline(requireContext())) {
            fetchData()
        } else {
            showIsOnlineDialog()
        }

    }

}