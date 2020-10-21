package com.example.modernartmidterm

import android.animation.ArgbEvaluator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val box1 = ColorBox(findViewById<View>(R.id.box1), Color.RED, Color.YELLOW/*ColorBox.BoxType.COLOR*/)
        val box2 = ColorBox(findViewById<View>(R.id.box2), Color.BLUE, Color.GREEN/*ColorBox.BoxType.COLOR*/)
        val box3 = ColorBox(findViewById<View>(R.id.box3), Color.WHITE, Color.WHITE/*ColorBox.BoxType.WHITE*/)    //we're gonna have this be the white box
        val box4 = ColorBox(findViewById<View>(R.id.box4), Color.GREEN, Color.CYAN/*ColorBox.BoxType.COLOR*/)
        val box5 = ColorBox(findViewById<View>(R.id.box5), Color.MAGENTA, Color.RED/*ColorBox.BoxType.COLOR*/)
        val boxes = listOf<ColorBox>(box1, box2, box3, box4, box5)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)

        for(box in boxes){
            box.makeColorBox()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            val evaluator = ArgbEvaluator()
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                for(box in boxes){
                    box.setNewCurrColor(evaluator.evaluate(progress.toFloat()/100, box.getBaseColor(), box.getEndColor()) as Int)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //do nothing
            }
        })
    }

    //show the custom menu we created
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    //show the about dialogue when you click the more info option in the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        showDialogue()
        return super.onOptionsItemSelected(item)
    }

    //code for showing the about dialog
    private fun showDialogue(){
        val diag = DialogBox()
        diag.show(supportFragmentManager, "about_diag")
    }

}