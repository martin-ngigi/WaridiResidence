package com.example.waridiresidence.presentation.ui.agent.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.waridiresidence.data.model.modelresponse.house.AllHousesResult
import com.example.waridiresidence.databinding.RowHomesBinding
import com.example.waridiresidence.presentation.ui.agent.viewmodels.AllHousesAgentViewModel

class AllHomesAgentAdapters(
    var items: List<AllHousesResult>,
    private  val viewModel: AllHousesAgentViewModel): RecyclerView.Adapter<AllHomesAgentAdapters.AllHomesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllHomesViewHolder {
        val itemView = RowHomesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllHomesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AllHomesViewHolder, position: Int) {
        var currentHouseItem = items[position]

        //set data
        holder.bind(currentHouseItem)

        //onclick
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class AllHomesViewHolder(val binding: RowHomesBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item : AllHousesResult){
            binding.houseTv.text = item.title
        }


    }
}