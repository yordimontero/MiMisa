package com.circleappsstudio.mimisa.ui.viewmodel.intention

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.circleappsstudio.mimisa.domain.Repo
import com.circleappsstudio.mimisa.ui.viewmodel.MainViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.FirebaseException
import kotlinx.coroutines.Dispatchers

class IntentionViewModel(
        private val intentionRepo: Repo.Intention
): ViewModel(), MainViewModel.Intention {

    override fun saveIntention(
            category: String,
            intention: String
    ): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {

            intentionRepo.saveIntention(category, intention)

            emit(Resource.Success(true))

        } catch (e: FirebaseException){

            emit(Resource.Failure(e))

        }

    }

}