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
            seatCategory: String,
            seatNumber: String,
            nameUser: String,
            lastNameUser: String,
            idNumberUser: String
    ): Resource<Boolean> {
        /*
            Método encargado de reservar un asiento.
        */
        val reservedSeat = hashMapOf(
                "seatCategory" to seatCategory,
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
                .document(seatNumber)

        db.runTransaction { transaction ->

            val snapshot = transaction.get(path)

            if (!snapshot.exists()) {

                db.collection("diaconia")
                        .document("la_argentina")
                        .collection("seat")
                        .document("data")
                        .collection("registered_seats")
                        .document(seatNumber)
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
                                    document.data!!["seatCategory"].toString(),
                                    document.data!!["seatNumber"].toString(),
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
                                    /*document.data!!["seatNumber"].toString(),
                                    document.data!!["nameUser"].toString(),
                                    document.data!!["lastNameUser"].toString(),
                                    document.data!!["idNumberUser"].toString(),
                                    document.data!!["dateRegistered"].toString()*/

                                    document.data!!["seatCategory"].toString(),
                                    document.data!!["seatNumber"].toString(),
                                    document.data!!["nameUser"].toString(),
                                    document.data!!["lastNameUser"].toString(),
                                    document.data!!["idNumberUser"].toString(),
                                    document.data!!["dateRegistered"].toString()
                                    //document.data!!["seatRegisteredBy"].toString()

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
                                    document.data!!["seatNumber"].toString(),
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

    override suspend fun fetchRegisteredSeatBySeatNumber(seatNumber: String): Resource<List<Seat>>? {
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

                    for (document in documents) {

                        if (document.exists()) {

                            seat = Seat(
                                    /*document.data!!["seatNumber"].toString(),
                                    document.data!!["nameUser"].toString(),
                                    document.data!!["lastNameUser"].toString(),
                                    document.data!!["idNumberUser"].toString(),
                                    document.data!!["dateRegistered"].toString()*/

                                    document.data["seatCategory"].toString(),
                                    document.data["seatNumber"].toString(),
                                    document.data["nameUser"].toString(),
                                    document.data["lastNameUser"].toString(),
                                    document.data["idNumberUser"].toString(),
                                    document.data["dateRegistered"].toString(),
                                    document.data["seatRegisteredBy"].toString()

                            )

                            seatList.add(seat)

                            Log.e("TAG", "seat: $seat")

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

                    for (document in documents) {
                        isUserRegistered = document.exists()
                    }

                }.await()

        return Resource.Success(isUserRegistered)

    }

    override suspend fun checkIfIsCoupleAvailable(coupleNumber: String): Resource<Boolean> {

        var isAvailable = false

        db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("couples")
                .collection("data")
                .document(coupleNumber)
                .get()
                .addOnSuccessListener { document ->

                    if (document.exists()) {
                        isAvailable = document.getBoolean("is_available")!!
                    }

                }.await()

        return Resource.Success(isAvailable)

    }

    override suspend fun updateIsCoupleAvailable(coupleNumber: String, isAvailable: Boolean) {

        db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("couples")
                .collection("data")
                .document(coupleNumber)
                .update("is_available", isAvailable)
                .await()

    }

    @ExperimentalCoroutinesApi
    override suspend fun loadNoAvailableCouples()
            : Flow<Resource<String>> = callbackFlow {

        var documentId = ""

        val documentIdPath = db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("couples")
                .collection("data")

        val subscription = documentIdPath.addSnapshotListener { querySnapshot,
                                                                firebaseFirestoreException ->

            if (querySnapshot != null) {

                for (document in querySnapshot) {

                    if (document.exists()) {

                        if (document.data["is_available"] == false) {

                            documentId = document.id
                            offer(Resource.Success(documentId))

                        }

                    }

                }
            } else {
                channel.close(firebaseFirestoreException?.cause)
            }

        }

        awaitClose { subscription.remove() }

    }

    @ExperimentalCoroutinesApi
    override suspend fun loadAvailableCouples()
            : Flow<Resource<String>> = callbackFlow {

        var documentId = ""

        val documentIdPath = db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("couples")
                .collection("data")

        val subscription = documentIdPath.addSnapshotListener { querySnapshot,
                                                                firebaseFirestoreException ->

            if (querySnapshot != null) {

                for (document in querySnapshot) {

                    if (document.exists()) {

                        if (document.data["is_available"] == true) {

                            documentId = document.id
                            offer(Resource.Success(documentId))

                        }

                    }

                }
            } else {
                channel.close(firebaseFirestoreException?.cause)
            }

        }

        awaitClose { subscription.remove() }

    }

    override suspend fun checkIfIsThreesomeAvailable(threesomeNumber: String): Resource<Boolean> {

        var isAvailable = false

        db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("threesomes")
                .collection("data")
                .document(threesomeNumber)
                .get()
                .addOnSuccessListener { document ->

                    if (document.exists()) {
                        isAvailable = document.getBoolean("is_available")!!
                    }

                }.await()

        return Resource.Success(isAvailable)

    }

    override suspend fun updateIsThreesomeAvailable(threesomeNumber: String, isAvailable: Boolean) {

        db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("threesomes")
                .collection("data")
                .document(threesomeNumber)
                .update("is_available", isAvailable)
                .await()

    }

    @ExperimentalCoroutinesApi
    override suspend fun loadAvailableThreesomes(): Flow<Resource<String>> = callbackFlow {

        var documentId = ""

        val documentIdPath = db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("threesomes")
                .collection("data")

        val subscription = documentIdPath.addSnapshotListener { querySnapshot,
                                                                firebaseFirestoreException ->

            if (querySnapshot != null) {

                for (document in querySnapshot) {

                    if (document.exists()) {

                        if (document.data["is_available"] == true) {

                            documentId = document.id
                            offer(Resource.Success(documentId))

                        }

                    }

                }
            } else {
                channel.close(firebaseFirestoreException?.cause)
            }

        }

        awaitClose { subscription.remove() }

    }

    @ExperimentalCoroutinesApi
    override suspend fun loadNoAvailableThreesomes(): Flow<Resource<String>> = callbackFlow {

        var documentId = ""

        val documentIdPath = db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("threesomes")
                .collection("data")

        val subscription = documentIdPath.addSnapshotListener { querySnapshot,
                                                                firebaseFirestoreException ->

            if (querySnapshot != null) {

                for (document in querySnapshot) {

                    if (document.exists()) {

                        if (document.data["is_available"] == false) {

                            documentId = document.id
                            offer(Resource.Success(documentId))

                        }

                    }

                }
            } else {
                channel.close(firebaseFirestoreException?.cause)
            }

        }

        awaitClose { subscription.remove() }

    }

    override suspend fun checkIfIsBubbleAvailable(bubbleNumber: String): Resource<Boolean> {

        var isAvailable = false

        db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("bubbles")
                .collection("data")
                .document(bubbleNumber)
                .get()
                .addOnSuccessListener { document ->

                    if (document.exists()) {
                        isAvailable = document.getBoolean("is_available")!!
                    }

                }.await()

        return Resource.Success(isAvailable)

    }

    override suspend fun updateIsBubbleAvailable(bubbleNumber: String, isAvailable: Boolean) {

        db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("bubbles")
                .collection("data")
                .document(bubbleNumber)
                .update("is_available", isAvailable)
                .await()

    }

    @ExperimentalCoroutinesApi
    override suspend fun loadAvailableBubbles(): Flow<Resource<String>> = callbackFlow {

        var documentId = ""

        val documentIdPath = db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("bubbles")
                .collection("data")

        val subscription = documentIdPath.addSnapshotListener { querySnapshot,
                                                                firebaseFirestoreException ->

            if (querySnapshot != null) {

                for (document in querySnapshot) {

                    if (document.exists()) {

                        if (document.data["is_available"] == true) {

                            documentId = document.id
                            offer(Resource.Success(documentId))

                        }

                    }

                }
            } else {
                channel.close(firebaseFirestoreException?.cause)
            }

        }

        awaitClose { subscription.remove() }

    }

    @ExperimentalCoroutinesApi
    override suspend fun loadNoAvailableBubbles(): Flow<Resource<String>> = callbackFlow {

        var documentId = ""

        val documentIdPath = db.collection("diaconia")
                .document("la_argentina")
                .collection("seat")
                .document("bubbles")
                .collection("data")

        val subscription = documentIdPath.addSnapshotListener { querySnapshot,
                                                                firebaseFirestoreException ->

            if (querySnapshot != null) {

                for (document in querySnapshot) {

                    if (document.exists()) {

                        if (document.data["is_available"] == false) {

                            documentId = document.id
                            offer(Resource.Success(documentId))

                        }

                    }

                }
            } else {
                channel.close(firebaseFirestoreException?.cause)
            }

        }

        awaitClose { subscription.remove() }

    }

}