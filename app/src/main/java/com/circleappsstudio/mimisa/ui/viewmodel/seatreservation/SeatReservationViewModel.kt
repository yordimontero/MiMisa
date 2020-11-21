package com.circleappsstudio.mimisa.ui.viewmodel.seatreservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.circleappsstudio.mimisa.domain.Repo
import com.circleappsstudio.mimisa.ui.viewmodel.MainViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.FirebaseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class SeatReservationViewModel(
        private val seatReservationRepo: Repo.SeatReservation
) : ViewModel(), MainViewModel.SeatReservation {

    override fun fetchIterator(): LiveData<Resource<Int>> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {

            seatReservationRepo.fetchIterator().collect {
                emit(it)
            }

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }
}