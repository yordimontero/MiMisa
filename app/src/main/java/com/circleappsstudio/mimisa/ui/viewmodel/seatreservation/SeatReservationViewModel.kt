package com.circleappsstudio.mimisa.ui.viewmodel.seatreservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.circleappsstudio.mimisa.data.model.Seat
import com.circleappsstudio.mimisa.domain.Repo
import com.circleappsstudio.mimisa.ui.viewmodel.MainViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.FirebaseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SeatReservationViewModel(
        private val seatReservationRepo: Repo.SeatReservation
) : ViewModel(), MainViewModel.SeatReservation {

    override fun fetchIterator(): LiveData<Resource<Int>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de escuchar en tiempo real el iterador de la reserva de asientos.
        */

        emit(Resource.Loading())

        try {

            seatReservationRepo.fetchIterator().collect { iterator ->
                // ".collect" colecta lo que está dentro del Flow (en este caso el iterador en la base de datos).
                emit(iterator)
            }

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    override fun fetchSeatLimit(): LiveData<Resource<Int>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de traer el número límite de asientos disponibles.
        */
        emit(Resource.Loading())

        try {

            emit(seatReservationRepo.fetchSeatLimit())

        } catch (e: FirebaseException){

            emit(Resource.Failure(e))

        }

    }

    override fun saveSeatReserved(
        seatNumber: Int,
        nameUser: String,
        idNumberUser: String
    ): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de reservar un asiento.
        */

        emit(Resource.Loading())

        try {
            seatReservationRepo.saveSeatReserved(seatNumber, nameUser, idNumberUser)
            emit(Resource.Success(true))
        } catch (e: FirebaseException){
            emit(Resource.Failure(e))
        }

    }

    override fun addIterator(seatNumber: Int): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de aumentar el iterador al reservar un asiento.
        */
        emit(Resource.Loading())

        try {
            seatReservationRepo.addIterator(seatNumber)
            emit(Resource.Success(true))
        } catch (e: FirebaseException){
            emit(Resource.Failure(e))
        }

    }

    override fun fetchRegisteredSeatsByUserName(): LiveData<Resource<List<Seat>>?> = liveData(Dispatchers.IO) {
        /*
            Método encargado de traer todos los asientos reservados por el usuario leggeado.
        */
        emit(Resource.Loading())

        try {

            emit(seatReservationRepo.fetchRegisteredSeatsByUserName())

        } catch (e: FirebaseException){

            emit(Resource.Failure(e))

        }

    }

    /*
        Método encargado de validar que los campos de texto no sean vacíos.
    */
    override fun checkEmptyFieldsForSeatReservation(
        nameUser: String,
        idNumberUser: String
    ): Boolean = nameUser.isEmpty() && idNumberUser.isEmpty()

    /*
        Método encargado de validar que el número de cédula tenga la longitud válida.
    */
    override fun checkValidIdNumberUser(idNumberUser: String)
            : Boolean = idNumberUser.length < 9

    /*
        Método encargado de validar que aún hayan asientos disponibles.
    */
    override fun checkSeatLimit(seatNumber: Int, seatLimit: Int)
            : Boolean = seatNumber > seatLimit

}