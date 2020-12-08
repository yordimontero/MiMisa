// DataSource encargado de interactur con Cloud Firestore para el registro de intenciones.

package com.circleappsstudio.mimisa.data.datasource.intention

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.data.model.Intention
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class IntentionDataSource : DataSource.Intentions {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val currentNameUser by lazy { FirebaseAuth.getInstance().currentUser!!.displayName }
    private val date by lazy {
        SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Calendar.getInstance().time)
    }

    override suspend fun saveIntention(category: String, intention: String) {
        /*
            Método encargado de guardar una intención en la base de datos.
        */
        val registeredIntention = hashMapOf(
                "category" to category,
                "intention" to intention,
                "dateRegistered" to date,
                "intentionRegisteredBy" to currentNameUser
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
        /*
            Método encargado de traer todas las intenciones guardadas por el usuario autenticado actual.
        */

        var intention: Intention
        val intentionList = arrayListOf<Intention>()

        db.collection("diaconia")
                .document("la_argentina")
                .collection("intention")
                .document("data")
                .collection("registered_intentions")
                .whereEqualTo("intentionRegisteredBy", currentNameUser)
                .get().addOnSuccessListener { documents ->

                    intentionList.clear()

                    for (document in documents) {

                        if (document.exists()) {

                            intention = Intention(
                                    document.data["category"].toString(),
                                    document.data["intention"].toString(),
                                    document.data["dateRegistered"].toString()
                            )

                            intentionList.add(intention)

                        }

                    }

                }.await()

        return Resource.Success(intentionList)

    }

}