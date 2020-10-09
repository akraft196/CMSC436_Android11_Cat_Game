package course.labs.lab5_lifecycle_aware

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.view.View
import android.widget.TextView
import android.content.Context
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders

class LifecycleMainActivity : AppCompatActivity() {
    private lateinit var mAdapter: HistoryListAdapter
    private fun getScreenOrientation(context: Context): String {
        val orientationList = listOf("Portrait","Landscape","Reverse Portrait","Reverse Landscape")
        val orientation = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
        return orientationList[orientation]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    //ToDo: Implement your own logic to display appropriate text ,increase and reset the counter and populate the history list
        // Add the Activity observer to preserve the counts till now and add  it to the list when the activity is created again
    }
}
