package com.circleappsstudio.mimisa.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.circleappsstudio.mimisa.domain.Repo

class VMFactoryIntention(private val intentionRepo: Repo.Intentions): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Repo.Intentions::class.java).newInstance(intentionRepo)
    }

}