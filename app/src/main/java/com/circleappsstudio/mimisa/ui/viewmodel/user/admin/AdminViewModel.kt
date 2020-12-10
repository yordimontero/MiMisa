package com.circleappsstudio.mimisa.ui.viewmodel.user.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.ui.viewmodel.MainViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.FirebaseException
import kotlinx.coroutines.Dispatchers

class AdminViewModel(private val adminRepository: Repository.AdminUser): ViewModel(), MainViewModel.AdminUser {

    override fun createAdmin(
            emailUser: String,
            nameUser: String
    ): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {

            adminRepository.createAdmin(emailUser, nameUser)
            emit(Resource.Success(true))

        } catch (e: FirebaseException){
            emit(Resource.Failure(e))
        }

    }

    override fun fetchAdminCode(): LiveData<Resource<String?>> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {

            emit(adminRepository.fetchAdminCode())

        } catch (e: FirebaseException){
            emit(Resource.Failure(e))
        }

    }

    override fun checkEmptyAdminCode(adminCode: String): Boolean = adminCode.isEmpty()

    override fun validateAdminCode(
        fetchedAdminCode: String,
        adminCode: String
    ): Boolean = fetchedAdminCode != adminCode

    override fun checkCreatedAdminByEmailUser(emailUser: String)
            : LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {

            emit(adminRepository.checkCreatedAdminByEmailUser(emailUser))

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    override fun deleteAdmin(emailUser: String)
            : LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {

        emit(Resource.Loading())

        try {

            adminRepository.deleteAdmin(emailUser)
            emit(Resource.Success(true))

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

}