package com.example.tanuls2.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.tanuls2.R
import com.example.tanuls2.model.*
import com.example.tanuls2.ui.viewmodel.*
import com.example.tanuls2.util.SingleEvent
import com.example.tanuls2.util.observe
import kotlinx.android.synthetic.main.fragment_combat.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CombatFragment : Fragment() {

    lateinit var zombie: Zombie
    var defeatAlertdialog: AlertDialog? = null
    var victoryAlertdialog: AlertDialog? = null

    val combatViewModel: CombatViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe(combatViewModel.onceLiveEvent) {
            dataEvent(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_combat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        combatViewModel.loadAllContent()

        createDefeatPopup()
        createVictoryPopup()

        knightHitId.setOnClickListener {
            if (combatViewModel.currentKnight!!.currentHealth > 0 && zombie.currentHealth > 0) {
                hitWithKnight()
                hitWithZombie()
            }
        }

        knightSkill1Id.setOnClickListener {
            skillExtraDamage()
        }

        knightSkill2Id.setOnClickListener {
            skillExtraCriticalHitChance()
        }

        knightSkill3Id.setOnClickListener {
            skillEliminateBlockChance()
        }

        knightSkill4Id.setOnClickListener {
            skillBloodSiphon()
        }
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(combatToolbar as Toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.combat_toolbar_title)
    }

    fun dataEvent(event: SingleEvent?) {
        when (event) {
            is ShowAllContent -> {
                showAllData(event.knightData, event.zombieData)
                combatLoadingIndicatorId.visibility = View.GONE
                combatContainerId.visibility = View.VISIBLE
            }
            is ShowError -> {
                showErrorMessage(event.errorMessage)
            }
            is DroppedItem -> {
                putItemToInventory(event.droppedItem)
            }
        }

    }

    fun showErrorMessage(message: String?) {
        Toast.makeText(requireContext(), message ?: "Hiba történt.", Toast.LENGTH_SHORT).show()
    }

    fun showAllData(knightData: Knight, zombieData: Zombie){
        showKnightContent(knightData)
        showEnemyZombieContent(zombieData)
    }

    fun showKnightContent(knightData: Knight) {
        printKnightParameter(knightData)
    }

    fun showEnemyZombieContent(zombieData: Zombie) {
        zombie = zombieData
        printZombieParameter(zombie)
    }

    fun clearKnightHitResults() {
        knightHitResultId.text = ""
        zombieDefenseResultId.text = ""
        knightCriticalHitResultId.text = ""
    }

    fun clearZombieHitResults() {
        zombieHitResultId.text = ""
        knightDefenseResultId.text = ""
        zombieCriticalHitResultId.text = ""
    }

    fun printKnightParameter(knight: Knight) {
        knightLevelValue.text = knight.level.toString()
        knightHealthValue.text = knight.currentHealth.toString()
        knightExperienceValue.text = knight.experience.toString()
        knightDamageValue.text = knight.damage.toString()
        knightCriticalHitChanceValue.text =
            getString(R.string.percentage_value, (knight.criticalHitChance * 100).toInt())
        knightBlockChanceValue.text =
            getString(R.string.percentage_value, (knight.blockChance * 100).toInt())
        setupKnightHealthBar()
    }

    fun printZombieParameter(zombie: Zombie) {
        enemyLevelValue.text = zombie.level.toString()
        enemyHealthValue.text = zombie.currentHealth.toString()
        enemyExperienceValue.text = zombie.experience.toString()
        enemyDamageValue.text = zombie.damage.toString()
        enemyCriticalHitChanceValue.text =
            getString(R.string.percentage_value, (zombie.criticalHitChance * 100).toInt())
        enemyBlockChanceValue.text =
            getString(R.string.percentage_value, (zombie.blockChance * 100).toInt())
        setupZombieHealthBar()
    }

    fun createVictoryPopup() {
        victoryAlertdialog = AlertDialog.Builder(requireContext())
            .setMessage("Megölted a ellenfeled, gratulálok! Szépp munka!")
            .setTitle("Győzelem!")

            .setPositiveButton("Király! Kérem a következőt!") { dialog, p1 ->
                knightHitId.isEnabled = false
                dialog.dismiss()
                setUpVictoryStart()
                knightHitId.isEnabled = true
            }
            .setCancelable(false)
            .create()
    }

    fun createDefeatPopup() {
        defeatAlertdialog = AlertDialog.Builder(requireContext())
            .setMessage("Gratulálok meghaltál!")
            .setTitle("Lúzer!")
            .setPositiveButton("Persze, megpróbálom újra.") { dialog, p1 ->
                dialog.dismiss()
                setUpDefeatStart()
            }
            .setCancelable(false)
            .create()
    }

    fun putItemToInventory(item: Item){

        combatViewModel.currentKnight?.let { myKnight ->
            val firstEmptySlot = myKnight.itemList.firstOrNull { it.type == ItemType.EMPTY_SLOT }
            if (firstEmptySlot != null) {
                val indexOfEmptySlot = myKnight.itemList.indexOf(firstEmptySlot)
                myKnight.itemList.removeAt(indexOfEmptySlot)
                myKnight.itemList.add(indexOfEmptySlot, item)
                combatViewModel.saveKnightToDb()

                if (indexOfEmptySlot < 11) {
                    Toast.makeText(
                        requireContext(),
                        "Kaptál egy tárgyat: ${item.itemName}",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Kaptál egy tárgyat: ${item.itemName}. Tele a táskád!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Megtelt a táskád, nem tudtad felvenni a tárgyat!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun setUpVictoryStart() {
        combatViewModel.currentKnight?.let { myKnight ->
            zombie.currentHealth = zombie.maxHealth
            myKnight.currentHealth = myKnight.maxHealth
            myKnight.experience += 50
            combatViewModel.getDroppedItem()
            clearZombieHitResults()
            clearKnightHitResults()
            printKnightParameter(myKnight)
            printZombieParameter(zombie)
            skillsEnable()
        }
    }

    fun setUpDefeatStart() {
        combatViewModel.currentKnight?.let { myKnight ->
            zombie.currentHealth = zombie.maxHealth
            myKnight.currentHealth = myKnight.maxHealth
            myKnight.experience -= 50
            clearZombieHitResults()
            clearKnightHitResults()
            knightHitResultId.text = getString(R.string.start_fight)
            printKnightParameter(myKnight)
            printZombieParameter(zombie)
            skillsEnable()
        }
    }

    fun checkKnightHealthStatus() {
        combatViewModel.currentKnight?.let { myKnight ->
            if (myKnight.currentHealth <= 0) {
                if (defeatAlertdialog?.isShowing == false) {
                    defeatAlertdialog?.show()
                }
            }
        }
    }

    fun checkZombieHealthStatus() {
                if (victoryAlertdialog?.isShowing == false) {
                    if (zombie.currentHealth <= 0) {
                        victoryAlertdialog?.show()
                    }
                }
            }

    fun randomValueGenerator(): Float {
        val randomValue = (0..100).random()
        return randomValue / 100f
    }

    fun setupKnightHealthBar() {
        combatViewModel.currentKnight?.let { myKnight ->
            knightHealthBar.apply {
                max = myKnight.maxHealth
                progress = myKnight.currentHealth
            }
        }
    }

    fun setupZombieHealthBar() {
        zombieHealthBar.apply {
            max = zombie.maxHealth
            progress = zombie.currentHealth
        }
    }

    fun criticalHitKnight(): Boolean {
            return randomValueGenerator() <= combatViewModel.currentKnight!!.criticalHitChance
        }

    fun criticalHitZombie(): Boolean {
        return randomValueGenerator() <= zombie.criticalHitChance
    }

    fun blockChanceKnight(): Boolean {
            return randomValueGenerator() <= combatViewModel.currentKnight!!.blockChance
        }

    fun blockChanceZombie(): Boolean {
        return randomValueGenerator() <= zombie.blockChance
    }

    fun skillsEnable() {
        knightSkill1Id.isEnabled = true
        knightSkill1Id.alpha= 1.0f
        knightSkill2Id.isEnabled = true
        knightSkill2Id.alpha= 1.0f
        knightSkill3Id.isEnabled = true
        knightSkill3Id.alpha= 1.0f
        knightSkill4Id.isEnabled = true
        knightSkill4Id.alpha= 1.0f
    }

    fun skillDisable(appCompatImageView: AppCompatImageView){
        appCompatImageView.isEnabled = false
        appCompatImageView.alpha = 0.4f
    }

    fun skillExtraDamage() {
        clearZombieHitResults()
        hitWithKnight(extraDamage = 100)
        skillDisable(knightSkill1Id)
        printKnightParameter(combatViewModel.currentKnight!!)
        printZombieParameter(zombie)
    }

    fun skillExtraCriticalHitChance() {
        clearZombieHitResults()
        val originalCriticalHitChance = combatViewModel.currentKnight!!.criticalHitChance
        combatViewModel.currentKnight!!.criticalHitChance += (1.0f - originalCriticalHitChance)
        hitWithKnight()
        combatViewModel.currentKnight!!.criticalHitChance = originalCriticalHitChance
        skillDisable(knightSkill2Id)
        printKnightParameter(combatViewModel.currentKnight!!)
        printZombieParameter(zombie)
    }

    fun skillEliminateBlockChance() {
        clearZombieHitResults()
        val originalBlockChance = zombie.blockChance
        zombie.blockChance = 0.0f
        hitWithKnight()
        zombie.blockChance = originalBlockChance
        skillDisable(knightSkill3Id)
        printKnightParameter(combatViewModel.currentKnight!!)
        printZombieParameter(zombie)
    }

    fun skillBloodSiphon() {
        clearZombieHitResults()
        val actualHealthOfEnemy = zombie.currentHealth
        hitWithKnight()
        val modifiedHealth =
            combatViewModel.currentKnight!!.currentHealth + (actualHealthOfEnemy - zombie.currentHealth)
        combatViewModel.currentKnight!!.currentHealth = when (modifiedHealth >= combatViewModel.currentKnight!!.maxHealth) {
            true -> {
                combatViewModel.currentKnight!!.maxHealth
            }
            else -> {
                modifiedHealth
            }
        }
        skillDisable(knightSkill4Id)
        printKnightParameter(combatViewModel.currentKnight!!)
        printZombieParameter(zombie)
    }

    fun hitWithKnight( extraDamage: Int = 0) {

        clearKnightHitResults()
        if (blockChanceZombie()) {
            zombieDefenseResultId.text = getString(R.string.successfull_block)
            knightHitResultId.text = getString(R.string.notsuccessfull_hit)
        } else {
            if (criticalHitKnight()) {
                zombie.currentHealth -= combatViewModel.currentKnight!!.damage * 2 + extraDamage
                knightHitResultId.text = getString(R.string.successfull_hit)
                knightCriticalHitResultId.text = getString(R.string.critical_hit)
            } else {
                zombie.currentHealth -= combatViewModel.currentKnight!!.damage + extraDamage
                knightHitResultId.text = getString(R.string.successfull_hit)
            }
        }
        checkKnightHealthStatus()
        checkZombieHealthStatus()
        printKnightParameter(combatViewModel.currentKnight!!)
        printZombieParameter(zombie)
    }

    fun hitWithZombie() {

        clearZombieHitResults()
        if (blockChanceKnight()) {
            knightDefenseResultId.text = getString(R.string.successfull_block)
            zombieHitResultId.text = getString(R.string.notsuccessfull_hit)
        } else {
            if (criticalHitZombie()) {
                combatViewModel.currentKnight!!.currentHealth -= zombie.damage * 2
                zombieHitResultId.text =
                    getString(R.string.successfull_hit)
                zombieCriticalHitResultId.text =
                    getString(R.string.critical_hit)
            } else {
                combatViewModel.currentKnight!!.currentHealth -= zombie.damage
                zombieHitResultId.text = getString(R.string.successfull_hit)
            }
        }
        checkKnightHealthStatus()
        checkZombieHealthStatus()
        printKnightParameter(combatViewModel.currentKnight!!)
        printZombieParameter(zombie)
    }
}