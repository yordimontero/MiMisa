package com.circleappsstudio.mimisa.ui.main.seatreservation.seatcategory.bubble

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
import kotlinx.android.synthetic.main.fragment_all_seat_bubbles.*

class AllSeatBubblesFragment : BaseFragment(),
    UI.AllSeatBubbles,
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

    private lateinit var bubbleId: String
    private lateinit var bubbleNumber: String

    override fun getLayout(): Int = R.layout.fragment_all_seat_bubbles

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        fetchData()

        callAllToGoBubbles()

    }

    override fun fetchData() {

        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
            return
        }

        checkIfUserIsAdmin()

        loadAvailableBubblesObserver()

        loadNoAvailableBubblesObserver()

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

    override fun checkIfIsBubbleAvailable() {
        /*
            Método encargado de verificar si una burbuja se encuentra disponible.
        */
        seatReservationViewModel.checkIfIsBubbleAvailable(bubbleId)
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when (resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {

                        if (resultEmitted.data) {

                            if (isAdmin) {
                                navController.navigate(R.id.admin_bubble_seat_category_fragment, bundle)
                            } else {
                                navController.navigate(R.id.bubble_seat_category_fragment, bundle)
                            }

                        } else {
                            showMessage(getString(R.string.bubble_no_available), 2)
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

    override fun loadAvailableBubblesObserver() {
        /*
            Método encargado de cargar todas las burbujas que se encuentran disponibles.
        */
        seatReservationViewModel.loadAvailableBubbles()
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when (resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        showAvailableBubbleLinearLayout(resultEmitted.data)
                        hideProgressBar()
                    }

                    is Resource.Failure -> {
                        showMessage(resultEmitted.exception.message.toString(), 2)
                    }

                }

            })

    }

    override fun loadNoAvailableBubblesObserver() {
        /*
            Método encargado de cargar todas las burbujas que no se encuentran disponibles.
        */
        seatReservationViewModel.loadNoAvailableBubbles()
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when (resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        showNoAvailableBubbleLinearLayout(resultEmitted.data)
                        hideProgressBar()
                    }

                    is Resource.Failure -> {
                        showMessage(resultEmitted.exception.message.toString(), 2)
                        hideProgressBar()
                    }

                }

            })

    }

    override fun showAvailableBubbleLinearLayout(documentId: String) {
        /*
            Método encargado de mostrar un LinearLayout verde a las burbujas disponibles.
        */
        when (documentId) {

            "bubble_1" -> {
                ll_availability_bubble_1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "bubble_2" -> {
                ll_availability_bubble_2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "bubble_3" -> {
                ll_availability_bubble_3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "bubble_4" -> {
                ll_availability_bubble_4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "bubble_5" -> {
                ll_availability_bubble_5.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "bubble_6" -> {
                ll_availability_bubble_6.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "bubble_7" -> {
                ll_availability_bubble_7.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

            "bubble_8" -> {
                ll_availability_bubble_8.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))
            }

        }

    }

    override fun showNoAvailableBubbleLinearLayout(documentId: String) {
        /*
            Método encargado de mostrar un LinearLayout rojo a las burbujas no disponibles.
        */
        when (documentId) {

            "bubble_1" -> {
                ll_availability_bubble_1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "bubble_2" -> {
                ll_availability_bubble_2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "bubble_3" -> {
                ll_availability_bubble_3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "bubble_4" -> {
                ll_availability_bubble_4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "bubble_5" -> {
                ll_availability_bubble_5.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "bubble_6" -> {
                ll_availability_bubble_6.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "bubble_7" -> {
                ll_availability_bubble_7.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

            "bubble_8" -> {
                ll_availability_bubble_8.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }

        }

    }

    override fun callAllToGoBubbles() {

        goToBubble1()
        goToBubble2()
        goToBubble3()
        goToBubble4()
        goToBubble5()
        goToBubble6()
        goToBubble7()
        goToBubble8()

    }

    override fun goToBubble1() {
        /*
            Método encargado de navegar hacia la burbuja 1
        */
        btn_bubble_1.setOnClickListener {

            bubbleId = "bubble_1"
            bubbleNumber = getString(R.string.txt_bubble_1)

            bundle.putStringArrayList(
                "bubbleSeats", arrayListOf(
                    bubbleId,
                    bubbleNumber,
                    "B1-"
                )
            )

            checkIfIsBubbleAvailable()

        }


    }

    override fun goToBubble2() {
        /*
            Método encargado de navegar hacia la burbuja 2
        */
        btn_bubble_2.setOnClickListener {

            bubbleId = "bubble_2"
            bubbleNumber = getString(R.string.txt_bubble_2)

            bundle.putStringArrayList(
                "bubbleSeats", arrayListOf(
                    bubbleId,
                    bubbleNumber,
                    "B2-"
                )
            )

            checkIfIsBubbleAvailable()

        }

    }

    override fun goToBubble3() {
        /*
            Método encargado de navegar hacia la burbuja 3
        */
        btn_bubble_3.setOnClickListener {

            bubbleId = "bubble_3"
            bubbleNumber = getString(R.string.txt_bubble_3)

            bundle.putStringArrayList(
                "bubbleSeats", arrayListOf(
                    bubbleId,
                    bubbleNumber,
                    "B3-"
                )
            )

            checkIfIsBubbleAvailable()

        }

    }

    override fun goToBubble4() {
        /*
            Método encargado de navegar hacia la burbuja 4
        */
        btn_bubble_4.setOnClickListener {

            bubbleId = "bubble_4"
            bubbleNumber = getString(R.string.txt_bubble_4)

            bundle.putStringArrayList(
                "bubbleSeats", arrayListOf(
                    bubbleId,
                    bubbleNumber,
                    "B4-"
                )
            )

            checkIfIsBubbleAvailable()

        }

    }

    override fun goToBubble5() {
        /*
            Método encargado de navegar hacia la burbuja 5
        */
        btn_bubble_5.setOnClickListener {

            bubbleId = "bubble_5"
            bubbleNumber = getString(R.string.txt_bubble_5)

            bundle.putStringArrayList(
                "bubbleSeats", arrayListOf(
                    bubbleId,
                    bubbleNumber,
                    "B5-"
                )
            )

            checkIfIsBubbleAvailable()

        }

    }

    override fun goToBubble6() {
        /*
            Método encargado de navegar hacia la burbuja 6
        */
        btn_bubble_6.setOnClickListener {

            bubbleId = "bubble_6"
            bubbleNumber = getString(R.string.txt_bubble_6)

            bundle.putStringArrayList(
                "bubbleSeats", arrayListOf(
                    bubbleId,
                    bubbleNumber,
                    "B6-"
                )
            )

            checkIfIsBubbleAvailable()

        }

    }

    override fun goToBubble7() {
        /*
            Método encargado de navegar hacia la burbuja 7
        */
        btn_bubble_7.setOnClickListener {

            bubbleId = "bubble_7"
            bubbleNumber = getString(R.string.txt_bubble_7)

            bundle.putStringArrayList(
                "bubbleSeats", arrayListOf(
                    bubbleId,
                    bubbleNumber,
                    "B7-"
                )
            )

            checkIfIsBubbleAvailable()

        }

    }

    override fun goToBubble8() {
        /*
            Método encargado de navegar hacia la burbuja 8
        */
        btn_bubble_8.setOnClickListener {

            bubbleId = "bubble_8"
            bubbleNumber = getString(R.string.txt_bubble_8)

            bundle.putStringArrayList(
                "bubbleSeats", arrayListOf(
                    bubbleId,
                    bubbleNumber,
                    "B8-"
                )
            )

            checkIfIsBubbleAvailable()

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
        progressbar_all_seat_bubbles.visibility = View.VISIBLE
        layout_all_seat_bubbles.visibility = View.GONE
    }

    override fun hideProgressBar() {
        /*
             Método encargado de ocultar un ProgressBar.
        */
        progressbar_all_seat_bubbles.visibility = View.GONE
        layout_all_seat_bubbles.visibility = View.VISIBLE
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