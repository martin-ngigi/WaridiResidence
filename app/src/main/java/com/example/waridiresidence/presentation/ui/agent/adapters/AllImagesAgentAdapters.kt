package com.example.waridiresidence.presentation.ui.agent.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.waridiresidence.data.model.modelresponse.house.AllImagesResponse
import com.example.waridiresidence.databinding.RowSpecialBinding

class AllImagesAgentAdapters(
    var items: List<AllImagesResponse>,
    ): RecyclerView.Adapter<AllImagesAgentAdapters.AllImagesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllImagesViewHolder {
        val itemView = RowSpecialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllImagesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AllImagesViewHolder, position: Int) {
        var currentImageItem = items[position]

        //set data
        holder.bind(currentImageItem)

        //or
        holder.itemView.setOnClickListener{ view ->
            //view.findNavController().navigate(R.id.action_allHousesAgentFragment2_to_singleHouseAgentFragment)
            //save current list in Constants as to be retrieved upon reaching SingleHouseAgentFragment.. i.e. Passing data
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class AllImagesViewHolder(val binding: RowSpecialBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item : AllImagesResponse){
            binding.tvHouse.text = item.title
            binding.tvDescription.text = item.description
            //set image
            Glide
                .with(binding.root)
                .load(item.url)
                .into(binding.moduleIv)

        }


    }
}