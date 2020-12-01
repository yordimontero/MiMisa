package com.circleappsstudio.mimisa.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.data.model.IntentionSpinner
import kotlinx.android.synthetic.main.intention_spinner_row.view.*

class IntentionSpinnerAdapter(context: Context, intentionList: List<IntentionSpinner>)
    : ArrayAdapter<IntentionSpinner>(context, 0, intentionList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        val intention = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.intention_spinner_row, parent, false)
        view.img_category_intention_intention_spinner_row.setImageResource(intention!!.image)
        view.txt_category_intention_intention_spinner_row.text = intention.name

        return view
    }
}