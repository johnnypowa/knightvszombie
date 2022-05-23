package com.example.tanuls2.ui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tanuls2.R
import com.example.tanuls2.model.Item
import com.example.tanuls2.ui.viewmodel.InventoryViewModel
import kotlinx.android.synthetic.main.cell_item.view.*

class InventoryAdapter(val inventoryViewModel: InventoryViewModel) : RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {

    var itemList: ArrayList<Item> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cell_item, parent, false)
        return InventoryViewHolder(itemView, inventoryViewModel)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.bind(currentItem, position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class InventoryViewHolder(itemView: View, val inventoryViewModel: InventoryViewModel) : RecyclerView.ViewHolder(itemView) {

        private lateinit var currentItem: Item

        init {
            itemView.cellId.setOnClickListener {
                inventoryViewModel.onItemClicked(currentItem)
            }
        }

        fun bind(item: Item, position: Int) {
            currentItem = item
            itemView.cellNameId.text = itemList[position].itemName
            itemView.cellPictureId.setImageResource(com.google.android.material.R.drawable.ic_clock_black_24dp)
        }

    }

}

