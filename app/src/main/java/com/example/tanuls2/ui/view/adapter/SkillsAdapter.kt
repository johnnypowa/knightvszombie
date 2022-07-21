package com.example.tanuls2.ui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tanuls2.R
import com.example.tanuls2.model.Skill
import com.example.tanuls2.model.SkillName
import com.example.tanuls2.ui.viewmodel.SkillsViewModel
import kotlinx.android.synthetic.main.cell_item.view.*

class SkillsAdapter(val skillsViewModel: SkillsViewModel):RecyclerView.Adapter<SkillsAdapter.SkillsViewHolder>() {

    var skillList: ArrayList<Skill> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cell_item, parent, false)
        return SkillsViewHolder(itemView, skillsViewModel)
    }

    override fun onBindViewHolder(holder: SkillsViewHolder, position: Int) {
        val currentItem = skillList[position]
        holder.bind(currentItem, position)
    }

    override fun getItemCount(): Int {
        return skillList.size
    }

    inner class SkillsViewHolder(itemView: View, val skillsViewModel: SkillsViewModel) : RecyclerView.ViewHolder(itemView) {

        private lateinit var currentSkill: Skill

        init {
            itemView.cellId.setOnClickListener { view ->
                skillsViewModel.onSkillClicked(currentSkill, view)
            }
        }

        fun bind(item: Skill, position: Int) {
            currentSkill = item
            itemView.cellNameId.text = skillList[position].skillLabel

            when (currentSkill.skillName) {
                SkillName.DOUBLE_HIT-> {
                    itemView.cellPictureId.setImageResource(R.drawable.ic_brutal_hit)
                }
                SkillName.CRITICAL_HIT -> {
                    itemView.cellPictureId.setImageResource(R.drawable.ic_critical_hit)
                }
                SkillName.PRECISION_HIT -> {
                    itemView.cellPictureId.setImageResource(R.drawable.ic_precision_hit)
                }
                SkillName.LIFE_STEAL_HIT -> {
                    itemView.cellPictureId.setImageResource(R.drawable.ic_life_steal)
                }
                SkillName.HEAL -> {
                    itemView.cellPictureId.setImageResource(R.drawable.ic_healing)
                }
            }
            itemView.equippedLabelId.visibility = if (currentSkill.equipped) VISIBLE else GONE
        }

    }
}

