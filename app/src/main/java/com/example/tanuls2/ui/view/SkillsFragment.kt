package com.example.tanuls2.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tanuls2.R
import com.example.tanuls2.model.Skill
import com.example.tanuls2.ui.view.adapter.SkillsAdapter
import com.example.tanuls2.ui.viewmodel.*
import com.example.tanuls2.util.SingleEvent
import com.example.tanuls2.util.observe
import kotlinx.android.synthetic.main.fragment_skills.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SkillsFragment : Fragment() {

    val skillsViewModel: SkillsViewModel by viewModel()
    lateinit var skillsAdapter: SkillsAdapter

    override fun onStart() {
        super.onStart()
        skillsViewModel.loadAllSkills()
    }

    override fun onStop() {
        super.onStop()
        skillsViewModel.skillList = arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe(skillsViewModel.onceLiveEvent) {
            dataEvent(it)

        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        return inflater.inflate(R.layout.fragment_skills, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(skillsToolbar as Toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.skills_toolbar_title)
    }

    fun dataEvent(event: SingleEvent?) {
        when (event) {
            is LoadSkills -> {loadSkills(event.loadSkillList)}
            is ClickOnSkill -> {showPopupMenu(event.item, event.view)}
            is SuccessfulSave -> {
                skillsLoadingIndicator(false)
            }
        }
    }

    fun skillsLoadingIndicator(active:Boolean) {
        if (active) {
            skillsLoadingIndicatorId.visibility = View.VISIBLE
            skillsLoadingIndicatorId.bringToFront()
        } else {
            skillsLoadingIndicatorId.visibility = View.GONE
        }
    }

    fun loadSkills(loadSkillList: ArrayList<Skill>) {
        bagRecyclerViewSkills.apply {
            adapter = SkillsAdapter(skillsViewModel).also { skillsAdapter = it }
            layoutManager = GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        }

        skillsAdapter.skillList.addAll(loadSkillList)
    }

    fun showPopupMenu(item: Skill, view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(if (item.equipped) R.menu.menu_skill_popup_equipped else R.menu.menu_skill_popup_unequipped, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { selectedMenu ->
            when (selectedMenu.itemId) {
                R.id.skillEquip -> {
                    item.equipped = true
                    skillsAdapter.notifyDataSetChanged()
                    skillsLoadingIndicator(true)
                    skillsViewModel.saveKnightToDb()
                }
                R.id.skillUnEquip -> {
                    item.equipped = false
                    skillsAdapter.notifyDataSetChanged()
                    skillsLoadingIndicator(true)
                    skillsViewModel.saveKnightToDb()
                }
            }
            true
        }

        popupMenu.show()

    }

}