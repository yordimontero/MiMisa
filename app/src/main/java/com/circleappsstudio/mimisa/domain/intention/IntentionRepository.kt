package com.circleappsstudio.mimisa.domain.intention

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.data.model.Intention
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.vo.Resource

class IntentionRepository(private val intentionDataSource: DataSource.Intentions) : Repository.Intentions {

    override suspend fun saveIntention(
            category: String,
            intention: String
    ) = intentionDataSource.saveIntention(category, intention)

    override suspend fun fetchSavedIntentionsByUserName():
            Resource<List<Intention>>? = intentionDataSource.fetchSavedIntentionsByNameUser()

}