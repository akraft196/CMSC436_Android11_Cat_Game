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

        sharedpreferences = baseContext.getSharedPreferences(mypreference, Context.MODE_PRIVATE)

        name = findViewById(R.id.etName)
        uidtv = findViewById(R.id.etUid)
    }

    fun Save(view: View) {
        val currName = name.text.toString()
        val currID = uidtv.text.toString()

        val editor = sharedpreferences.edit()
        editor.putString(Name, currName)
        editor.putString(uid, currID)
        editor.apply()

    }

    fun clear(view: View) {
        name.text = ""
        uidtv.text = ""
    }

    fun Get(view: View) {
        val lastName = sharedpreferences.getString(Name, "")
        val lastID = sharedpreferences.getString(uid, "")

        name.text = lastName
        uidtv.text = lastID
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
