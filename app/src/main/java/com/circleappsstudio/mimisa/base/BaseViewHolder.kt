package com.circleappsstudio.mimisa.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView) {
    // El parámetro genérico "T" hace referencia al modelo que se "dibujará" en el RecyclerView.

    abstract fun bind(item: T, position: Int)

}