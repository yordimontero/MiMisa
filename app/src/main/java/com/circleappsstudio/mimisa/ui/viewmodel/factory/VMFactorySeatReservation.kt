package com.circleappsstudio.mimisa.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.circleappsstudio.mimisa.domain.Repo

class VMFactorySeatReservation(private val repo: Repo.SeatReservation) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Repo.SeatReservation::class.java).newInstance(repo)
    }

}