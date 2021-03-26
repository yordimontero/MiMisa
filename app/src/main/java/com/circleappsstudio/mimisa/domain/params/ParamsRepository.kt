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
        Método encargado de escuchar en tiempo real el versionCode.
    */
    override suspend fun fetchVersionCode()
            : Flow<Resource<Int>> = paramsDataSource.fetchVersionCode()

    /*
        Método encargado de escuchar en tiempo real si el registro de intenciones esta habilitado
        o deshabilitado.
    */
    override suspend fun fetchIsRegisterIntentionAvailable()
            : Flow<Resource<Boolean>> = paramsDataSource.fetchIsRegisterIntentionAvailable()

    /*
        Método encargado de habilitar o deshabilitar el registro de intenciones.
    */
    override suspend fun setIsRegisterIntentionAvailable(isRegisterIntentionAvailable: Boolean) =
            paramsDataSource.setIsRegisterIntentionAvailable(isRegisterIntentionAvailable)

}