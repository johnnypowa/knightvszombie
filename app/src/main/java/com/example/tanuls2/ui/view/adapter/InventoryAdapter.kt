package com.example.tanuls2.ui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tanuls2.R
import com.example.tanuls2.model.Item
import com.example.tanuls2.model.ItemType
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
            itemView.cellId.setOnClickListener { view ->
                if (currentItem.type != ItemType.EMPTY_SLOT) {
                    inventoryViewModel.onItemClicked(currentItem, view)
                }
                false
            }
        }

        fun bind(item: Item, position: Int) {
            currentItem = item
            itemView.cellNameId.text = itemList[position].itemName
            when(currentItem.type){
                ItemType.WEAPON -> { itemView.cellPictureId.setImageResource(R.drawable.ic_swords) }
                ItemType.POTION -> { itemView.cellPictureId.setImageResource(R.drawable.ic_potion) }
                ItemType.ARMOR -> { itemView.cellPictureId.setImageResource(R.drawable.ic_armor) }
                ItemType.JEWELLERY -> { itemView.cellPictureId.setImageResource(R.drawable.ic_treasure) }
                ItemType.EMPTY_SLOT -> { itemView.cellPictureId.setImageResource(R.drawable.ic_empty) }
            }
            itemView.equippedLabelId.visibility = if (currentItem.equipped) VISIBLE else GONE
        }

    }

}

