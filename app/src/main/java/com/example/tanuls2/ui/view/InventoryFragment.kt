package com.example.tanuls2.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tanuls2.R
import com.example.tanuls2.handler.SharedPreferencesHandler
import com.example.tanuls2.model.*
import com.example.tanuls2.ui.view.adapter.InventoryAdapter
import com.example.tanuls2.ui.viewmodel.InventoryViewModel
import com.example.tanuls2.ui.viewmodel.ItemClick
import com.example.tanuls2.ui.viewmodel.LoadInventory
import com.example.tanuls2.util.SingleEvent
import com.example.tanuls2.util.observe
import kotlinx.android.synthetic.main.fragment_combat.*
import kotlinx.android.synthetic.main.fragment_inventory.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class InventoryFragment : Fragment() {

    val inventoryViewModel: InventoryViewModel by viewModel()
    lateinit var inventoryAdapter: InventoryAdapter


    override fun onStart() {
        super.onStart()
        if (inventoryViewModel.currentItems.isEmpty()) {
            inventoryViewModel.loadAllInventory()
        } else {
            loadItemList(inventoryViewModel.currentItems)
        }
    }

    override fun onStop() {
        super.onStop()
        inventoryViewModel.currentItems = arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe(inventoryViewModel.onceLiveEvent) {
            dataEvent(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(inventoryToolbar as Toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.inventory_toolbar_title)
    }

    fun dataEvent(event: SingleEvent?) {
        when (event) {
            is ItemClick -> {
                showPopupMenu(event.item, event.view)
                //Toast.makeText(requireContext(), event.item.itemName, Toast.LENGTH_SHORT).show()
            }
            is LoadInventory -> {loadItemList(event.loadItemList)}
        }
    }

    fun showPopupMenu(item: Item, view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(if (item.equipped) R.menu.menu_item_popup_equipped else R.menu.menu_item_popup_unequipped, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { selectedMenu ->
            when (selectedMenu.itemId) {
                R.id.itemDetails -> { navigateToDetail(item) }
                R.id.itemEquip -> {
                    item.equipped = true
                    inventoryAdapter.notifyDataSetChanged()
                    SharedPreferencesHandler.storedItemList = inventoryViewModel.currentItems
                }
                R.id.itemUnEquip -> {
                    item.equipped = false
                    inventoryAdapter.notifyDataSetChanged()
                    SharedPreferencesHandler.storedItemList = inventoryViewModel.currentItems
                }
                R.id.itemSell -> {
                    val index = inventoryAdapter.itemList.indexOf(item)
                    inventoryAdapter.itemList.removeAt(index)
                    inventoryAdapter.itemList.add(index, EmptySlot())
                    inventoryAdapter.notifyDataSetChanged()
                    inventoryViewModel.currentItems = inventoryAdapter.itemList
                    SharedPreferencesHandler.storedItemList = inventoryViewModel.currentItems
                }
            }
            true
        }

        popupMenu.show()

    }

    fun loadItemList(loadItemList: ArrayList<Item>) {
        bagRecyclerViewId.apply {
            adapter = InventoryAdapter(inventoryViewModel).also { inventoryAdapter = it }
            layoutManager = GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        }

        inventoryAdapter.itemList.addAll(loadItemList)
    }

    fun navigateToDetail(item: Item) {
        val action = InventoryFragmentDirections.actionInventoryToItemDetails(item)
        findNavController().navigate(action)
    }
}