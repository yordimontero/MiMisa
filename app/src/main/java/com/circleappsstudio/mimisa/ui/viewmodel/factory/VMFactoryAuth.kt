package com.circleappsstudio.mimisa.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.circleappsstudio.mimisa.domain.Repository

class VMFactoryAuth(private val repository: Repository.Auth): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Repository.Auth::class.java).newInstance(repository)
    }

}