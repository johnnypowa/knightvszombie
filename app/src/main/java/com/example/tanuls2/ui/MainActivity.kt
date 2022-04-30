package com.example.tanuls2.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tanuls2.R
import com.example.tanuls2.model.Character
import com.example.tanuls2.model.Knight
import com.example.tanuls2.model.Zombie
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val myKnight = Knight()
    val enemyZombie = Zombie()
    //val strongZombie = Zombie(health = 2000)

    fun printKnightParameter(knight: Knight) {
        knightLevelValue.text = knight.level.toString()
        knightHealthValue.text = knight.health.toString()
        knightExperienceValue.text = knight.experience.toString()
        knightDamageValue.text = knight.damage.toString()
        knightCriticalHitChanceValue.text = (knight.criticalHitChance*100).toString()+getString(R.string.szazalek_jelölés)
        knightBlockChanceValue.text = (knight.blockChance*100).toString()+getString(R.string.szazalek_jelölés)
    }


    fun printZombieParameter(zombie: Zombie) {
        enemyLevelValue.text = zombie.level.toString()
        enemyHealthValue.text = zombie.health.toString()
        enemyExperienceValue.text = zombie.experience.toString()
        enemyDamageValue.text = zombie.damage.toString()
        enemyCriticalHitChanceValue.text = (zombie.criticalHitChance*100).toString()+getString(R.string.szazalek_jelölés)
        enemyBlockChanceValue.text = (zombie.blockChance*100).toString()+getString(R.string.szazalek_jelölés)
    }

    fun showVictoryPopup() {
        AlertDialog.Builder(this)
            .setMessage("Megölted a ellenfeled, gratulálok! Szépp munka!")
            .setTitle("Győzelem!")
            .setPositiveButton("Király! Kérem a következőt!") { dialog, p1 ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()

        //Toast.makeText(this,"ez egy szöveg", Toast.LENGTH_LONG).show()
    }

    fun showDefeatPopup() {
        AlertDialog.Builder(this)
            .setMessage("Gratulálok meghaltál!")
            .setTitle("Lúzer!")
            .setPositiveButton("Persze, megpróbálom újra.") { dialog, p1 ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()

        //Toast.makeText(this,"ez egy szöveg", Toast.LENGTH_LONG).show()
    }

    fun skillsEnable() {
        knightSkill1Id.isEnabled = true
        knightSkill2Id.isEnabled = true
        knightSkill3Id.isEnabled = true
        knightSkill4Id.isEnabled = true
    }

    //fun printParameters() {
    //    knightParameterId.text =
    //       "Lovag\nSzint: " + myKnight.level.toString() + "\nÉlet: " + myKnight.health.toString() + "\nTapasztalat: " + myKnight.experience.toString() + "\nSebzés: " + myKnight.damage.toString() + "\nKritikus ütés: " + myKnight.criticalHitChance.toString() + "\nBlokkolás: " + myKnight.blockChance.toString()
    //   zombieParameterId.text =
    //       "Zombi\nSzint: " + enemyZombie.level.toString() + "\nÉlet: " + enemyZombie.health.toString() + "\nTapasztalat: " + enemyZombie.experience.toString() + "\nSebzés: " + enemyZombie.damage.toString() + "\nKritikus ütés: " + enemyZombie.criticalHitChance.toString() + "\nBlokkolás: " + enemyZombie.blockChance.toString()
    //}

    fun checkHealthStatus(character: Character) {
        when (character) {
            is Knight -> {

                if (myKnight.health < 1) {
                    enemyZombie.health = 1000
                    myKnight.health = 100
                    myKnight.experience -= 50
                    clearZombieHitResults()
                    clearKnightHitResults()
                    knightHitResultId.text = getString(R.string.start_fight)
                    printKnightParameter(myKnight)
                    printZombieParameter(enemyZombie)
                    showDefeatPopup()
                    skillsEnable()
                }

            }
            is Zombie -> {

                if (enemyZombie.health < 1) {
                    enemyZombie.health = 1000
                    myKnight.health = 100
                    myKnight.experience += 50
                    clearZombieHitResults()
                    clearKnightHitResults()
                    knightHitResultId.text = getString(R.string.start_fight)
                    printKnightParameter(myKnight)
                    printZombieParameter(enemyZombie)
                    showVictoryPopup()
                    skillsEnable()
                }

            }
        }
    }

    fun randomValueGenerator(): Float {
        val randomValue = (0..100).random()
        return randomValue / 100f
    }

    //fun healthBar (){
      //  val knightMaxHealth  = Knight.health

    //}

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

    //fun knightCriticalHit(): Boolean {
    //    return randomValueGenerator() <= myKnight.criticalHitChance
    //}
    //fun zombieCriticalHit(): Boolean {
    //   return randomValueGenerator() <= enemyZombie.criticalHitChance
    //}

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
        val actualHealthOfEnemy = enemyZombie.health
        hitWith(myKnight)
        myKnight.health += (actualHealthOfEnemy - enemyZombie.health)
        printKnightParameter(myKnight)
        printZombieParameter(enemyZombie)
        knightSkill4Id.isEnabled = false
    }

    //fun knightBlockChance(): Boolean {
    //   return randomValueGenerator() <= myKnight.blockChance
    //}
    //fun zombieBlockChance(): Boolean {
    //   return randomValueGenerator() <= enemyZombie.blockChance
    //}

    fun clearKnightHitResults() {
        knightHitResultId.text = ""
        knightDefenseResultId.text = ""
        knightCriticalHitResultId.text = ""
    }

    fun clearZombieHitResults() {
        zombieHitResultId.text = ""
        zombieDefenseResultId.text = ""
        zombieCriticalHitResultId.text = ""
    }

    fun hitWith(character: Character, extraDamage: Int = 0) {
        when (character) {
            is Knight -> {
                clearKnightHitResults()
                if (blockChance(enemyZombie) == true) {
                    zombieDefenseResultId.text = getString(R.string.successfull_block)
                } else {
                    if (criticalHit(myKnight) == true) {
                        //enemyZombie.health = enemyZombie.health - myKnight.damage * 2
                        enemyZombie.health -= myKnight.damage * 2 + extraDamage
                        knightHitResultId.text =
                            getString(R.string.successfull_hit)
                        knightCriticalHitResultId.text =
                            getString(R.string.critical_hit)
                    } else {
                        enemyZombie.health -= myKnight.damage + extraDamage
                        knightHitResultId.text = getString(R.string.successfull_hit)
                    }
                }
            }
            is Zombie -> {
                clearZombieHitResults()
                if (blockChance(myKnight) == true) {
                    knightDefenseResultId.text = getString(R.string.successfull_block)
                } else {
                    if (criticalHit(enemyZombie) == true) {
                        myKnight.health -= enemyZombie.damage * 2
                        zombieHitResultId.text =
                            getString(R.string.successfull_hit)
                        zombieCriticalHitResultId.text =
                            getString(R.string.critical_hit)
                    } else {
                        myKnight.health -= enemyZombie.damage
                        zombieHitResultId.text = getString(R.string.successfull_hit)
                    }
                }

            }
        }
        printZombieParameter(enemyZombie)
        printKnightParameter(myKnight)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        printKnightParameter(myKnight)
        printZombieParameter(enemyZombie)

        knightHitId.setOnClickListener {
            hitWith(myKnight)
            hitWith(enemyZombie)
            checkHealthStatus(enemyZombie)
            checkHealthStatus(myKnight)
        }

        knightSkill1Id.setOnClickListener {
            hitWith(myKnight, extraDamage = 100)
            printKnightParameter(myKnight)
            printZombieParameter(enemyZombie)
            knightSkill1Id.isEnabled = false
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

        //val knightHitChance = randomValueGenerator()
        //val zombieHitChance = randomValueGenerator()

        //if (knightHitChance< myKnight.hitRate) {

        // knightFightResultId.text = "Lovag találati ráta: $knightHitChance\n"+getString(R.string.successfull_hit)+"\n-" + myKnight.damage + " sebzés"
        //enemyZombie.health = enemyZombie.health-myKnight.damage
        //printParameters()
        //} else {

        // if (zombieHitChance < enemyZombie.hitRate) {
        //     knightFightResultId.text = "Lovag találati ráta: $knightHitChance\n" + getString(R.string.notsuccessfull_hit)
        //   zombieFightResultId.text = "Zombi találati ráta: $zombieHitChance\n" + getString(R.string.successfull_hit)+"\n-" + enemyZombie.damage + " sebzés"
        //    myKnight.health = myKnight.health - enemyZombie.damage
        //     printParameters()
        // } else {
        //     knightFightResultId.text = "Találati ráta: $knightHitChance\n" + getString(R.string.notsuccessfull_hit)
        //    zombieFightResultId.text = "Zombi találati ráta: $zombieHitChance\n" + getString(R.string.notsuccessfull_hit)
        //    printParameters()
        // }
        //}

        // if (enemyZombie.health < 1 ){
        //    enemyZombie.health = 100
        //    myKnight.health = 100
        //     myKnight.experience = myKnight.experience+50
        //    knightFightResultId.text = ""
        //    zombieFightResultId.text = getString(R.string.start_fight)

        //    printParameters()

        // }else{

        // }


    }
}









