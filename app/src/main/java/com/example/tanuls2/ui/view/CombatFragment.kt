package com.example.tanuls2.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.tanuls2.R
import com.example.tanuls2.handler.SharedPreferencesHandler
import com.example.tanuls2.model.*
import com.example.tanuls2.ui.viewmodel.*
import com.example.tanuls2.util.SingleEvent
import com.example.tanuls2.util.observe
import kotlinx.android.synthetic.main.fragment_combat.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CombatFragment : Fragment() {

    lateinit var myKnight: Knight
    lateinit var enemyZombie: Zombie
    var defeatAlertdialog: AlertDialog? = null
    var victoryAlertdialog: AlertDialog? = null
    //val strongZombie = Zombie(health = 2000)

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

        combatViewModel.loadAllContent()
        //combatViewModel.loadEnemyZombieData()
        //enemyZombie = combatViewModel.loadEnemyZombieData()
        createDefeatPopup()
        createVictoryPopup()

        knightHitId.setOnClickListener {
            if (myKnight.currentHealth > 0 || enemyZombie.currentHealth > 0) {
                hitWith(myKnight)
                hitWith(enemyZombie)
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
        }

    }


    fun showErrorMessage(message: String?) {
        Toast.makeText(requireContext(), message ?: "Hiba történt.", Toast.LENGTH_SHORT).show()
    }

    fun showAllData(knightData: Knight,zombieData: Zombie){
        showKnightContent(knightData)
        showEnemyZombieContent(zombieData)
    }

    fun showKnightContent(knightData: Knight) {
        myKnight = knightData
        setupKnightHealthBar()
        printKnightParameter(myKnight)
    }

    fun showEnemyZombieContent(zombieData: Zombie) {
        enemyZombie = zombieData
        setupZombieHealthBar()
        printZombieParameter(enemyZombie)
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
        //Toast.makeText(this,"ez egy szöveg", Toast.LENGTH_LONG).show()
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
        //Toast.makeText(this,"ez egy szöveg", Toast.LENGTH_LONG).show()
    }

    fun putItemToInventory(){

        val item = Weapon("BarbárKard",1, damage = 10, price = 2)

        val firstEmptySlot = myKnight.itemList.firstOrNull { it.type == ItemType.EMPTY_SLOT }
        if(firstEmptySlot != null) {
            val indexOfEmptySlot = myKnight.itemList.indexOf(firstEmptySlot)
            myKnight.itemList.removeAt(indexOfEmptySlot)
            myKnight.itemList.add(indexOfEmptySlot, item)
            SharedPreferencesHandler.storedItemList = myKnight.itemList

            if (indexOfEmptySlot < 11) {
                Toast.makeText(requireContext(), "Kaptál egy tárgyat: ${item.itemName}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Kaptál egy tárgyat: ${item.itemName}. Tele a táskád!", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(), "Megtelt a táskád, nem tudtad felvenni a tárgyat!", Toast.LENGTH_LONG).show()
        }
    }

    fun setUpVictoryStart() {
        enemyZombie.currentHealth = enemyZombie.maxHealth
        myKnight.currentHealth = myKnight.maxHealth
        myKnight.experience += 50
        putItemToInventory()
        SharedPreferencesHandler.storedKnight = myKnight
        clearZombieHitResults()
        clearKnightHitResults()
        knightHitResultId.text = getString(R.string.start_fight)
        printKnightParameter(myKnight)
        printZombieParameter(enemyZombie)
        skillsEnable()
        setupKnightHealthBar()
        setupZombieHealthBar()
    }

    fun setUpDefeatStart() {
        enemyZombie.currentHealth = enemyZombie.maxHealth
        myKnight.currentHealth = myKnight.maxHealth
        myKnight.experience -= 50
        clearZombieHitResults()
        clearKnightHitResults()
        knightHitResultId.text = getString(R.string.start_fight)
        printKnightParameter(myKnight)
        printZombieParameter(enemyZombie)
        skillsEnable()
        setupKnightHealthBar()
        setupZombieHealthBar()
    }

    fun checkHealthStatus(character: Character) {

        when (character) {
            is Knight -> {
                if (myKnight.currentHealth <= 0) {
                    if (defeatAlertdialog?.isShowing == false) {
                        defeatAlertdialog?.show()
                    }
                }
            }
            is Zombie -> {
                if (victoryAlertdialog?.isShowing == false) {
                    if (enemyZombie.currentHealth <= 0) {
                        victoryAlertdialog?.show()
                    }
                }

            }
        }
    }

    fun randomValueGenerator(): Float {
        val randomValue = (0..100).random()
        return randomValue / 100f
    }

    fun setupKnightHealthBar() {
        knightHealthBar.apply {
            max = myKnight.maxHealth
            progress = myKnight.currentHealth
        }
    }

    fun setupZombieHealthBar() {
        zombieHealthBar.apply {
            max = enemyZombie.maxHealth
            progress = enemyZombie.currentHealth
        }
    }

    fun criticalHit(character: Character): Boolean? {
        return when (character) {
            is Knight -> {
                randomValueGenerator() <= myKnight.criticalHitChance
            }
            is Zombie -> {
                randomValueGenerator() <= enemyZombie.criticalHitChance
            }
            else -> {
                null
            }
        }
    }

    fun blockChance(character: Character): Boolean? {
        return when (character) {
            is Knight -> {
                randomValueGenerator() <= myKnight.blockChance
            }
            is Zombie -> {
                randomValueGenerator() <= enemyZombie.blockChance
            }
            else -> {
                null
            }
        }
    }

    fun skillsEnable() {
        knightSkill1Id.isEnabled = true
        knightSkill2Id.isEnabled = true
        knightSkill3Id.isEnabled = true
        knightSkill4Id.isEnabled = true
    }

    fun skillExtraDamage() {
        clearZombieHitResults()
        hitWith(myKnight, extraDamage = 100)

        knightSkill1Id.isEnabled = false
        printKnightParameter(myKnight)
        printZombieParameter(enemyZombie)
        setupKnightHealthBar()
        setupZombieHealthBar()
    }

    fun skillExtraCriticalHitChance() {
        clearZombieHitResults()
        val originalCriticalHitChance = myKnight.criticalHitChance
        myKnight.criticalHitChance += (1.0f - originalCriticalHitChance)
        hitWith(myKnight)
        myKnight.criticalHitChance = originalCriticalHitChance

        knightSkill2Id.isEnabled = false
        printKnightParameter(myKnight)
        printZombieParameter(enemyZombie)
        setupKnightHealthBar()
        setupZombieHealthBar()
    }

    fun skillEliminateBlockChance() {
        clearZombieHitResults()
        val originalBlockChance = enemyZombie.blockChance
        enemyZombie.blockChance = 0.0f
        hitWith(myKnight)
        enemyZombie.blockChance = originalBlockChance

        knightSkill3Id.isEnabled = false
        printKnightParameter(myKnight)
        printZombieParameter(enemyZombie)
        setupKnightHealthBar()
        setupZombieHealthBar()
    }

    fun skillBloodSiphon() {
        clearZombieHitResults()
        val actualHealthOfEnemy = enemyZombie.currentHealth
        hitWith(myKnight)
        val modifiedHealth =
            myKnight.currentHealth + (actualHealthOfEnemy - enemyZombie.currentHealth)
        myKnight.currentHealth = when (modifiedHealth >= myKnight.maxHealth) {
            true -> {
                myKnight.maxHealth
            }
            else -> {
                modifiedHealth
            }
        }
        knightSkill4Id.isEnabled = false
        printKnightParameter(myKnight)
        printZombieParameter(enemyZombie)
        setupKnightHealthBar()
        setupZombieHealthBar()
    }


    fun hitWith(character: Character, extraDamage: Int = 0) {
        when (character) {
            is Knight -> {
                clearKnightHitResults()
                if (blockChance(enemyZombie) == true) {
                    zombieDefenseResultId.text = getString(R.string.successfull_block)
                    knightHitResultId.text = getString(R.string.notsuccessfull_hit)
                } else {
                    if (criticalHit(myKnight) == true) {
                        //enemyZombie.health = enemyZombie.health - myKnight.damage * 2
                        enemyZombie.currentHealth -= myKnight.damage * 2 + extraDamage
                        knightHitResultId.text =
                            getString(R.string.successfull_hit)
                        knightCriticalHitResultId.text =
                            getString(R.string.critical_hit)
                    } else {
                        enemyZombie.currentHealth -= myKnight.damage + extraDamage
                        knightHitResultId.text = getString(R.string.successfull_hit)
                    }
                }
            }
            is Zombie -> {
                clearZombieHitResults()
                if (blockChance(myKnight) == true) {
                    knightDefenseResultId.text = getString(R.string.successfull_block)
                    zombieHitResultId.text = getString(R.string.notsuccessfull_hit)
                } else {
                    if (criticalHit(enemyZombie) == true) {
                        myKnight.currentHealth -= enemyZombie.damage * 2
                        zombieHitResultId.text =
                            getString(R.string.successfull_hit)
                        zombieCriticalHitResultId.text =
                            getString(R.string.critical_hit)
                    } else {
                        myKnight.currentHealth -= enemyZombie.damage
                        zombieHitResultId.text = getString(R.string.successfull_hit)
                    }
                }

            }
        }
        printZombieParameter(enemyZombie)
        printKnightParameter(myKnight)
        setupKnightHealthBar()
        setupZombieHealthBar()
        checkHealthStatus(enemyZombie)
        checkHealthStatus(myKnight)
    }
}