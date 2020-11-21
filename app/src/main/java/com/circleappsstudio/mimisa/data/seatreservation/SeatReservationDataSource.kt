package com.circleappsstudio.mimisa.data.seatreservation

import com.circleappsstudio.mimisa.data.DataSource
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SeatReservationDataSource : DataSource.SeatReservation {

    private val db by lazy { FirebaseFirestore.getInstance() }

    @ExperimentalCoroutinesApi
    override suspend fun fetchIterator(): Flow<Resource<Int>> = callbackFlow {

        val iteratorPath = db.collection("asientos")
                .document("diaconia")
                .collection("la_argentina")
                .document("data")

        val subscription = iteratorPath.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

            if (documentSnapshot!!.exists()){

                val iterator = documentSnapshot.getLong("iterador")!!.toInt()

                offer(Resource.Success(iterator))

            } else {
                channel.close(firebaseFirestoreException?.cause)
            }

        }

        awaitClose { subscription.remove() }

    }

}