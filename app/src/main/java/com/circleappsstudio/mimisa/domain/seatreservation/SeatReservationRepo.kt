package com.circleappsstudio.mimisa.domain.seatreservation

import com.circleappsstudio.mimisa.data.DataSource
import com.circleappsstudio.mimisa.domain.Repo
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.coroutines.flow.Flow

class SeatReservationRepo(
        private val seatReservationDataSource: DataSource.SeatReservation
) : Repo.SeatReservation {

    override suspend fun fetchIterator(): Flow<Resource<Int>> = seatReservationDataSource.fetchIterator()

    override suspend fun saveSeatReserved(
            seatNumber: Int,
            nameUser: String,
            idNumberUser: String
    ) = seatReservationDataSource.saveSeatReserved(seatNumber, nameUser, idNumberUser)

}