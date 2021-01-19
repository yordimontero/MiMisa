package com.circleappsstudio.mimisa.domain.params

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.coroutines.flow.Flow

class ParamsRepository(
        private val paramsDataSource: DataSource.Params
): Repository.Params {

    /*
        Método encargado de escuchar en tiempo real si la reservación de asientos esta habilitada
        o deshabilitada.
    */
    override suspend fun fetchIsSeatReservationAvailable()
            : Flow<Resource<Boolean>> = paramsDataSource.fetchIsSeatReservationAvailable()

    /*
        Método encargado de habilitar o deshabilitar la reservación de asientos.
    */
    override suspend fun setIsSeatReservationAvailable(isSeatReservationAvailable: Boolean)
            = paramsDataSource.setIsSeatReservationAvailable(isSeatReservationAvailable)

    /*
        Método encargado de escuchar en tiempo real el iterador de la reserva de asientos.
    */
    override suspend fun fetchIterator()
            : Flow<Resource<Int>> = paramsDataSource.fetchIterator()

    /*
        Método encargado de aumentar el iterador al reservar un asiento.
    */
    override suspend fun addIterator(seatNumber: Int)
            = paramsDataSource.addIterator(seatNumber)

    /*
        Método encargado de traer el número límite de asientos disponibles.
    */
    override suspend fun fetchSeatLimit()
            : Resource<Int> = paramsDataSource.fetchSeatLimit()

    /*
        Método encargado de actualizar el número máximo de asientos disponibles.
    */
    override suspend fun updateSeatLimit(seatLimit: Int)
            = paramsDataSource.updateSeatLimit(seatLimit)

    /*
        Método encargado de escuchar en tiempo real el versionCode.
    */
    override suspend fun fetchVersionCode()
            : Flow<Resource<Int>> = paramsDataSource.fetchVersionCode()

}