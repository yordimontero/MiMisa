package com.circleappsstudio.mimisa.domain.seatreservation

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.data.model.Seat
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.coroutines.flow.Flow

class SeatReservationRepository(
        private val seatReservationDataSource: DataSource.SeatReservation
) : Repository.SeatReservation {

    override suspend fun saveSeatReserved(
        seatCategory: String,
        seatNumber: String,
        nameUser: String,
        lastNameUser: String,
        idNumberUser: String
    ): Resource<Boolean> = seatReservationDataSource.saveSeatReserved(seatCategory, seatNumber, nameUser, lastNameUser, idNumberUser)

    /*
            Método encargado de traer todos los asientos reservados de la base datos.
        */
    override suspend fun fetchAllRegisteredSeats()
            : Resource<List<Seat>>? = seatReservationDataSource.fetchAllRegisteredSeats()

    /*
        Método encargado de traer todos los asientos reservados por el usuario actual registrado.
    */
    override suspend fun fetchRegisteredSeatsByNameUser()
            : Resource<List<Seat>>? = seatReservationDataSource.fetchRegisteredSeatsByNameUser()

    /*
        Método encargado de traer el asiento reservado por el nombre de la
        persona a la que está reservado.
    */
    override suspend fun fetchRegisteredSeatByRegisteredPerson(registeredPerson: String)
            : Resource<List<Seat>>? =
            seatReservationDataSource.fetchRegisteredSeatByRegisteredPerson(registeredPerson)

    /*
        Método encargado de traer el asiento reservado por el número de asiento.
    */
    override suspend fun fetchRegisteredSeatBySeatNumber(seatNumber: Int): Resource<List<Seat>>? =
            seatReservationDataSource.fetchRegisteredSeatBySeatNumber(seatNumber)

    /*
        Método encargado de verificar si una persona ya tiene reservado un asiento.
    */
    override suspend fun checkSeatSavedByIdNumberUser(idNumberUser: String)
            : Resource<Boolean> = seatReservationDataSource.checkSeatSavedByIdNumberUser(idNumberUser)

    /*override suspend fun checkCouples(coupleNumber: String)
    : Flow<Resource<Boolean>> = seatReservationDataSource.checkCouples(coupleNumber)*/

    override suspend fun checkIfIsCoupleAvailable(coupleNumber: String)
    : Resource<Boolean> = seatReservationDataSource.checkIfIsCoupleAvailable(coupleNumber)

    override suspend fun updateIsCoupleAvailable(coupleNumber: String, isAvailable: Boolean) =
            seatReservationDataSource.updateIsCoupleAvailable(coupleNumber, isAvailable)

    override suspend fun loadAvailableCouples()
            : Flow<Resource<String>> = seatReservationDataSource.loadAvailableCouples()

    override suspend fun loadNoAvailableCouples()
    : Flow<Resource<String>> = seatReservationDataSource.loadNoAvailableCouples()

    override suspend fun checkIfIsThreesomeAvailable(threesomeNumber: String)
    : Resource<Boolean> = seatReservationDataSource.checkIfIsThreesomeAvailable(threesomeNumber)

    override suspend fun updateIsThreesomeAvailable(threesomeNumber: String, isAvailable: Boolean)
    = seatReservationDataSource.updateIsThreesomeAvailable(threesomeNumber, isAvailable)

    override suspend fun loadAvailableThreesomes()
    : Flow<Resource<String>> = seatReservationDataSource.loadAvailableThreesomes()

    override suspend fun loadNoAvailableThreesomes()
    : Flow<Resource<String>> = seatReservationDataSource.loadNoAvailableThreesomes()

    override suspend fun checkIfIsBubbleAvailable(bubbleNumber: String)
    : Resource<Boolean> = seatReservationDataSource.checkIfIsBubbleAvailable(bubbleNumber)

    override suspend fun updateIsBubbleAvailable(bubbleNumber: String, isAvailable: Boolean)
    = seatReservationDataSource.updateIsBubbleAvailable(bubbleNumber, isAvailable)

    override suspend fun loadAvailableBubbles()
    : Flow<Resource<String>> = seatReservationDataSource.loadAvailableBubbles()

    override suspend fun loadNoAvailableBubbles()
    : Flow<Resource<String>> = seatReservationDataSource.loadNoAvailableBubbles()

}