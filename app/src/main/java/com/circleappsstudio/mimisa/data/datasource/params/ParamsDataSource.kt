package com.circleappsstudio.mimisa.data.datasource.params

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ParamsDataSource: DataSource.Params {

    private val db by lazy { FirebaseFirestore.getInstance() }

    @ExperimentalCoroutinesApi
    override suspend fun fetchIsSeatReservationAvailable()
            : Flow<Resource<Boolean>> = callbackFlow {
        /*
            Método encargado de escuchar en tiempo real si la reservación de asientos esta habilitada
            o deshabilitada.
        */
        val isAvailablePath = db.collection("diaconia")
            .document("la_argentina")
            .collection("params")
            .document("data")

        val subscription = isAvailablePath.addSnapshotListener { documentSnapshot,
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
        /*
            Si no existe nada en el ViewModel que no esté haciendo ".collect",
            entonces cancela la suscripción y cierra el canal con el awaitClose.
            Esto pasa cuando la activity que conecta con el dicho ViewModel se cierra.
        */
        awaitClose { subscription.remove() }

    }

    override suspend fun setIsSeatReservationAvailable(isSeatReservationAvailable: Boolean) {
        /*
            Método encargado de habilitar o deshabilitar la reservación de asientos.
        */
        db.collection("diaconia")
            .document("la_argentina")
            .collection("params")
            .document("data")
            .update("is_seat_reservation_available", isSeatReservationAvailable)
            .await()

    }

    @ExperimentalCoroutinesApi
    override suspend fun fetchVersionCode()
            : Flow<Resource<Int>> = callbackFlow {
        /*
            Método encargado de escuchar en tiempo real el versionCode.
        */

        val versionCodePath = db.collection("general_params")
                .document("data")

        val subscription = versionCodePath.addSnapshotListener { documentSnapshot,
                                                                 firebaseFirestoreException ->

            try {

                if (documentSnapshot!!.exists()) {

                    val versionCode = documentSnapshot.getLong("version_code")!!.toInt()

                    offer(Resource.Success(versionCode))

                } else {
                    channel.close(firebaseFirestoreException?.cause)
                }

            } catch (e: Exception){
                e.printStackTrace()
            }

        }

        awaitClose { subscription.remove() }

    }

    @ExperimentalCoroutinesApi
    override suspend fun fetchIsRegisterIntentionAvailable()
            : Flow<Resource<Boolean>> = callbackFlow {
        /*
            Método encargado de escuchar en tiempo real si el registro de intenciones esta habilitado
            o deshabilitado.
        */
        val isAvailablePath = db.collection("diaconia")
                .document("la_argentina")
                .collection("params")
                .document("data")

        val subscription = isAvailablePath.addSnapshotListener { documentSnapshot,
                                                                 firebaseFirestoreException ->
            /*
                "subscription" va a estar siempre escuchando en tiempo real el valor de "is_register_intention_available",
                y va a estar ofreciendo su valor por medio del offer(Resource.Success(isAvailable)).
            */
            if (documentSnapshot!!.exists()) {

                val isAvailable = documentSnapshot.getBoolean("is_register_intention_available")!!

                offer(Resource.Success(isAvailable))

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

    override suspend fun setIsRegisterIntentionAvailable(isRegisterIntentionAvailable: Boolean) {
        /*
            Método encargado de habilitar o deshabilitar el registro de intenciones.
        */
        db.collection("diaconia")
                .document("la_argentina")
                .collection("params")
                .document("data")
                .update("is_register_intention_available", isRegisterIntentionAvailable)
                .await()

    }

}