package com.circleappsstudio.mimisa.data.datasource.intention

import android.util.Log
import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.data.model.Intention
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class IntentionDataSource : DataSource.Intentions {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val userName by lazy { FirebaseAuth.getInstance().currentUser!!.displayName }

    override suspend fun saveIntention(category: String, intention: String) {

        val registeredIntention = hashMapOf(
                "userName" to userName,
                "category" to category,
                "intention" to intention
        )

        db.collection("diaconia")
                .document("la_argentina")
                .collection("intention")
                .document("data")
                .collection("registered_intentions")
                .add(registeredIntention)
                .await()

    }

    override suspend fun fetchSavedIntentionsByNameUser(): Resource<List<Intention>>? {

        var intention: Intention
        val intentionArrayList = arrayListOf<Intention>()

        db.collection("diaconia")
                .document("la_argentina")
                .collection("intention")
                .document("data")
                .collection("registered_intentions")
                .whereEqualTo("userName", userName)
                .get().addOnSuccessListener { documents ->

                intentionArrayList.clear()

                    for (document in documents.documents) {

                        if (document.exists()) {

                            intention = Intention(
                                    //document.data!!["userName"].toString(),
                                    document.data!!["category"].toString(),
                                    document.data!!["intention"].toString()
                            )

                            intentionArrayList.add(intention)

                            Log.e("TAG", "Intenciones: $intentionArrayList")

                        }

                    }

                }.await()

        //Log.e("TAG", "Intenciones: $intentionArrayList" )

        return Resource.Success(intentionArrayList)

    }

}