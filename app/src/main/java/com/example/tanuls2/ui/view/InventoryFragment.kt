package com.example.tanuls2.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ShareActionProvider
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tanuls2.R
import com.example.tanuls2.handler.SharedPreferencesHandler
import com.example.tanuls2.model.Armor
import com.example.tanuls2.model.Item
import com.example.tanuls2.model.Jewellery
import com.example.tanuls2.model.Weapon
import com.example.tanuls2.ui.view.adapter.InventoryAdapter
import com.example.tanuls2.ui.viewmodel.InventoryViewModel
import com.example.tanuls2.ui.viewmodel.ItemClick
import com.example.tanuls2.util.SingleEvent
import com.example.tanuls2.util.observe
import kotlinx.android.synthetic.main.fragment_inventory.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class InventoryFragment : Fragment() {

    val inventoryViewModel: InventoryViewModel by viewModel()
    val inventoryAdapter: InventoryAdapter by lazy { InventoryAdapter(inventoryViewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe(inventoryViewModel.onceLiveEvent) {
            dataEvent(it)
        }
    }

    fun dataEvent(event: SingleEvent?) {
        when(event) {
            is ItemClick -> { Toast.makeText(requireContext(), event.item.itemName, Toast.LENGTH_SHORT).show() }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = SharedPreferencesHandler.storedItemList
//            arrayListOf<Item>(
//            Weapon("Big fucking sword"),
//            Armor("Chest armor"),
//            Jewellery("Necklace"),
//            Weapon("Dagger"),
//            Armor("Helmet"),
//            Jewellery("Ring"),
//            Weapon("Knife"),
//            Armor("Boots"),
//            Jewellery("Earings"),
//            Weapon("Fiery sword"),
//            Armor("Gloves"),
//            Jewellery("Tiara")
//        )

        bagRecyclerViewId.apply {
            adapter = inventoryAdapter
            layoutManager = GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        }

        inventoryAdapter.itemList.addAll(items)
    }

}