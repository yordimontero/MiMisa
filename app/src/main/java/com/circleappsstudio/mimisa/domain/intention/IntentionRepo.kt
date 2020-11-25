package com.circleappsstudio.mimisa.domain.intention

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.domain.Repo

class IntentionRepo(private val intentionDataSource: DataSource.Intention) : Repo.Intention {

    override suspend fun saveIntention(
            category: String,
            intention: String
    ) = intentionDataSource.saveIntention(category, intention)

}