package com.example.automatedwhatsappmessage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.automatedwhatsappmessage.api.TemplateList
import com.example.automatedwhatsappmessage.databinding.TemplatelayoutBinding


class TemplateAdapter(
    private val context: Context,
    private val values: List<TemplateList>,
    private val listener:ClickListener) :
    RecyclerView.Adapter<TemplateAdapter.ViewHolder>() {

interface ClickListener{
    fun deleteList(position: Int, List: TemplateList)
    fun editList(position: Int, List: TemplateList)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TemplatelayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item = values[position]

        holder.name.text = item.templatetype
        holder.count.text = item.templatecontent
        holder.deleteList.setOnClickListener {
            listener.deleteList(position,item)
        }
        holder.view.setOnClickListener {
            listener.editList(position,item)
        }

        if (item.templateimage?.isNotEmpty() == true){

            holder.yes.text = "yes"
        }else{

            holder.yes.text = "No"

        }

        if (item.templatelocation?.isNotEmpty() == true){

            holder.no.text = "yes"
        }else{

            holder.no.text = "No"

        }


    }

    override fun getItemCount(): Int {
        return values.size
    }


    inner class ViewHolder(binding: TemplatelayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.textHeading
        val count: TextView = binding.textSubheading
        val view: TextView = binding.textView
        val yes: TextView = binding.textYes
        val no: TextView = binding.textNo
        val deleteList: ImageView = binding.delete

    }

}
