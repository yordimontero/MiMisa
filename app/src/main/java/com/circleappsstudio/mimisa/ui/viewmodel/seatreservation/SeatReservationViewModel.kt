package com.circleappsstudio.mimisa.ui.viewmodel.seatreservation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.circleappsstudio.mimisa.domain.Repo
import com.circleappsstudio.mimisa.domain.seatreservation.SeatReservationRepo
import com.circleappsstudio.mimisa.ui.viewmodel.MainViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.FirebaseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SeatReservationViewModel(
        private val seatReservationRepo: Repo.SeatReservation
) : ViewModel(), MainViewModel.SeatReservation {

    override fun fetchIterator(): LiveData<Resource<Int?>> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {

            emit(seatReservationRepo.fetchIterator())

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    /*val test = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {

            seatReservationRepo.test().collect {
                emit(it)
            }

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }*/
}