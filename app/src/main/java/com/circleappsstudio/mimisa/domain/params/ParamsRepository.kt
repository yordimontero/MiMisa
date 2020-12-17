package com.circleappsstudio.mimisa.domain.params

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.vo.Resource
import kotlinx.coroutines.flow.Flow

class ParamsRepository(private val paramsDataSource: DataSource.Params): Repository.Params {

    override suspend fun fetchIsAvailable()
            : Flow<Resource<Boolean>> = paramsDataSource.fetchIsAvailable()

    override suspend fun setIsAvailable(isAvailable: Boolean)
            = paramsDataSource.setIsAvailable(isAvailable)

    override suspend fun fetchIterator(): Flow<Resource<Int>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchSeatLimit(): Resource<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSeatLimit(seatLimit: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchVersionCode()
            : Flow<Resource<Int>> = paramsDataSource.fetchVersionCode()

}