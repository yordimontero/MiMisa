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

    override fun checkVersionCode(
        fetchedVersionCode: Int,
        currentVersionCode: Int
    ): Boolean = fetchedVersionCode != currentVersionCode

}