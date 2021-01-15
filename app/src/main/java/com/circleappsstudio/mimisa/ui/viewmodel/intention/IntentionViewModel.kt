package com.circleappsstudio.mimisa.ui.viewmodel.intention

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.circleappsstudio.mimisa.data.model.Intention
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.ui.viewmodel.MainViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.FirebaseException
import kotlinx.coroutines.Dispatchers

class IntentionViewModel(
        private val intentionRepository: Repository.Intentions
) : ViewModel(), MainViewModel.Intentions {

    override fun saveIntention(
            category: String,
            intention: String
    ): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de guardar una intención en la base de datos.
        */
        emit(Resource.Loading())

        try {

            intentionRepository.saveIntention(category, intention)
            emit(Resource.Success(true))

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    /*
        Método encargado de verificar si la categoría de la intención es válida.
    */
    override fun checkValidIntentionCategory(category: String)
            : Boolean = category == "Seleccione una categoría"

    /*
        Método encargado de verificar si la intención es vacía.
    */
    override fun checkEmptyIntention(intention: String): Boolean = intention.isEmpty()

    override fun fetchSavedIntentionsByNameUser()
            : LiveData<Resource<List<Intention>>?> = liveData(Dispatchers.IO) {
        /*
            Método encargado de traer todas las intenciones guardadas por el usuario autenticado actual.
        */
        emit(Resource.Loading())

        try {

            emit(intentionRepository.fetchSavedIntentionsByNameUser())

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    override fun fetchAllSavedIntentions()
            : LiveData<Resource<List<Intention>>?> = liveData(Dispatchers.IO) {
        /*
            Método encargado de traer todas las intenciones guardadas en la base de datos.
        */
        emit(Resource.Loading())

        try {

            emit(intentionRepository.fetchAllSavedIntentions())

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    override fun fetchSavedIntentionsByCategory(category: String)
            : LiveData<Resource<List<Intention>>?> =  liveData(Dispatchers.IO) {
        /*
            Método encargado de traer las intenciones guardadas por categoría.
        */
        emit(Resource.Loading())

        try {

            emit(intentionRepository.fetchSavedIntentionsByCategory(category))

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

}