package com.circleappsstudio.mimisa.data.datasource.intention

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class IntentionDataSource : DataSource.Intention {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val nameUser by lazy { FirebaseAuth.getInstance().currentUser!!.displayName }

    override suspend fun saveIntention(category: String, intention: String) {

        val registeredIntention = hashMapOf (
                "nameUser" to nameUser,
                "category" to category,
                "intention" to intention
        )

        db.collection("diaconia")
                .document("la_argentina")
                .collection("intention")
                .document("data")
                .collection("registered_intentions")
                .document("category")
                .collection(category)
                .add(registeredIntention)
                .await()

    }

}