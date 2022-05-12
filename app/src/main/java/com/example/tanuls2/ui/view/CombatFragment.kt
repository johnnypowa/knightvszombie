package com.example.tanuls2.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.tanuls2.R
import com.example.tanuls2.handler.SharedPreferencesHandler
import com.example.tanuls2.model.Character
import com.example.tanuls2.model.Knight
import com.example.tanuls2.model.Zombie
import com.example.tanuls2.ui.viewmodel.CombatViewModel
import kotlinx.android.synthetic.main.fragment_combat.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CombatFragment : Fragment() {

    lateinit var myKnight: Knight
    lateinit var enemyZombie: Zombie
    var defeatAlertdialog: AlertDialog? = null
    var victoryAlertdialog: AlertDialog? = null
    //val strongZombie = Zombie(health = 2000)

    val combatViewModel: CombatViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_combat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myKnight = combatViewModel.loadMyKnightData()
        enemyZombie = combatViewModel.loadEnemyZombieData()

        setupHealthBar()
        createDefeatPopup()
        createVictoryPopup()
        printKnightParameter(myKnight)
        printZombieParameter(enemyZombie)

        knightHitId.setOnClickListener {
            if (myKnight.currentHealth > 0 || enemyZombie.currentHealth > 0) {
                hitWith(myKnight)
                hitWith(enemyZombie)
                setupHealthBar()
            }
        }

        knightSkill1Id.setOnClickListener {
            clearZombieHitResults()
            hitWith(myKnight, extraDamage = 100)
            printKnightParameter(myKnight)
            printZombieParameter(enemyZombie)
            knightSkill1Id.isEnabled = false
            setupHealthBar()
        }

        knightSkill2Id.setOnClickListener {
            clearZombieHitResults()
            skillExtraCriticalHitChance()
            setupHealthBar()
        }

        knightSkill3Id.setOnClickListener {
            clearZombieHitResults()
            skillEliminateBlockChance()
            setupHealthBar()
        }

        knightSkill4Id.setOnClickListener {
            clearZombieHitResults()
            skillBloodSiphon()
            setupHealthBar()
        }
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
        victoryAlertdialog= AlertDialog.Builder(requireContext())
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

    fun skillsEnable() {
        knightSkill1Id.isEnabled = true
        knightSkill2Id.isEnabled = true
        knightSkill3Id.isEnabled = true
        knightSkill4Id.isEnabled = true
    }

    fun setUpVictoryStart() {
        enemyZombie.currentHealth = enemyZombie.maxHealth
        myKnight.currentHealth = myKnight.maxHealth
        myKnight.experience += 50
        SharedPreferencesHandler.storedKnight = myKnight
        clearZombieHitResults()
        clearKnightHitResults()
        knightHitResultId.text = getString(R.string.start_fight)
        printKnightParameter(myKnight)
        printZombieParameter(enemyZombie)
        skillsEnable()
        setupHealthBar()
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
        setupHealthBar()
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

    fun setupHealthBar() {
        knightHealthBar.apply {
            max = myKnight.maxHealth
            progress = myKnight.currentHealth
        }
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

    fun skillExtraCriticalHitChance() {
        val originalCriticalHitChance = myKnight.criticalHitChance
        myKnight.criticalHitChance += (1.0f - originalCriticalHitChance)
        hitWith(myKnight)
        myKnight.criticalHitChance = originalCriticalHitChance
        printKnightParameter(myKnight)
        printZombieParameter(enemyZombie)
        knightSkill2Id.isEnabled = false
    }

    fun skillEliminateBlockChance() {
        val originalBlockChance = enemyZombie.blockChance
        enemyZombie.blockChance = 0.0f
        hitWith(myKnight)
        enemyZombie.blockChance = originalBlockChance
        printKnightParameter(myKnight)
        printZombieParameter(enemyZombie)
        knightSkill3Id.isEnabled = false
    }

    fun skillBloodSiphon() {
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
        printKnightParameter(myKnight)
        printZombieParameter(enemyZombie)
        knightSkill4Id.isEnabled = false
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
        checkHealthStatus(enemyZombie)
        checkHealthStatus(myKnight)
    }
}