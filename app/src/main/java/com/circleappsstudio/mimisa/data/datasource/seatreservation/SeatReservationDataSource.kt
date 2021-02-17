package com.circleappsstudio.mimisa.data.datasource.seatreservation

import android.util.Log
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

    override suspend fun saveSeatReserved(
            seatNumber: Int,
            nameUser: String,
            lastNameUser: String,
            idNumberUser: String
    ): Resource<Boolean> {
        /*
            Método encargado de reservar un asiento.
        */
        val reservedSeat = hashMapOf(
                "seatNumber" to seatNumber,
                "nameUser" to nameUser,
                "lastNameUser" to lastNameUser,
                "idNumberUser" to idNumberUser,
                "dateRegistered" to date,
                "seatRegisteredBy" to currentNameUser
        )

        var isSeatReserved = false

        val path = db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("data")
                .collection("registered_seats")
                .document(seatNumber.toString())

        db.runTransaction { transaction ->

            val snapshot = transaction.get(path)

            if (!snapshot.exists()) {

                db.collection("diaconia")
                        .document("la_argentina")
                        .collection("seat")
                        .document("data")
                        .collection("registered_seats")
                        .document(seatNumber.toString())
                        .set(reservedSeat)

                isSeatReserved = true

            }

        }.await()

        return Resource.Success(isSeatReserved)

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
                                    document.data!!["lastNameUser"].toString(),
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

    override suspend fun fetchRegisteredSeatsByNameUser(): Resource<List<Seat>>? {
        /*
            Método encargado de traer todos los asientos reservados por el usuario actual registrado.
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
                                    document.data!!["lastNameUser"].toString(),
                                    document.data!!["idNumberUser"].toString(),
                                    document.data!!["dateRegistered"].toString()
                            )

                            seatList.add(seat)

                        }

                    }

                }.await()

        return Resource.Success(seatList)

    }

    override suspend fun fetchRegisteredSeatByRegisteredPerson(registeredPerson: String): Resource<List<Seat>>? {
        /*
            Método encargado de traer el asiento reservado por el nombre de la
            persona a la que está reservado.
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
                                    document.data!!["lastNameUser"].toString(),
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
            Método encargado de traer el asiento reservado por el número de asiento.
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
                                    document.data!!["lastNameUser"].toString(),
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


    /*@ExperimentalCoroutinesApi
    override suspend fun checkCouples()
    : Flow<Resource<String>> = callbackFlow {

        val isCoupleAvailablePath = db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("data")
                .collection("couples")

        val subscription = isCoupleAvailablePath.addSnapshotListener { documentSnapshot,
                                                                 firebaseFirestoreException ->
            /*
                "subscription" va a estar siempre escuchando en tiempo real el valor de "is_seat_reservation_available",
                y va a estar ofreciendo su valor por medio del offer(Resource.Success(isAvailable)).
            */
            if (documentSnapshot!!.exists()) {

                val isAvailable = documentSnapshot.getBoolean("is_seat_reservation_available")!!

                offer(Resource.Success(isAvailable))

            } else {
                channel.close(firebaseFirestoreException?.cause)
            }

        }


    }*/

    @ExperimentalCoroutinesApi
    override suspend fun checkCouples(coupleNumber: String)
            : Flow<Resource<Boolean>> = callbackFlow {
        /*

        */
        val isAvailablePath = db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("couples")
                .collection("data")
                .document(coupleNumber)

        val subscription = isAvailablePath.addSnapshotListener { documentSnapshot,
                                                                 firebaseFirestoreException ->

            if (documentSnapshot!!.exists()) {

                val documentId = documentSnapshot.getBoolean("is_available")!!

                offer(Resource.Success(documentId))

            } else {
                channel.close(firebaseFirestoreException?.cause)
            }

        }

        /*
            Si no existe nada en el ViewModel que no esté haciendo ".collect",
            entonces cancela la suscripción y cierra el canal con el awaitClose.
            Esto pasa cuando la activity que conecta con el dicho ViewModel se cierra.
        */
        awaitClose { subscription.remove() }

    }

}