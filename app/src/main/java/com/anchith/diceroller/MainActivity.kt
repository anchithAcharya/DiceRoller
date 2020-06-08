package com.anchith.diceroller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.google.android.material.floatingactionbutton.FloatingActionButton

var COUNT: Int = 0
var INDEX: Int = -1
var MAX_DIE_COUNT = 7
val maxDiceValues: MutableList<Int> = MutableList(MAX_DIE_COUNT) { 6 }
lateinit var diceBox: LinearLayout
lateinit var numberPicker: NumberPicker

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.activity_main)
		diceBox = findViewById(R.id.dice_box)

		numberPicker = findViewById(R.id.number_picker)
		numberPicker.setOnValueChangedListener { _, _, newVal -> maxDiceValues[INDEX] = newVal }

		numberPicker.maxValue = 20
		numberPicker.minValue = 2
		numberPicker.value = 6

		val fab: FloatingActionButton = findViewById(R.id.field_add)
		fab.performClick()

		val rollButton: Button = findViewById(R.id.roll_button)
		rollButton.setOnClickListener { rollDice() }

		val resetButton: Button = findViewById(R.id.reset_button)
		resetButton.setOnClickListener { resetView() }
	}

	private fun rollDice() {
		for (child in diceBox.children) {
			val resultText = child as TextView
			val index = diceBox.indexOfChild(child)
			val random: Int = (1..maxDiceValues[index]).random()

			if (child.isSelected) {
				resultText.text = random.toString()
				resultText.performClick()
			} else resultText.text = random.toString()
		}

	}

	private fun resetView() {
		while (diceBox.childCount > 1) {
			diceBox.removeViewAt(0)
		}

		val view = diceBox.getChildAt(0) as TextView
		view.text = getString(R.string.text_field)

		COUNT = 1
		INDEX = 0
		numberPicker.value = 6

		view.performClick()
	}

	fun addDie(view: View) {
		if (COUNT < MAX_DIE_COUNT) {
			val diceBox: LinearLayout = findViewById(R.id.dice_box)

			val inflater: LayoutInflater =
				this.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
			inflater.inflate(R.layout.field, diceBox)

			val newDie = diceBox.getChildAt(diceBox.childCount - 1) as TextView

			newDie.setOnClickListener { selectDie(newDie) }
			newDie.performClick()

			COUNT++
			numberPicker.value = 6
		} else {
			Toast.makeText(this, "Cannot add more dice!", Toast.LENGTH_SHORT).show()
		}
	}

	fun removeDie(view: View) {
		if (COUNT > 1) {
			diceBox.removeViewAt(INDEX)
			maxDiceValues.removeAt(INDEX)
			maxDiceValues.add(6)
			COUNT--
			if (INDEX != 0) INDEX--
			diceBox.getChildAt(INDEX).performClick()
		} else {
			Toast.makeText(this, "Can't remove the last one!", Toast.LENGTH_SHORT).show()
		}
	}

	private fun selectDie(die: TextView) {
		val numberPicker: NumberPicker = findViewById(R.id.number_picker)
		val pickPrompt: TextView = findViewById(R.id.pick_prompt)

		val old = diceBox.getChildAt(INDEX) as? TextView
		old?.isSelected = false

		INDEX = diceBox.indexOfChild(die)

		pickPrompt.text = "${getText(R.string.pick_prompt)} ${(INDEX + 1).toString()}"
		numberPicker.value = maxDiceValues[INDEX]
		die.isSelected = true
	}
}