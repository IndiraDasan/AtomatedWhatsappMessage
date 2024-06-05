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
import com.example.automatedwhatsappmessage.api.SubsCriptionData
import com.example.automatedwhatsappmessage.databinding.CallogslayoutBinding
import com.example.automatedwhatsappmessage.databinding.SubscriptionlayoutBinding


class SubcribtionAdapter(
    private val context: Context,
    private val values: List<SubsCriptionData>,
    private val listener:clicklistener
) :
    RecyclerView.Adapter<SubcribtionAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SubscriptionlayoutBinding.inflate(
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


        holder.name.text = item.heading
        holder.subHeading.text = item.subHeading
        holder.days.text = item.days
        holder.click.setOnClickListener {
            listener.imageclick()
        }



    }

    override fun getItemCount(): Int {
        return values.size
    }


    inner class ViewHolder(binding: SubscriptionlayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.heading
        val subHeading: TextView = binding.subheading
        val days: TextView = binding.days
        val click: CardView = binding.cardview

    }

    interface clicklistener{

        fun imageclick()

    }


}
