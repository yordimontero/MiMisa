package com.circleappsstudio.mimisa.data.model

import com.circleappsstudio.mimisa.R

data class IntentionSpinner(val image: Int, val name: String)

object Intentions {

    private val images = intArrayOf(
        R.drawable.ic_text,
        R.drawable.ic_thumb_up,
        R.drawable.ic_deceased,
        R.drawable.ic_cake
    )

    private val categories = arrayOf(
        "Seleccione una categoría",
        "Acción de Gracias",
        "Difuntos",
        "Cumpleaños"
    )

    var list: ArrayList<IntentionSpinner>? = null
        get() {

            if (field != null)
                return field

            field = ArrayList()
            for (i in images.indices) {

                val imageId = images[i]
                val intentionName = categories[i]

                val intentionSpinner = IntentionSpinner(imageId, intentionName)
                field!!.add(intentionSpinner)
            }

            return field
        }
}