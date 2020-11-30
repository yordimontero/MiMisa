package com.circleappsstudio.mimisa.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.base.BaseViewHolder
import com.circleappsstudio.mimisa.data.model.Intention
import kotlinx.android.synthetic.main.intention_row.view.*
import java.lang.IllegalArgumentException

class IntentionAdapter(
        private val context: Context,
        private val intentionList: List<Intention>
): RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {

        return MainIntentionViewHolder(
                LayoutInflater.from(context).inflate(R.layout.intention_row, parent, false)
        )

    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {

        when(holder){
            is  MainIntentionViewHolder -> holder.bind(intentionList[position], position)
            else -> throw IllegalArgumentException("Error on bind.")
        }

    }

    override fun getItemCount(): Int = intentionList.size

    inner class MainIntentionViewHolder(itemView: View): BaseViewHolder<Intention>(itemView){

        override fun bind(item: Intention, position: Int) {

            itemView.txt_user_name_intention_row.text = item.userName
            itemView.txt_category_intention_row.text = item.category
            itemView.txt_intention_row.text = item.intention

        }

    }

}