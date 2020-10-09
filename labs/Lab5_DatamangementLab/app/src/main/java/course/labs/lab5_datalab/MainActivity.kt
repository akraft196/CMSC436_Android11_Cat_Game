package course.labs.lab5_datalab

import androidx.appcompat.app.AppCompatActivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
//import javax.swing.text.View

class MainActivity : AppCompatActivity() {

    protected lateinit var sharedpreferences: SharedPreferences
    protected lateinit var name: TextView
    protected lateinit var uidtv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun Save(view: View) {

    }

    fun clear(view: View) {

    }

    fun Get(view: View) {

    }

    fun goToAnActivity(view: View) {
        val intent = Intent(this, External::class.java)
        startActivity(intent)
    }

    companion object {
        val mypreference = "mypref"
        val Name = "nameKey"
        val uid = "uidkey"
    }
}
