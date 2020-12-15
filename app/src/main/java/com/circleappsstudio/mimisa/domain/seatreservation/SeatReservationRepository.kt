package com.circleappsstudio.mimisa.domain.seatreservation

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.data.model.Seat
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.coroutines.flow.Flow

class SeatReservationRepository(
        private val seatReservationDataSource: DataSource.SeatReservation
) : Repository.SeatReservation {

    /*
        Método encargado de escuchar en tiempo real el iterador de la reserva de asientos.
    */
    override suspend fun fetchIterator(): Flow<Resource<Int>> = seatReservationDataSource.fetchIterator()

    /*
        Método encargado de traer el número límite de asientos disponibles.
    */
    override suspend fun fetchSeatLimit(): Resource<Int> = seatReservationDataSource.fetchSeatLimit()

    /*
        Método encargado de reservar un asiento.
    */
    override suspend fun saveSeatReserved(
            seatNumber: Int,
            nameUser: String,
            idNumberUser: String
    ) = seatReservationDataSource.saveSeatReserved(seatNumber, nameUser, idNumberUser)

    /*
        Método encargado de aumentar el iterador al reservar un asiento.
    */
    override suspend fun addIterator(seatNumber: Int) = seatReservationDataSource.addIterator(seatNumber)

    /*
        Método encargado de traer todos los asientos reservados por el usuario leggeado.
    */
    override suspend fun fetchRegisteredSeatsByNameUser()
            : Resource<List<Seat>>? = seatReservationDataSource.fetchRegisteredSeatsByNameUser()

    /*
        Método encargado de traer todos los asientos reservados de la base datos.
    */
    override suspend fun fetchAllRegisteredSeats()
            : Resource<List<Seat>>? = seatReservationDataSource.fetchAllRegisteredSeats()

    /*
        Método encargado de verificar si una persona ya tiene reservado un asiento.
    */
    override suspend fun checkSeatSavedByIdNumberUser(idNumberUser: String)
            : Resource<Boolean> = seatReservationDataSource.checkSeatSavedByIdNumberUser(idNumberUser)

    /*
        Método encargado de actualizar el número máximo de asientos disponibles.
    */
    override suspend fun updateSeatLimit(seatLimit: Int)
            = seatReservationDataSource.updateSeatLimit(seatLimit)

}