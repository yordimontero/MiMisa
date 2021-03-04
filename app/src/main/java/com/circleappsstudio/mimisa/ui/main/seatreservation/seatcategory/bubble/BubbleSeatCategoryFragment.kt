package com.circleappsstudio.mimisa.ui.main.seatreservation.seatcategory.bubble

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.params.ParamsDataSource
import com.circleappsstudio.mimisa.data.datasource.seatreservation.SeatReservationDataSource
import com.circleappsstudio.mimisa.domain.params.ParamsRepository
import com.circleappsstudio.mimisa.domain.seatreservation.SeatReservationRepository
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactoryParams
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactorySeatReservation
import com.circleappsstudio.mimisa.ui.viewmodel.params.ParamsViewModel
import com.circleappsstudio.mimisa.ui.viewmodel.seatreservation.SeatReservationViewModel
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.android.synthetic.main.fragment_bubble_seat_category.*

class BubbleSeatCategoryFragment : BaseFragment(),
    UI.BubbleSeatCategory,
    UI.IsOnlineDialogClickButtonListener,
    UI.ConfirmDialogClickButtonListener,
    UI.ReserveSeatDialogClickButtonListener,
    UI.IsSeatReservationAvailableDialogClickButtonListener {

    private lateinit var navController: NavController

    private val seatReservationViewModel by activityViewModels<SeatReservationViewModel> {
        VMFactorySeatReservation(
            SeatReservationRepository(
                SeatReservationDataSource()
            )
        )
    }

    private val paramsViewModel by activityViewModels<ParamsViewModel> {
        VMFactoryParams(
            ParamsRepository(
                ParamsDataSource()
            )
        )
    }

    private var isAvailable = true
    private var isAnySeatReserved = false

    private lateinit var getSeats: ArrayList<String>
    private lateinit var bubbleId: String
    private lateinit var bubbleNumber: String

    private var nameBubbleSeat: String = ""
    private var seat1: Int = 1

    companion object {
        private const val SEAT5: Int = 5
        private const val SEAT6: Int = 6
    }

    private var seatNumber: String = ""

    private lateinit var nameUser: String
    private lateinit var lastNameUser: String
    private lateinit var idNumberUser: String

    override fun getLayout(): Int = R.layout.fragment_bubble_seat_category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        fetchData()

        setIsNotBubbleAvailable()

        saveSeatReserved()

    }

    override fun fetchData() {

        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
            return
        }

        getBundle()

        fetchIsSeatReservationAvailableObserver()

    }

    override fun getBundle() {

        requireArguments().let {

            getSeats = it.getStringArrayList("bubbleSeats")!!
            bubbleId = getSeats[0]
            bubbleNumber = getSeats[1]
            nameBubbleSeat = getSeats[2]
            /*nameSeat2 = getSeats[2]
            nameSeat3 = getSeats[3]
            nameSeat4 = getSeats[4]
            nameSeat5 = getSeats[5]

            when(bubbleNumber) {

                "bubble_3" -> {
                    nameSeat6 = getSeats[6]
                }

                "bubble_5" -> {
                    nameSeat6 = getSeats[6]
                }

                "bubble_6" -> {
                    nameSeat6 = getSeats[6]
                }

                "bubble_8" -> {
                    nameSeat6 = getSeats[6]
                }

            }*/

        }

    }

    override fun fetchIsSeatReservationAvailableObserver() {
        /*
            Método encargado de escuchar en tiempo real si la reservación de asientos esta habilitada
            o deshabilitada.
        */
        if (isOnline(requireContext())) {

            paramsViewModel.fetchIsSeatReservationAvailable()
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when (resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {

                            isAvailable = resultEmitted.data

                            if (!isAvailable) {
                                showIsSeatReservationAvailableDialog()
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

    override fun saveSeatReserved() {

        btn_bubble_seat_reservation.setOnClickListener {

            hideKeyboard()

            nameUser = txt_name_seat_reservation_bubble_seat_category.text.toString().trim()
            lastNameUser = txt_lastname_seat_reservation_bubble_seat_category.text.toString().trim()
            idNumberUser = txt_id_number_user_seat_reservation_bubble_seat_category.text.toString()

            if (!isOnline(requireContext())) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkEmptyNameUser(nameUser)) {
                txt_name_seat_reservation_bubble_seat_category.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkEmptyLastNameUser(lastNameUser)) {
                txt_lastname_seat_reservation_bubble_seat_category.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (!cb_btn_under_age_seat_reservation_bubble_seat_category.isChecked) {

                if (seatReservationViewModel.checkEmptyIdNumberUser(idNumberUser)){
                    txt_id_number_user_seat_reservation_bubble_seat_category.error = getString(R.string.complete_fields)
                    return@setOnClickListener
                }

            } else {
                idNumberUser = "Menor de edad"
            }

            if (seatReservationViewModel.checkValidIdNumberUser(idNumberUser)) {
                txt_id_number_user_seat_reservation_bubble_seat_category.error = getString(R.string.invalid_id_number)
                return@setOnClickListener
            }

            showConfirmDialog()

        }

    }

    override fun checkSeatSavedByIdNumberUserObserver() {
        /*
            Método encargado de verificar si una persona ya tiene reservado un asiento.
        */
        if (isOnline(requireContext())) {

            seatReservationViewModel.checkSeatSavedByIdNumberUser(idNumberUser)
                .observe(viewLifecycleOwner, Observer { resultEmitted ->

                    when (resultEmitted) {

                        is Resource.Loading -> {
                            showProgressBar()
                        }

                        is Resource.Success -> {

                            if (resultEmitted.data) {

                                showMessage(getString(R.string.user_has_a_reserved_seat), 2)
                                hideProgressBar()

                            } else {
                                saveSeatReservedObserver()
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

    override fun saveSeatReservedObserver() {
        /*
            Método encargado de reservar un asiento.
        */
        if (isOnline(requireContext())) {

            seatNumber = nameBubbleSeat + seat1

            seatReservationViewModel.saveSeatReserved(
                bubbleNumber,
                seatNumber,
                nameUser,
                lastNameUser,
                idNumberUser).observe(viewLifecycleOwner, Observer { resultEmitted ->

                when (resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {

                        if (resultEmitted.data) {

                            isAnySeatReserved = true

                            ++seat1

                            showMessage("Asiento reservado con éxito.", 2)
                            clearFields()
                            hideProgressBar()

                            when(bubbleId) {

                                "bubble_1" -> {

                                    if (seat1 > SEAT5) {
                                        goToMainSeatReservation()
                                    } else {
                                        showReserveSeatDialog()
                                    }

                                }

                                "bubble_2" -> {

                                    if (seat1 > SEAT5) {
                                        goToMainSeatReservation()
                                    } else {
                                        showReserveSeatDialog()
                                    }

                                }

                                "bubble_3" -> {

                                    if (seat1 > SEAT6) {
                                        goToMainSeatReservation()
                                    } else {
                                        showReserveSeatDialog()
                                    }

                                }

                                "bubble_4" -> {

                                    if (seat1 > SEAT5) {
                                        goToMainSeatReservation()
                                    } else {
                                        showReserveSeatDialog()
                                    }

                                }

                                "bubble_5" -> {

                                    if (seat1 > SEAT6) {
                                        goToMainSeatReservation()
                                    } else {
                                        showReserveSeatDialog()
                                    }

                                }

                                "bubble_6" -> {

                                    if (seat1 > SEAT6) {
                                        goToMainSeatReservation()
                                    } else {
                                        showReserveSeatDialog()
                                    }

                                }

                                "bubble_7" -> {

                                    if (seat1 > SEAT5) {
                                        goToMainSeatReservation()
                                    } else {
                                        showReserveSeatDialog()
                                    }

                                }

                                "bubble_8" -> {

                                    if (seat1 > SEAT6) {
                                        goToMainSeatReservation()
                                    } else {
                                        showReserveSeatDialog()
                                    }

                                }

                            }

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

    override fun setIsBubbleAvailable() {

        seatReservationViewModel.updateIsBubbleAvailable(bubbleId, true)
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when (resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                    }

                    is Resource.Failure -> {
                        showMessage(resultEmitted.exception.message.toString(), 2)
                        hideProgressBar()
                    }

                }

            })


    }

    override fun setIsNotBubbleAvailable() {

        seatReservationViewModel.updateIsBubbleAvailable(bubbleId, false)
            .observe(viewLifecycleOwner, Observer { resultEmitted ->

                when (resultEmitted) {

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                    }

                    is Resource.Failure -> {
                        showMessage(resultEmitted.exception.message.toString(), 2)
                        hideProgressBar()
                    }

                }

            })


    }

    override fun clearFields() {

        txt_name_seat_reservation_bubble_seat_category.setText("")

        txt_lastname_seat_reservation_bubble_seat_category.setText("")

        txt_id_number_user_seat_reservation_bubble_seat_category.setText("")

        txt_name_seat_reservation_bubble_seat_category.requestFocus()

    }

    override fun showMessage(message: String, duration: Int) {
        /*
            Método encargado de mostrar un Toast.
        */
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        progressbar_bubble_seat_category.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressbar_bubble_seat_category.visibility = View.GONE
    }

    override fun goToMainSeatReservation() {
        /*
            Método encargado de navegar hacia el fragment "MainSeatReservation".
        */
        navController.navigate(R.id.action_go_to_seat_reservation_main_fragment_from_bubble_seat_category_fragment)
    }

    override fun onPause() {
        super.onPause()

        if (!isAnySeatReserved) {
            setIsBubbleAvailable()
        }

    }

    override fun showIsOnlineDialog() {
        /*
            Método encargado de mostrar el Dialog "IsOnlineDialog".
        */
        isOnlineDialog(this)
    }

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

    override fun showIsSeatReservationAvailableDialog() {
        /*
            Método encargado de mostrar el Dialog "isSeatReservationAvailableDialog".
        */
        isSeatReservationAvailableDialog(this)
    }

    override fun isSeatReservationAvailablePositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "isSeatReservationAvailableDialog".
        */
        goToMainSeatReservation()
    }

    override fun showConfirmDialog()
    : AlertDialog? = confirmDialog(this, getString(R.string.do_you_want_to_reserve_seat))

    override fun confirmPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "confirmDialog".
        */
        checkSeatSavedByIdNumberUserObserver()
    }

    override fun confirmNegativeButtonClicked() {
        /*
            Método encargado de controlar el botón negativo del Dialog "confirmDialog".
        */
        showConfirmDialog()!!.dismiss()
    }

    override fun showReserveSeatDialog()
    : AlertDialog? = reserveSeatDialog(this, "¿Desea reservar otro asiento?")

    override fun reserveSeatPositiveButtonClicked() {
        showReserveSeatDialog()!!.dismiss()
    }

    override fun reserveSeatNegativeButtonClicked() {
        goToMainSeatReservation()
    }

}