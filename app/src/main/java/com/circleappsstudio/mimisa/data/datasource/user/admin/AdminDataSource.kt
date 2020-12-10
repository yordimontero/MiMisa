package com.circleappsstudio.mimisa.data.datasource.user.admin

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdminDataSource: DataSource.AdminUser {

    private val db by lazy { FirebaseFirestore.getInstance() }

    override suspend fun createAdmin(emailUser: String, nameUser: String) {

        val admin = hashMapOf(
                "emailUser" to emailUser,
                "nameUser" to nameUser
        )

        db.collection("diaconia")
                .document("la_argentina")
                .collection("admins")
                .document("data")
                .collection("registered_admins")
                .add(admin)
                .await()

    }

    override suspend fun fetchAdminCode(): Resource<String?> {

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

}