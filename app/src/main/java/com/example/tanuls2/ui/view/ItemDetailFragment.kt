package com.example.tanuls2.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tanuls2.R
import com.example.tanuls2.model.Item
import com.example.tanuls2.model.ItemType
import kotlinx.android.synthetic.main.fragment_combat.*
import kotlinx.android.synthetic.main.fragment_item_datail.*

class ItemDetailFragment: Fragment() {

    val args : ItemDetailFragmentArgs by navArgs()

    lateinit var item : Item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_datail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        item = args.item

        setupToolbar()
        itemImageChange()
        loadItemProperties()

    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(itemDetailToolbar as Toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.item_details_toolbar_title)
        (itemDetailToolbar as Toolbar).setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun itemImageChange(){

        when(item.type){
            ItemType.WEAPON -> {itemImage.setImageResource(R.drawable.ic_swords)}
            ItemType.JEWELLERY ->{itemImage.setImageResource(R.drawable.ic_treasure)}
            ItemType.ARMOR ->{itemImage.setImageResource(R.drawable.ic_armor)}
            ItemType.POTION ->{itemImage.setImageResource(R.drawable.ic_potion)}
        }
    }

    fun loadItemProperties(){

        itemName.text= item.itemName



    }

}