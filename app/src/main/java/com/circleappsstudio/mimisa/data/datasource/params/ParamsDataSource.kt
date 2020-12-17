package com.circleappsstudio.mimisa.data.datasource.params

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.vo.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ParamsDataSource: DataSource.Params {

    private val db by lazy { FirebaseFirestore.getInstance() }

    @ExperimentalCoroutinesApi
    override suspend fun fetchIsAvailable()
            : Flow<Resource<Boolean>> = callbackFlow {
        /*
            Método encargado de escuchar en tiempo real el iterador de la reserva de asientos.
        */
        val isAvailablePath = db.collection("diaconia")
            .document("la_argentina")
            .collection("params")
            .document("data")

        val subscription = isAvailablePath.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            /*
                "subscription" va a estar siempre escuchando en tiempo real el valor del iterador
                y va a estar ofreciendo el valor de dicho iterador por medio del offer(Resource.Success(iterator)).
            */
            if (documentSnapshot!!.exists()) {

                val isAvailable = documentSnapshot.getBoolean("is_available")!!

                offer(Resource.Success(isAvailable))

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

    override suspend fun setIsAvailable(isAvailable: Boolean) {
        /*
            Método encargado de bloquear o desbloquear el funcionamiento del app.
        */
        db.collection("diaconia")
            .document("la_argentina")
            .collection("params")
            .document("data")
            .update("is_available", isAvailable)
            .await()

    }

    override suspend fun fetchIterator(): Flow<Resource<Int>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchSeatLimit(): Resource<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSeatLimit(seatLimit: Int) {
        TODO("Not yet implemented")
    }

    @ExperimentalCoroutinesApi
    override suspend fun fetchVersionCode()
            : Flow<Resource<Int>> = callbackFlow {
        /*
            Método encargado de escuchar en tiempo real el versionCode en la base de datos.
        */
        val versionCodePath = db.collection("general_params")
            .document("data")

        val subscription = versionCodePath.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

            if (documentSnapshot!!.exists()) {

                val versionCode = documentSnapshot.getLong("version_code")!!.toInt()

                offer(Resource.Success(versionCode))

            } else {
                channel.close(firebaseFirestoreException?.cause)
            }

        }

        awaitClose { subscription.remove() }

    }

}