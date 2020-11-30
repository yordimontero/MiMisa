package com.circleappsstudio.mimisa.ui.viewmodel.intention

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.data.model.Intention
import com.circleappsstudio.mimisa.domain.Repo
import com.circleappsstudio.mimisa.ui.viewmodel.MainViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.FirebaseException
import kotlinx.coroutines.Dispatchers

class IntentionViewModel(
        private val intentionRepo: Repo.Intentions
) : ViewModel(), MainViewModel.Intentions {

    override fun saveIntention(
            category: String,
            intention: String
    ): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {

            intentionRepo.saveIntention(category, intention)

            emit(Resource.Success(true))

        } catch (e: FirebaseException) {

            emit(Resource.Failure(e))

        }

    }

    override fun renameCategoryResource(context: Context, category: String): String {

        if (category == context.resources.getString(R.string.intention_category_init)) {
            return "error"
        }

        if (category == context.resources.getString(R.string.thanksgiving)) {
            return "thanksgiving"
        }

        if (category == context.resources.getString(R.string.deceased)) {
            return "deceased"
        }

        if (category == context.resources.getString(R.string.birthday)) {
            return "birthday"
        }

        return ""

    }

    override fun checkEmptyIntentionCategory(category: String): Boolean = category == "error"

    override fun checkEmptyIntention(intention: String): Boolean = intention.isEmpty()

    override fun fetchSavedIntentionsByNameUser(): LiveData<Resource<List<Intention>>?> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {

            emit(intentionRepo.fetchSavedIntentionsByUserName())

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

}