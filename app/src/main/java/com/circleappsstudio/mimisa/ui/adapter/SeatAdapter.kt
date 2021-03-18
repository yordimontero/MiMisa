package com.circleappsstudio.mimisa.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseViewHolder
import com.circleappsstudio.mimisa.data.model.Seat
import kotlinx.android.synthetic.main.seat_row.view.*
import java.lang.IllegalArgumentException

class SeatAdapter(private val context: Context,
                  private val seatList: List<Seat>): RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        // Retorno de la clase encargada de bindear cada elemento del RecyclerView.
        // Infla el resouce de vista (layout) creado para mostrar la información.
        return MainSeatViewHolder(
            LayoutInflater.from(context).inflate(R.layout.seat_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            // Bindeo de cada elemento del RecyclerView.
            is MainSeatViewHolder -> holder.bind(seatList[position], position)
            else -> throw IllegalArgumentException("Error on bind.")
        }
    }

    override fun getItemCount(): Int = seatList.size

    inner class MainSeatViewHolder(itemView: View): BaseViewHolder<Seat>(itemView) {

        override fun bind(item: Seat, position: Int) {
            // Creación de cada elemento a "dibujar" en el RecyclerView.

            if (item.seatCategory.isEmpty()){
                itemView.layout_category.visibility = View.GONE
            } else {
                itemView.txt_category_row.text = item.seatCategory
            }

            itemView.txt_seat_number_row.text = item.seatNumber
            itemView.txt_name_user_row.text = item.nameUser
            itemView.txt_lastname_user_row.text = item.lastNameUser
            itemView.txt_id_number_row.text = item.idNumberUser
            itemView.txt_date_seat_row.text = item.dateRegistered

            if (item.seatRegisteredBy.isEmpty()){
                itemView.layout_seat_registered_by.visibility = View.GONE
            } else {
                itemView.txt_seat_registered_by_row.text = item.seatRegisteredBy
            }

        }

    }

}