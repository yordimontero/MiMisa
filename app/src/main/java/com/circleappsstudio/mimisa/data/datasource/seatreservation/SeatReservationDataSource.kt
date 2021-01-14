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
import java.text.SimpleDateFormat
import java.util.*

class SeatReservationDataSource : DataSource.SeatReservation {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val currentNameUser by lazy { FirebaseAuth.getInstance().currentUser!!.displayName }
    private val date by lazy {
        SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Calendar.getInstance().time)
    }

    @ExperimentalCoroutinesApi
    override suspend fun fetchIterator(): Flow<Resource<Int>> = callbackFlow {
        /*
            Método encargado de escuchar en tiempo real el iterador de la reserva de asientos.
        */
        val iteratorPath = db.collection("diaconia")
                .document("la_argentina")
                .collection("params")
                .document("data")

        val subscription = iteratorPath.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            /*
                "subscription" va a estar siempre escuchando en tiempo real el valor del iterador
                y va a estar ofreciendo el valor de dicho iterador por medio del offer(Resource.Success(iterator)).
            */
            if (documentSnapshot!!.exists()) {

                val iterator = documentSnapshot.getLong("iterator")!!.toInt()

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

        db.collection("diaconia")
                .document("la_argentina")
                .collection("params")
                .document("data")
                .get().addOnSuccessListener { document ->

                    if (document.exists()) {
                        seatLimit = document.data!!["seat_limit"].toString().toInt()
                    }

                }.await()

        return Resource.Success(seatLimit)

    }

    override suspend fun saveSeatReserved(seatNumber: Int, nameUser: String, idNumberUser: String) {
        /*
            Método encargado de reservar un asiento.
        */

        val reservedSeat = hashMapOf(
                "seatNumber" to seatNumber,
                "nameUser" to nameUser,
                "idNumberUser" to idNumberUser,
                "dateRegistered" to date,
                "seatRegisteredBy" to currentNameUser
        )

        db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("data")
                .collection("registered_seats")
                .add(reservedSeat)
                .await()

    }

    override suspend fun addIterator(seatNumber: Int) {
        /*
            Método encargado de aumentar el iterador al reservar un asiento.
        */

        val addIterator = seatNumber + 1

        db.collection("diaconia")
                .document("la_argentina")
                .collection("params")
                .document("data")
                .update("iterator", addIterator)
                .await()

    }

    override suspend fun fetchRegisteredSeatsByNameUser(): Resource<List<Seat>>? {
        /*
            Método encargado de traer todos los asientos reservados por el usuario leggeado.
        */

        var seat: Seat
        val seatList = arrayListOf<Seat>()

        db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("data")
                .collection("registered_seats")
                .whereEqualTo("seatRegisteredBy", currentNameUser)
                .orderBy("seatNumber", Query.Direction.DESCENDING)
                .get().addOnSuccessListener { documents ->

                seatList.clear()

                    for (document in documents.documents) {

                        if (document.exists()) {

                            seat = Seat(
                                    document.data!!["seatNumber"].toString().toInt(),
                                    document.data!!["nameUser"].toString(),
                                    document.data!!["idNumberUser"].toString(),
                                    document.data!!["dateRegistered"].toString()
                            )

                            seatList.add(seat)

                        }

                    }

                }.await()

        return Resource.Success(seatList)

    }

    override suspend fun fetchAllRegisteredSeats(): Resource<List<Seat>>? {
        /*
            Método encargado de traer todos los asientos reservados de la base datos.
        */
        var seat: Seat
        val seatList = arrayListOf<Seat>()

        db.collection("diaconia")
            .document("la_argentina")
            .collection("seat")
            .document("data")
            .collection("registered_seats")
            .orderBy("seatNumber", Query.Direction.DESCENDING)
            .get().addOnSuccessListener { documents ->

                seatList.clear()

                for (document in documents.documents) {

                    if (document.exists()) {

                        seat = Seat(
                            document.data!!["seatNumber"].toString().toInt(),
                            document.data!!["nameUser"].toString(),
                            document.data!!["idNumberUser"].toString(),
                            document.data!!["dateRegistered"].toString(),
                            document.data!!["seatRegisteredBy"].toString()
                        )

                        seatList.add(seat)

                    }

                }

            }.await()

        return Resource.Success(seatList)

    }

    override suspend fun fetchRegisteredSeatByRegisteredPerson(registeredPerson: String): Resource<List<Seat>>? {
        /*
            Método encargado de traer el asiento reservado por el nombre de la persona.
        */

        var seat: Seat
        val seatList = arrayListOf<Seat>()

        db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("data")
                .collection("registered_seats")
                .whereEqualTo("nameUser", registeredPerson)
                .orderBy("seatNumber", Query.Direction.DESCENDING)
                .get().addOnSuccessListener { documents ->

                    seatList.clear()

                    for (document in documents.documents) {

                        if (document.exists()) {

                            seat = Seat(
                                    document.data!!["seatNumber"].toString().toInt(),
                                    document.data!!["nameUser"].toString(),
                                    document.data!!["idNumberUser"].toString(),
                                    document.data!!["dateRegistered"].toString()
                            )

                            seatList.add(seat)

                        }

                    }

                }.await()

        return Resource.Success(seatList)

    }

    override suspend fun fetchRegisteredSeatBySeatNumber(seatNumber: Int): Resource<List<Seat>>? {
        /*
            Método encargado de traer el asiento reservado por número de asiento.
        */

        var seat: Seat
        val seatList = arrayListOf<Seat>()

        db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("data")
                .collection("registered_seats")
                .whereEqualTo("seatNumber", seatNumber)
                .get().addOnSuccessListener { documents ->

                    seatList.clear()

                    for (document in documents.documents) {

                        if (document.exists()) {

                            seat = Seat(
                                    document.data!!["seatNumber"].toString().toInt(),
                                    document.data!!["nameUser"].toString(),
                                    document.data!!["idNumberUser"].toString(),
                                    document.data!!["dateRegistered"].toString()
                            )

                            seatList.add(seat)

                        }

                    }

                }.await()

        return Resource.Success(seatList)

    }

    override suspend fun checkSeatSavedByIdNumberUser(idNumberUser: String): Resource<Boolean> {
        /*
            Método encargado de verificar si una persona ya tiene reservado un asiento.
        */
        var isUserRegistered = false

        db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("data")
                .collection("registered_seats")
                .whereEqualTo("idNumberUser", idNumberUser)
                .get().addOnSuccessListener { documents ->

                    for (document in documents){
                        isUserRegistered = document.exists()
                    }

                }.await()

        return Resource.Success(isUserRegistered)

    }

    override suspend fun updateSeatLimit(seatLimit: Int) {
        /*
            Método encargado de actualizar el número máximo de asientos disponibles.
        */
        db.collection("diaconia")
                .document("la_argentina")
                .collection("params")
                .document("data")
                .update("seat_limit", seatLimit)
                .await()

    }

}