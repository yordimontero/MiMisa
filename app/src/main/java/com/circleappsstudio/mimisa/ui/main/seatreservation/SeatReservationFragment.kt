package com.circleappsstudio.mimisa.ui.main.seatreservation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseFragment
import com.circleappsstudio.mimisa.data.datasource.seatreservation.SeatReservationDataSource
import com.circleappsstudio.mimisa.data.model.Seat
import com.circleappsstudio.mimisa.domain.seatreservation.SeatReservationRepo
import com.circleappsstudio.mimisa.ui.UI
import com.circleappsstudio.mimisa.ui.adapter.RegisteredSeatsAdapter
import com.circleappsstudio.mimisa.ui.viewmodel.factory.VMFactorySeatReservation
import com.circleappsstudio.mimisa.ui.viewmodel.seatreservation.SeatReservationViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_seat_reservation.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.HashMap

class SeatReservationFragment : BaseFragment(), UI.SeatReservation {

    private lateinit var navController: NavController

    private lateinit var seatNumber: String
    private lateinit var nameUser: String
    private lateinit var idNumberUser: String

    private val seatReservationViewModel by activityViewModels<SeatReservationViewModel> {
        VMFactorySeatReservation(
                SeatReservationRepo(
                      SeatReservationDataSource()
                )
        )
    }

    override fun getLayout(): Int {
        return R.layout.fragment_seat_reservation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        setUpRecyclerView()

        observeFetchIterator()

        btn_seat_reservation.setOnClickListener {
            saveSeatReserved()
        }

        //test()

        fetchRegisteredSeatsByUserNameObserver()

    }

    override fun observeFetchIterator() {

        seatReservationViewModel.fetchIterator().observe(viewLifecycleOwner, Observer { resultEmmited ->

            when(resultEmmited){
                is Resource.Loading -> {
                    requireContext().toast(requireContext(), "Loading...")
                }
                is Resource.Success -> {
                    txt_iterator.text = resultEmmited.data.toString()
                }
                is Resource.Failure -> {
                    requireContext().toast(requireContext(), "Failure: ${resultEmmited.exception}")
                }
            }

        })

    }

    override fun saveSeatReserved() {

        seatNumber = txt_iterator.text.toString()
        nameUser = txt_fullname_seat_reservation.text.toString()
        idNumberUser = txt_id_number_user_seat_reservation.text.toString()

        if (!isOnline(requireContext())){
            showMessage("No hay conexión a Internet.", 2)
            return
        }

        if (seatReservationViewModel.checkEmptyFieldsForSeatReservation(nameUser, idNumberUser)){
            txt_fullname_seat_reservation.error = "Complete los campos."
            txt_id_number_user_seat_reservation.error = "Complete los campos."
            return
        }

        if (seatReservationViewModel.checkValidIdNumberUser(idNumberUser)){
            txt_id_number_user_seat_reservation.error = "Número de cédula inválido."
            return
        }

        saveSeatReservedObserver()

    }

    override fun saveSeatReservedObserver() {

        seatReservationViewModel.saveSeatReserved(seatNumber.toInt(), nameUser, idNumberUser).observe(viewLifecycleOwner, Observer { resultEmitted ->

            when(resultEmitted){

                is Resource.Loading -> {
                    showProgressBar()
                }

                is Resource.Success -> {
                    addIteratorObserver()
                }

                is Resource.Failure -> {
                    showMessage(resultEmitted.exception.message.toString(), 2)
                    hideProgressBar()
                }

            }

        })

    }

    override fun addIteratorObserver() {

        seatReservationViewModel.addIterator(seatNumber.toInt()).observe(viewLifecycleOwner,
            Observer { resultEmitted ->

                when (resultEmitted) {

                    is Resource.Loading -> {
                        Log.e("TAG", "observeAddIterator: Loading")
                    }

                    is Resource.Success -> {
                        showMessage("Asiento número $seatNumber reservado con éxito.", 1)
                        hideProgressBar()
                    }

                    is Resource.Failure -> {
                        Log.e("TAG", "observeAddIterator: Failure")
                    }

                }

            })

    }

    override fun showMessage(message: String, duration: Int) {
        requireContext().toast(requireContext(), message, duration)
    }

    override fun showProgressBar() {
        progressbar_seat_reservation.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressbar_seat_reservation.visibility = View.GONE
    }

    /*fun test(){

        FirebaseFirestore.getInstance().collection("asientos")
            .document("diaconia")
            .collection("la_argentina")
            .document("data")
            .collection("asientos_registrados")
            .whereEqualTo("seatRegisteredBy", "Tester")
            .get().addOnSuccessListener { documents ->

                for (document in documents){

                    if (document.exists()){

                        Log.e("TAG", "test: ${document.data}")

                    }

                }

            }

    }*/

    /*fun test(){

        var myObject: Seat
        var myObjectList: List<Seat>

        FirebaseFirestore.getInstance().collection("asientos")
            .document("diaconia")
            .collection("la_argentina")
            .document("data")
            .collection("asientos_registrados")
            .whereEqualTo("seatRegisteredBy", FirebaseAuth.getInstance().currentUser!!.displayName)
            .get().addOnSuccessListener { document ->

                for (document in document.documents){

                    if (document.exists()){

                        /*myObject = Seat(
                            document.data!!["seatNumber"].toString().toInt(),
                            document.data!!["userName"].toString(),
                            document.data!!["idNumberUser"].toString(),
                            document.data!!["seatRegisteredBy"].toString()
                        )*/

                        myObjectList = listOf(
                            Seat(
                                document.data!!["seatNumber"].toString().toInt(),
                                document.data!!["userName"].toString(),
                                document.data!!["idNumberUser"].toString(),
                                document.data!!["seatRegisteredBy"].toString()
                            )
                        )

                        //Log.e("TAG", myObject.toString() )
                        Log.e("TAG", myObjectList.toString())

                    }

                }

            }

    }*/

    override fun fetchRegisteredSeatsByUserNameObserver() {
        seatReservationViewModel.fetchRegisteredSeatsByUserName().observe(viewLifecycleOwner, Observer { resultEmitted ->

            when(resultEmitted){

                is Resource.Loading -> {}
                is Resource.Success -> {

                    rv_seat_reservation.adapter = RegisteredSeatsAdapter(requireContext(), resultEmitted.data)

                }
                is Resource.Failure -> { showMessage(resultEmitted.exception.message.toString(), 2) }

            }

        })
    }

    override fun setUpRecyclerView() {
        rv_seat_reservation.layoutManager = LinearLayoutManager(requireContext())

        rv_seat_reservation.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

}