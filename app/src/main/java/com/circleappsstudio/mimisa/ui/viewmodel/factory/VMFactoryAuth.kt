package com.circleappsstudio.mimisa.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.circleappsstudio.mimisa.domain.Repo

class VMFactoryAuth(private val repo: Repo.Auth): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Repo.Auth::class.java).newInstance(repo)
    }

}