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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_couple_seat_category.*

class CoupleSeatCategoryFragment : BaseFragment(),
        UI.IsOnlineDialogClickButtonListener,
        UI.ConfirmDialogClickButtonListener,
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

    private lateinit var getSeats: ArrayList<String>
    private lateinit var coupleNumber: String
    //private lateinit var seat1: String
    //private lateinit var seat2: String
    private var seat1: Int = 0
    private var seat2: Int = 0

    private lateinit var nameUser: String
    private lateinit var lastNameUser: String
    private lateinit var idNumberUser: String
    private lateinit var seatLimitNumber: String

    private var isAvailable = true

    private var isAnySeatReserved = false



    private val db by lazy { FirebaseFirestore.getInstance() }



    override fun getLayout(): Int = R.layout.fragment_couple_seat_category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        requireArguments().let {
            getSeats = it.getStringArrayList("coupleSeats")!!
            coupleNumber = getSeats[0]
            seat1 = getSeats[1].toInt()
            seat2 = getSeats[2].toInt()
        }

        saveSeatReserved()

    }

    override fun onDetach() {
        super.onDetach()

        showMessage("onDetach", 1)

    }

    fun fetchData() {

        if (!isOnline(requireContext())) {
            showIsOnlineDialog()
            return
        }

        fetchIsSeatReservationAvailable()

    }

    fun updateIsAvailable() {

        db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("data")
                .collection("couples")
                .document(coupleNumber)
                .update("isAvailable", false)

    }

    fun fetchIsSeatReservationAvailable() {
        /*
            Método encargado de escuchar en tiempo real si la reservación de asientos esta habilitada
            o deshabilitada.
        */
        if (isOnline(requireContext())) {

            paramsViewModel.fetchIsSeatReservationAvailable()
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                isAvailable = resultEmitted.data

                                if (!isAvailable){
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

    fun saveSeatReserved() {

        btn_seat_reservation.setOnClickListener {

            hideKeyboard()

            nameUser = txt_name_2_seat_reservation_couple_seat_category.text.toString().trim()

            lastNameUser = txt_lastname_2_seat_reservation_couple_seat_category.text.toString().trim()

            idNumberUser = txt_id_number_user_2_seat_reservation_couple_seat_category.text.toString()

            showConfirmDialog()



            /*if (!isOnline(requireContext())) {
                showIsOnlineDialog()
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkEmptyNameUser(nameUser)) {
                txt_name_1_seat_reservation_couple_seat_category.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkEmptyLastNameUser(lastNameUser)) {
                txt_lastname_1_seat_reservation_couple_seat_category.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkEmptyIdNumberUser(idNumberUser)){
                txt_id_number_user_1_seat_reservation_couple_seat_category.error = getString(R.string.complete_fields)
                return@setOnClickListener
            }

            if (seatReservationViewModel.checkValidIdNumberUser(idNumberUser)) {
                txt_id_number_user_1_seat_reservation_couple_seat_category.error = getString(R.string.invalid_id_number)
                return@setOnClickListener
            }

            showConfirmDialog()*/

        }

    }

    fun saveSeatReservedObserver() {
        /*
            Método encargado de reservar un asiento.
        */
        if (isOnline(requireContext())){

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
                            clearFields()
                            showMessage("Asiento reservado", 2)
                            hideProgressBar()

                        } else {
                            //showMessage(getString(R.string.seat_reserved_error), 2)
                            //hideProgressBar()

                            seatReservationViewModel.saveSeatReserved(
                                    seat2,
                                    nameUser,
                                    lastNameUser,
                                    idNumberUser).observe(viewLifecycleOwner, Observer { resultEmitted2 ->

                                when (resultEmitted2) {

                                    is Resource.Loading -> {
                                        showProgressBar()
                                    }

                                    is Resource.Success -> {

                                        if (resultEmitted2.data) {

                                            isAnySeatReserved = true
                                            showMessage("Asiento reservado", 2)
                                            hideProgressBar()
                                            goToSeatReservationMain()

                                        } else {
                                            showMessage(getString(R.string.seat_reserved_error), 2)
                                            hideProgressBar()
                                        }

                                    }

                                    is Resource.Failure -> {
                                        showMessage(resultEmitted2.exception.message.toString(), 2)
                                        hideProgressBar()
                                    }

                                }

                            })

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

    fun goToSeatReservationMain() {
        /*
            Método encargado de navegar hacia el fragment "MainSeatReservation".
        */
        navController.navigateUp()
    }

    fun checkSeatSavedByIdNumberUserObserver() {
        /*
            Método encargado de verificar si una persona ya tiene reservado un asiento.
        */
        if (isOnline(requireContext())) {

            seatReservationViewModel.checkSeatSavedByIdNumberUser(idNumberUser)
                    .observe(viewLifecycleOwner, Observer { resultEmitted ->

                        when(resultEmitted) {

                            is Resource.Loading -> {
                                showProgressBar()
                            }

                            is Resource.Success -> {

                                if (resultEmitted.data) {
                                    showMessage(getString(R.string.user_has_a_reserved_seat), 2)
                                    hideProgressBar()
                                } else {

                                    if (seat1 <= seat2) {
                                        saveSeatReservedObserver()
                                    } else {
                                        showMessage("Error", 2)
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

    fun clearFields() {

        txt_name_2_seat_reservation_couple_seat_category.setText("")

        txt_lastname_2_seat_reservation_couple_seat_category.setText("")

        txt_id_number_user_2_seat_reservation_couple_seat_category.setText("")

        txt_name_2_seat_reservation_couple_seat_category.requestFocus()

    }

    fun showMessage(message: String, duration: Int) {
        /*
            Método encargado de mostrar un Toast.
        */
        requireContext().toast(requireContext(), message, duration)
    }

    fun showProgressBar() {
        /*
            Método encargado de mostrar un ProgressBar.
        */
        progressbar_couple_seat_category.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        /*
            Método encargado de ocultar un ProgressBar.
        */
        progressbar_couple_seat_category.visibility = View.GONE
    }

    fun showIsOnlineDialog() {
        /*
            Método encargado de mostrar el Dialog "IsOnlineDialog".
        */
        isOnlineDialog(this)

    }

    override fun isOnlineDialogPositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "IsOnlineDialog".
        */
        if (isOnline(requireContext())){
            fetchData()
        } else {
            showIsOnlineDialog()
        }

    }

    fun showIsSeatReservationAvailableDialog() {
        /*
            Método encargado de mostrar el Dialog "isSeatReservationAvailableDialog".
        */
        isSeatReservationAvailableDialog(this)
    }

    override fun isSeatReservationAvailablePositiveButtonClicked() {
        /*
            Método encargado de controlar el botón positivo del Dialog "isSeatReservationAvailableDialog".
        */
        navController.navigateUp()
    }

    fun showConfirmDialog(): AlertDialog? {
        /*
            Método encargado de mostrar el Dialog "confirmDialog".
        */
        return confirmDialog(this, getString(R.string.do_you_want_to_reserve_seat))
    }

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

}