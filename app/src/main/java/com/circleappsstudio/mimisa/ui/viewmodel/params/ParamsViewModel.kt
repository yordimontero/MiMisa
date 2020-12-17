package com.circleappsstudio.mimisa.ui.viewmodel.params

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.ui.viewmodel.MainViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.FirebaseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class ParamsViewModel(private val paramsRepository: Repository.Params): ViewModel(), MainViewModel.Params {

    /*
        Método encargado de escuchar en tiempo real el iterador de la reserva de asientos.
    */
    override fun fetchIsAvailable()
            : LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {

            paramsRepository.fetchIsAvailable().collect { isAvailable ->
                emit(isAvailable)
            }

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    /*
        Método encargado de bloquear o desbloquear el funcionamiento del app.
    */
    override fun setIsAvailable(isAvailable: Boolean)
            : LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {

            paramsRepository.setIsAvailable(isAvailable)
            emit(Resource.Success(true))

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    override fun fetchIterator(): LiveData<Resource<Int>> {
        TODO("Not yet implemented")
    }

    override fun fetchSeatLimit(): Resource<Int> {
        TODO("Not yet implemented")
    }

    override fun updateSeatLimit(seatLimit: Int): LiveData<Resource<Boolean>> {
        TODO("Not yet implemented")
    }

    /*
        Método encargado de escuchar en tiempo real el versionCode en la base de datos.
    */
    override fun fetchVersionCode()
            : LiveData<Resource<Int>> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {

            paramsRepository.fetchVersionCode().collect { versionCode ->
                emit(versionCode)
            }

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    /*
        Método encargado de validar que el versionCode actual
        sea igual al versionCode traído de la base de datos.
    */
    override fun checkVersionCode(
        fetchedVersionCode: Int,
        currentVersionCode: Int
    ): Boolean = fetchedVersionCode != currentVersionCode

}