package com.circleappsstudio.mimisa.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.circleappsstudio.mimisa.domain.Repository

class VMFactoryAdmin(private val repository: Repository.AdminUser): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Repository.AdminUser::class.java).newInstance(repository)
    }

}