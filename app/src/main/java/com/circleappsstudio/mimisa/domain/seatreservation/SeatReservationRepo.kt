package com.circleappsstudio.mimisa.domain.seatreservation

import com.circleappsstudio.mimisa.data.DataSource
import com.circleappsstudio.mimisa.domain.Repo
import com.circleappsstudio.mimisa.vo.Resource

class SeatReservationRepo(
        private val seatReservationDataSource: DataSource.SeatReservation
) : Repo.SeatReservation {

    override suspend fun fetchIterator(): Resource<Int?> {
        return seatReservationDataSource.fetchIterator()
    }

}