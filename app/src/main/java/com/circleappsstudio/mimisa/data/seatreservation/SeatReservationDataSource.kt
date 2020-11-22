// DataSource encargado de interactuar con Cloud Firestore para la reserva de espacios.

package com.circleappsstudio.mimisa.data.seatreservation

import com.circleappsstudio.mimisa.data.DataSource
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SeatReservationDataSource : DataSource.SeatReservation {

    private val db by lazy { FirebaseFirestore.getInstance() }

    @ExperimentalCoroutinesApi
    override suspend fun fetchIterator(): Flow<Resource<Int>> = callbackFlow {
        /*
            Método encargado de escuchar en tiempo real el iterador de la reserva de asientos.
        */

        val iteratorPath = db.collection("asientos")
                .document("diaconia")
                .collection("la_argentina")
                .document("data")
                .collection("params")
                .document("data")

        val subscription = iteratorPath.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            /*
                "subscription" va a estar siempre escuchando en tiempo real el valor del iterador
                y va a estar ofreciendo el valor de dicho iterador por medio del offer(Resource.Success(iterator)).
            */
            if (documentSnapshot!!.exists()){

                val iterator = documentSnapshot.getLong("iterador")!!.toInt()

                offer(Resource.Success(iterator))

            } else {
                channel.close(firebaseFirestoreException?.cause)
            }

        }
        /*
            Si no existe nada en el ViewModel que no esté haciendo ".collect", entonces cancela la suscripción
            y cierra el canal con el awaitClose.
            Esto pasa cuando la activity que conecta con el dicho ViewModel se cierra.
        */
        awaitClose { subscription.remove() }

    }

    override suspend fun saveSeatReserved(seatNumber: Int, nameUser: String, idNumberUser: String) {

        val seatReserved = hashMapOf (
                "seatNumber" to seatNumber,
                "userName" to nameUser,
                "idNumberUser" to idNumberUser
        )

        db.collection("asientos")
                .document("diaconia")
                .collection("la_argentina")
                .document("data")
                .collection("asientos_registrados")
                .add(seatReserved)
                .await()

    }

}