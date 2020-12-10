// Repositorio encargado de interactuar con el DataSource "RoleDataSource".

package com.circleappsstudio.mimisa.domain.roleuser

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.vo.Resource

class RoleUserRepository(private val roleDataSource: DataSource.RoleUser): Repository.RoleUser {

    /*
        Método encargado de traer el código de verificación para crear el rol de Administrador.
    */
    override suspend fun fetchAdminCode(): Resource<String?> = roleDataSource.fetchAdminCode()

    /*
        Método encargado de verificar que el código de verificación
        para crear el rol de Administrador sea correcto.
    */
    override suspend fun checkCreatedAdminByEmailUser(emailUser: String)
            : Resource<Boolean> = roleDataSource.checkCreatedAdminByEmailUser(emailUser)

    /*
        Método encargado de crear el rol de Administrador.
    */
    override suspend fun createAdmin(
            emailUser: String,
            nameUser: String
    ) = roleDataSource.createAdmin(emailUser, nameUser)

    /*
        Método encargado de eliminar el rol de Administrador. (Crear rol de Usuario).
    */
    override suspend fun deleteAdmin(emailUser: String) = roleDataSource.deleteAdmin(emailUser)

}