package com.circleappsstudio.mimisa.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseViewHolder
import com.circleappsstudio.mimisa.data.model.Seat
import kotlinx.android.synthetic.main.reserved_seats_row.view.*
import java.lang.IllegalArgumentException

class RegisteredSeatsAdapter(private val context: Context,
                             private val seatList: List<Seat>): RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        // Retorno de la clase encargada de bindear cada elemento del RecyclerView.
        // Infla el resouce de vista (layout) creado para mostrar la información.
        return MainViewHolder(
            LayoutInflater.from(context).inflate(R.layout.reserved_seats_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            // Bindeo de cada elemento del RecyclerView.
            is MainViewHolder -> holder.bind(seatList[position], position)
            else -> throw IllegalArgumentException("Error on bind.")
        }
    }

    override fun getItemCount(): Int = seatList.size

    inner class MainViewHolder(itemView: View): BaseViewHolder<Seat>(itemView) {

        override fun bind(item: Seat, position: Int) {
            // Creación de cada elemento a "dibujar" en el RecyclerView.

            itemView.txt_seat_number_row.text = item.seatNumber.toString()
            itemView.txt_name_user_row.text = item.nameUser
            itemView.txt_id_number_row.text = item.idNumberUser
            itemView.txt_seat_reserved_by_row.text = item.seatRegisteredBy

        }

    }

}