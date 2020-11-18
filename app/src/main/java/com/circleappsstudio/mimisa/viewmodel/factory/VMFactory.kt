package com.circleappsstudio.mimisa.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.circleappsstudio.mimisa.domain.Repo

class VMFactory(private val repo: Repo.LogInUser): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Repo.LogInUser::class.java).newInstance(repo)
    }

}