package com.example.tanuls2.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tanuls2.model.Item
import com.example.tanuls2.util.OnceLiveEvent
import com.example.tanuls2.util.SingleEvent

class InventoryViewModel : ViewModel() {

    val onceLiveEvent = OnceLiveEvent<SingleEvent>() //Live data

    fun onItemClicked(item: Item) {
        onceLiveEvent.postValue(ItemClick(item))
    }
}

class ItemClick(val item: Item) : SingleEvent