package com.circleappsstudio.mimisa.ui.viewmodel.seatreservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
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

    override fun saveSeatReserved(
        seatNumber: Int,
        nameUser: String,
        idNumberUser: String
    ): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {
            seatReservationRepo.saveSeatReserved(seatNumber, nameUser, idNumberUser)
            emit(Resource.Success(true))
        } catch (e: FirebaseException){
            emit(Resource.Failure(e))
        }

    }

    override fun addIterator(seatNumber: Int): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {
            seatReservationRepo.addIterator(seatNumber)
            emit(Resource.Success(true))
        } catch (e: FirebaseException){
            emit(Resource.Failure(e))
        }

    }

    override fun checkEmptyFieldsForSeatReservation(
        nameUser: String,
        idNumberUser: String
    ): Boolean {
        return nameUser.isEmpty() && idNumberUser.isEmpty()
    }

    override fun checkValidIdNumberUser(idNumberUser: String): Boolean {
        return idNumberUser.length < 9
    }

}