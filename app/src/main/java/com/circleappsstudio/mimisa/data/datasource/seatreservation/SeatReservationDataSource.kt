// DataSource encargado de interactuar con Cloud Firestore para la reserva de espacios.

package com.circleappsstudio.mimisa.data.datasource.seatreservation

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.data.model.Seat
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SeatReservationDataSource : DataSource.SeatReservation {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val userName by lazy { FirebaseAuth.getInstance().currentUser!!.displayName }

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

    override suspend fun fetchSeatLimit(): Resource<Int> {
        /*
            Método encargado de traer el número límite de asientos disponibles.
        */

        var seatLimit = 0

        db.collection("asientos")
            .document("diaconia")
            .collection("la_argentina")
            .document("data")
            .collection("params")
            .document("data")
            .get().addOnSuccessListener { document ->

                if (document.exists()){
                    seatLimit = document.data!!["limite_espacios"].toString().toInt()
                }

            }.await()

        return Resource.Success(seatLimit)

    }

    override suspend fun saveSeatReserved(seatNumber: Int, nameUser: String, idNumberUser: String) {
        /*
            Método encargado de reservar un asiento.
        */

        val seatReserved = hashMapOf (
                "seatNumber" to seatNumber,
                "nameUser" to nameUser,
                "idNumberUser" to idNumberUser,
                "seatRegisteredBy" to userName
        )

        db.collection("asientos")
                .document("diaconia")
                .collection("la_argentina")
                .document("data")
                .collection("asientos_registrados")
                .add(seatReserved)
                .await()

    }

    override suspend fun addIterator(seatNumber: Int) {
        /*
            Método encargado de aumentar el iterador al reservar un asiento.
        */

        val addIterator = seatNumber + 1

        db.collection("asientos")
            .document("diaconia")
            .collection("la_argentina")
            .document("data")
            .collection("params")
            .document("data")
            .update("iterador", addIterator)
            .await()

    }

    override suspend fun fetchRegisteredSeatsByUserName(): Resource<List<Seat>>? {
        /*
            Método encargado de traer todos los asientos reservados por el usuario leggeado.
        */

        var seat: Seat
        val seatArrayList = arrayListOf<Seat>()

        db.collection("asientos")
                .document("diaconia")
                .collection("la_argentina")
                .document("data")
                .collection("asientos_registrados")
                .whereEqualTo("seatRegisteredBy", userName)
                .orderBy("seatNumber", Query.Direction.DESCENDING)
                .get().addOnSuccessListener { documents ->

                    seatArrayList.clear()

                    for (document in documents.documents) {

                        if (document.exists()) {

                            seat = Seat(
                                    document.data!!["seatNumber"].toString().toInt(),
                                    document.data!!["nameUser"].toString(),
                                    document.data!!["idNumberUser"].toString()
                            )

                            seatArrayList.add(seat)

                        }

                    }

                }.await()

        return Resource.Success(seatArrayList)

    }

}