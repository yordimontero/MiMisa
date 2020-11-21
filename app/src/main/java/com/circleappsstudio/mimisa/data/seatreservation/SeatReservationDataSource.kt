package com.circleappsstudio.mimisa.data.seatreservation

import com.circleappsstudio.mimisa.data.DataSource
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SeatReservationDataSource : DataSource.SeatReservation {

    private val db by lazy { FirebaseFirestore.getInstance() }

    override suspend fun fetchIterator(): Resource<Int?> {

        var iterator: Int? = 0

        db.collection("asientos").document("diaconia")
                .collection("la_argentina").document("data")
                .get().addOnSuccessListener { documentFetched ->

                    if (documentFetched.exists()){
                        iterator = documentFetched.data?.get("iterador").toString().toInt()
                    }

                }.await()

        return Resource.Success(iterator)

    }

    /*fun test(): Flow<Resource<Int>> = callbackFlow {

        val docRef = db.collection("asientos").document("diaconia")
                .collection("la_argentina").document("data")

        val getIterator = docRef.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

            if (documentSnapshot!!.exists()){
                val iterator = documentSnapshot.getString("iterador")
                offer(Resource.Success(iterator.toString().toInt()))
            }

        }

        awaitClose { getIterator.remove() }

    }*/

}