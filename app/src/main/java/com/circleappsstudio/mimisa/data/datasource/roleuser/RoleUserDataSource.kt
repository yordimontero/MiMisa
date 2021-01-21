package com.circleappsstudio.mimisa.data.datasource.roleuser

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RoleUserDataSource: DataSource.RoleUser {

    private val db by lazy { FirebaseFirestore.getInstance() }

    override suspend fun fetchAdminCode(): Resource<String?> {
        /*
            Método encargado de traer el código de verificación para crear el rol de Administrador.
        */
        var adminCode = ""

        db.collection("diaconia")
            .document("la_argentina")
            .collection("params")
            .document("data")
            .get()
            .addOnSuccessListener { document ->

                if (document.exists()){
                    adminCode = document.data!!["admin_code"].toString()
                }

            }.await()

        return Resource.Success(adminCode)

    }

    override suspend fun checkCreatedAdminByEmailUser(emailUser: String): Resource<Boolean> {
        /*
            Método encargado de verificar que el código de verificación
            para crear el rol de Administrador sea correcto.
        */
        var isAdminRegistered = false

        db.collection("diaconia")
            .document("la_argentina")
            .collection("admins")
            .document("data")
            .collection("registered_admins")
            .whereEqualTo("emailUser", emailUser)
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    isAdminRegistered = document.exists()
                }

            }.await()

        return Resource.Success(isAdminRegistered)

    }

    override suspend fun createAdmin(emailUser: String, nameUser: String) {
        /*
            Método encargado de crear el rol de Administrador.
        */
        val admin = hashMapOf(
                "emailUser" to emailUser,
                "nameUser" to nameUser
        )

        db.collection("diaconia")
                .document("la_argentina")
                .collection("admins")
                .document("data")
                .collection("registered_admins")
                .document(emailUser)
                .set(admin)
                .await()

    }

    override suspend fun deleteAdmin(emailUser: String) {
        /*
            Método encargado de eliminar el rol de Administrador. (Crear rol de Usuario).
        */
        db.collection("diaconia")
                .document("la_argentina")
                .collection("admins")
                .document("data")
                .collection("registered_admins")
                .document(emailUser)
                .delete()
                .await()

    }

}