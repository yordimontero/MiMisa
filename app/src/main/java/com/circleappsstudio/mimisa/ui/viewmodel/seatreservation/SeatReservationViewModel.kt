package com.circleappsstudio.mimisa.ui.viewmodel.seatreservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.circleappsstudio.mimisa.data.model.Seat
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.ui.viewmodel.MainViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.FirebaseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class SeatReservationViewModel(
        private val seatReservationRepository: Repository.SeatReservation
) : ViewModel(), MainViewModel.SeatReservation {

    override fun fetchIterator()
            : LiveData<Resource<Int>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de escuchar en tiempo real el iterador de la reserva de asientos.
        */

        emit(Resource.Loading())

        try {

            seatReservationRepository.fetchIterator().collect { iterator ->
                // ".collect" colecta lo que está dentro del Flow (en este caso el iterador en la base de datos).
                emit(iterator)
            }

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    override fun fetchSeatLimit()
            : LiveData<Resource<Int>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de traer el número límite de asientos disponibles.
        */
        emit(Resource.Loading())

        try {

            emit(seatReservationRepository.fetchSeatLimit())

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

            seatReservationRepository.saveSeatReserved(seatNumber, nameUser, idNumberUser)
            emit(Resource.Success(true))

        } catch (e: FirebaseException){
            emit(Resource.Failure(e))
        }

    }

    override fun addIterator(seatNumber: Int)
            : LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de aumentar el iterador al reservar un asiento.
        */
        emit(Resource.Loading())

        try {

            seatReservationRepository.addIterator(seatNumber)
            emit(Resource.Success(true))

        } catch (e: FirebaseException){
            emit(Resource.Failure(e))
        }

    }

    override fun fetchRegisteredSeatsByNameUser()
            : LiveData<Resource<List<Seat>>?> = liveData(Dispatchers.IO) {
        /*
            Método encargado de traer todos los asientos reservados por el usuario leggeado.
        */
        emit(Resource.Loading())

        try {

            emit(seatReservationRepository.fetchRegisteredSeatsByNameUser())

        } catch (e: FirebaseException){
            emit(Resource.Failure(e))
        }

    }

    override fun fetchAllRegisteredSeats(): LiveData<Resource<List<Seat>>?> = liveData(Dispatchers.IO) {
        /*
            Método encargado de traer todos los asientos reservados en la base de datos.
        */
        emit(Resource.Loading())

        try {

            emit(seatReservationRepository.fetchAllRegisteredSeats())

        } catch (e: FirebaseException){
            emit(Resource.Failure(e))
        }

    }

    override fun fetchRegisteredSeatByRegisteredPerson(registeredPerson: String)
            : LiveData<Resource<List<Seat>>?> = liveData(Dispatchers.IO) {
        /*
            Método encargado de traer el asiento reservado por el nombre de la persona.
        */

        emit(Resource.Loading())

        try {

            emit(seatReservationRepository.fetchRegisteredSeatByRegisteredPerson(registeredPerson))

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    override fun fetchRegisteredSeatBySeatNumber(seatNumber: Int)
            : LiveData<Resource<List<Seat>>?> = liveData(Dispatchers.IO) {
        /*
            Método encargado de traer el asiento reservado por número de asiento.
        */

        emit(Resource.Loading())

        try {

            emit(seatReservationRepository.fetchRegisteredSeatBySeatNumber(seatNumber))

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    /*
        Método encargado de validar que el nombre no sea vacío.
    */
    override fun checkEmptyNameUser(nameUser: String): Boolean = nameUser.isEmpty()

    /*
        Método encargado de validar que el número de cédula no sea vacío.
    */
    override fun checkEmptyIdNumberUser(idNumberUser: String): Boolean = idNumberUser.isEmpty()

    /*
        Método encargado de validar que el número de cédula tenga la longitud válida.
    */
    override fun checkValidIdNumberUser(idNumberUser: String)
            : Boolean = idNumberUser.length < 11

    /*
        Método encargado de validar que aún hayan asientos disponibles.
    */
    override fun checkSeatLimit(seatNumber: Int, seatLimit: Int)
            : Boolean = seatNumber > seatLimit

    /*
        Método encargado de verificar si una persona ya tiene reservado un asiento.
    */
    override fun checkSeatSavedByIdNumberUser(idNumberUser: String)
            : LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {

            emit(seatReservationRepository.checkSeatSavedByIdNumberUser(idNumberUser))

        } catch (e: FirebaseException){
            emit(Resource.Failure(e))
        }

    }

    override fun updateSeatLimit(seatLimit: Int)
            : LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de actualizar el número máximo de asientos disponibles.
        */
        emit(Resource.Loading())

        try {

            seatReservationRepository.updateSeatLimit(seatLimit)
            emit(Resource.Success(true))

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

}