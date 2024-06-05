package com.example.automatedwhatsappmessage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.automatedwhatsappmessage.R
import com.example.automatedwhatsappmessage.api.CallLogData
import com.example.automatedwhatsappmessage.databinding.CallogslayoutBinding


class CallLogAdapter(
    private val context: Context,
    private val values: List<CallLogData>,
) :
    RecyclerView.Adapter<CallLogAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CallogslayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item = values[position]

        holder.name.isSelected = true


        holder.name.text = item.callLogName
        holder.count.text = item.count



    }

    override fun getItemCount(): Int {
        return values.size
    }


    inner class ViewHolder(binding: CallogslayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val cardClick: CardView = binding.cardview
        val name: TextView = binding.textCalllogname
        val count: TextView = binding.textCallcount

    }

}
