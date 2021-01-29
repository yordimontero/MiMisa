package com.circleappsstudio.mimisa.ui.viewmodel.params

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.ui.viewmodel.MainViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.FirebaseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class ParamsViewModel(
        private val paramsRepository: Repository.Params
): ViewModel(), MainViewModel.Params {

    override fun fetchIsSeatReservationAvailable()
            : LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de escuchar en tiempo real si la reservación de asientos esta habilitada
            o deshabilitada.
        */
        emit(Resource.Loading())

        try {

            paramsRepository.fetchIsSeatReservationAvailable().collect { isAvailable ->
                emit(isAvailable)
            }

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    override fun setIsSeatReservationAvailable(isAvailable: Boolean)
            : LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de habilitar o deshabilitar la reservación de asientos.
        */
        emit(Resource.Loading())

        try {

            paramsRepository.setIsSeatReservationAvailable(isAvailable)
            emit(Resource.Success(true))

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    override fun fetchIterator()
            : LiveData<Resource<Int>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de escuchar en tiempo real el iterador de la reserva de asientos.
        */
        emit(Resource.Loading())

        try {

            paramsRepository.fetchIterator().collect { iterator ->
                /*
                    ".collect" colecta lo que está dentro del Flow
                    (en este caso el iterador en la base de datos).
                */
                emit(iterator)
            }

        } catch (e: FirebaseException) {
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

            paramsRepository.addIterator(seatNumber)
            emit(Resource.Success(true))

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

            emit(paramsRepository.fetchSeatLimit())

        } catch (e: FirebaseException) {
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

            paramsRepository.updateSeatLimit(seatLimit)
            emit(Resource.Success(true))

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    override fun fetchVersionCode()
            : LiveData<Resource<Int>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de escuchar en tiempo real el versionCode.
        */
        emit(Resource.Loading())

        try {

            paramsRepository.fetchVersionCode().collect { versionCode ->
                emit(versionCode)
            }

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    override fun fetchIsRegisterIntentionAvailable()
            : LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de escuchar en tiempo real si la reservación de asientos esta habilitada
            o deshabilitada.
        */
        emit(Resource.Loading())

        try {

            paramsRepository.fetchIsRegisterIntentionAvailable().collect { isAvailable ->
                emit(isAvailable)
            }

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    override fun setIsRegisterIntentionAvailable(isRegisterIntentionAvailable: Boolean)
            : LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de habilitar o deshabilitar la reservación de asientos.
        */
        emit(Resource.Loading())

        try {

            paramsRepository.setIsRegisterIntentionAvailable(isRegisterIntentionAvailable)
            emit(Resource.Success(true))

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    /*
        Método encargado de validar que el límite de asientos no sea nulo.
    */
    override fun checkEmptySeatLimit(seatLimit: String)
            : Boolean = seatLimit.isEmpty()

    /*
            Método encargado de validar que el versionCode actual
            sea igual al versionCode traido de la base de datos.
        */
    override fun checkVersionCode(
        fetchedVersionCode: Int,
        currentVersionCode: Int
    ): Boolean = fetchedVersionCode != currentVersionCode

}