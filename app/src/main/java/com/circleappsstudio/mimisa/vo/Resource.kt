// Clase para retornar estados de objetos (Carga, Éxito y Fallo).

package com.circleappsstudio.mimisa.vo

import java.lang.Exception

sealed class Resource<out T> {
    // Cada método de la clase Resource() se retorna a sí misma (: Resource<T>())

    //Resource.Loading()
    class Loading<out T> : Resource<T>()

    //Resource.Success()
    data class Success<out T>(val data: T) : Resource<T>()

    //Resource.Failure()
    data class Failure<out T>(val exception: Exception) : Resource<T>()

}