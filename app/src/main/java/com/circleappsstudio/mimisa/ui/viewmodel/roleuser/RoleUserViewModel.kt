package com.circleappsstudio.mimisa.ui.viewmodel.roleuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.ui.viewmodel.MainViewModel
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.FirebaseException
import kotlinx.coroutines.Dispatchers

class RoleUserViewModel(
    private val roleRepository:
    Repository.RoleUser
): ViewModel(), MainViewModel.RoleUser {

    override fun fetchAdminCode(): LiveData<Resource<String?>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de traer el código de verificación para crear el rol de Administrador.
        */
        emit(Resource.Loading())

        try {

            emit(roleRepository.fetchAdminCode())

        } catch (e: FirebaseException){
            emit(Resource.Failure(e))
        }

    }

    override fun checkCreatedAdminByEmailUser(emailUser: String)
            : LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de verificar que el código de verificación
            para crear el rol de Administrador sea correcto.
        */
        emit(Resource.Loading())

        try {

            emit(roleRepository.checkCreatedAdminByEmailUser(emailUser))

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    override fun createAdmin(
            emailUser: String,
            nameUser: String
    ): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de crear el rol de Administrador.
        */
        emit(Resource.Loading())

        try {

            roleRepository.createAdmin(emailUser, nameUser)
            emit(Resource.Success(true))

        } catch (e: FirebaseException){
            emit(Resource.Failure(e))
        }

    }

    override fun deleteAdmin(emailUser: String)
            : LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        /*
            Método encargado de eliminar el rol de Administrador. (Crear rol de Usuario).
        */
        emit(Resource.Loading())

        try {

            roleRepository.deleteAdmin(emailUser)
            emit(Resource.Success(true))

        } catch (e: FirebaseException) {
            emit(Resource.Failure(e))
        }

    }

    /*
        Método encargado de validar que el código de verificación del rol de Administrador
        no sea vacío.
    */
    override fun checkEmptyAdminCode(adminCode: String): Boolean = adminCode.isEmpty()

    /*
        Método encargado de validar que el código de verificación del rol de Administrador
        sea correcto.
    */
    override fun validateAdminCode(
        fetchedAdminCode: String,
        adminCode: String
    ): Boolean = fetchedAdminCode != adminCode

}