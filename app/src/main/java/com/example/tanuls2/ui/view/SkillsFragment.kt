package com.example.tanuls2.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.tanuls2.R
import kotlinx.android.synthetic.main.fragment_inventory.*
import kotlinx.android.synthetic.main.fragment_skills.*

class SkillsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

}