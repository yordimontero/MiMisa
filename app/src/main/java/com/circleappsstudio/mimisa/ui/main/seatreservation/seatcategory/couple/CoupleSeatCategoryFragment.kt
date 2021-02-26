package com.circleappsstudio.mimisa.ui.main.seatreservation.seatcategory.couple

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
import kotlinx.android.synthetic.main.fragment_couple_seat_category.*

class CoupleSeatCategoryFragment : BaseFragment(),
        UI.CoupleSeatCategory,
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
    private lateinit var coupleNumber: String
    private var seat1: Int = 0
    private var seat2: Int = 0

    private lateinit var nameUser: String
    private lateinit var lastNameUser: String
    private lateinit var idNumberUser: String

    override fun getLayout(): Int = R.layout.fragment_couple_seat_category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        fetchData()

        setIsNotCoupleAvailable()

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

            getSeats = it.getStringArrayList("coupleSeats")!!
            coupleNumber = getSeats[0]
            seat1 = getSeats[1].toInt()
            seat2 = getSeats[2].toInt()

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

        btn_seat_reservation.setOnClickListener {

            hideKeyboard()

            nameUser = txt_name_seat_reservation_couple_seat_category.text.toString().trim()
            lastNameUser = txt_lastname_seat_reservation_couple_seat_category.text.toString().trim()
            idNumberUser = txt_id_number_user_seat_reservation_couple_seat_category.text.toString()

            if (!isOnline(requireContext())) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkEmptyNameUser(nameUser)) {
                txt_name_seat_reservation_couple_seat_category.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkEmptyLastNameUser(lastNameUser)) {
                txt_lastname_seat_reservation_couple_seat_category.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkEmptyIdNumberUser(idNumberUser)){
                txt_id_number_user_seat_reservation_couple_seat_category.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkValidIdNumberUser(idNumberUser)) {
                txt_id_number_user_seat_reservation_couple_seat_category.error = getString(R.string.invalid_id_number)
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

            seatReservationViewModel.saveSeatReserved(
                    seat1,
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

                            if (seat1 > seat2) {
                                goTo()
                            } else {
                                 showReserveSeatDialog()
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


    override fun setIsCoupleAvailable() {

        seatReservationViewModel.updateIsCoupleAvailable(coupleNumber, true)
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

    override fun setIsNotCoupleAvailable() {

        seatReservationViewModel.updateIsCoupleAvailable(coupleNumber, false)
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

        txt_name_seat_reservation_couple_seat_category.setText("")

        txt_lastname_seat_reservation_couple_seat_category.setText("")

        txt_id_number_user_seat_reservation_couple_seat_category.setText("")

        txt_name_seat_reservation_couple_seat_category.requestFocus()

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
        progressbar_couple_seat_category.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        /*
            Método encargado de ocultar un ProgressBar.
        */
        progressbar_couple_seat_category.visibility = View.GONE
    }

    override fun goTo() {
        /*
            Método encargado de navegar hacia el fragment "MainSeatReservation".
        */
        //navController.navigateUp()
        navController.navigate(R.id.action_go_to_seat_reservation_main_fragment_from_couple_seat_category_fragment)
    }

    override fun onPause() {
        super.onPause()

        if (!isAnySeatReserved) {
            setIsCoupleAvailable()
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
        goTo()
    }

    /*
        Método encargado de mostrar el Dialog "confirmDialog".
    */
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
    : AlertDialog? = reserveSeatDialog(this, "¿Reservar otro asiento?")

    override fun reserveSeatPositiveButtonClicked() {
        showReserveSeatDialog()!!.dismiss()
    }

    override fun reserveSeatNegativeButtonClicked() {
        goTo()
    }

}