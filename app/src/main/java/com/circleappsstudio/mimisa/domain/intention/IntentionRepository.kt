// Repositorio encargado de interactuar con el DataSource "IntentionDataSource".

package com.circleappsstudio.mimisa.domain.intention

import com.circleappsstudio.mimisa.data.datasource.DataSource
import com.circleappsstudio.mimisa.data.model.Intention
import com.circleappsstudio.mimisa.domain.Repository
import com.circleappsstudio.mimisa.vo.Resource

class IntentionRepository(private val intentionDataSource: DataSource.Intentions) : Repository.Intentions {
    /*
        Método encargado de guardar una intención en la base de datos.
    */
    override suspend fun saveIntention(
            category: String,
            intention: String
    ) = intentionDataSource.saveIntention(category, intention)

    /*
        Método encargado de traer todas las intenciones guardadas por el usuario autenticado actual.
    */
    override suspend fun fetchSavedIntentionsByNameUser()
            : Resource<List<Intention>>? = intentionDataSource.fetchSavedIntentionsByNameUser()

}