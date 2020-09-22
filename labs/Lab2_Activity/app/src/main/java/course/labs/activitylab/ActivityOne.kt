/* See https://kotlinlang.org/docs/reference/ for reference material on
 the Kotlin language */

package course.labs.activitylab

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView

class ActivityOne : Activity() {


    // Lifecycle counters

    // TODO:
    // Create counter variables for onCreate(), onRestart(), onStart() and
    // onResume()
    // You will need to increment these variables' values when their
    // corresponding lifecycle methods get called

    var createCount = 0
    var restartCount = 0
    var startCount = 0
    var resumeCount = 0


    // TODO: Create variables for each of the TextViews
    lateinit var textViewCreate:TextView
    lateinit var textViewStart:TextView
    lateinit var textViewRestart:TextView
    lateinit var textViewResume:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one)

        // TODO: Assign the appropriate TextViews to the TextView variables
        // Hint: Access the TextView by calling Activity's findViewById()
        textViewCreate = findViewById(R.id.create)
        textViewStart = findViewById(R.id.start)
        textViewRestart = findViewById(R.id.restart)
        textViewResume = findViewById(R.id.resume)


        val launchActivityTwoButton = findViewById<Button>(R.id.bLaunchActivityTwo)
        launchActivityTwoButton.setOnClickListener {
            // TODO:
            // Launch Activity Two
            // Hint: use Context's startActivity() method

            // Create an intent stating which Activity you would like to start
            val intent = Intent(this, ActivityTwo::class.java)
            // Launch the Activity using the intent
            startActivity(intent)
        }

        // Has previous state been saved?
        if (savedInstanceState != null) {

            // TODO:
            // Restore value of counters from saved state
            // Only need 4 lines of code, one for every count variable
//            savedInstanceState.let { createCount = it.getInt(CREATE_KEY) }
//            savedInstanceState.let { restartCount = it.getInt(RESTART_KEY) }
//            savedInstanceState.let { startCount = it.getInt(START_KEY) }
//            savedInstanceState.let { resumeCount = it.getInt(RESUME_KEY) }

            with(savedInstanceState){
                createCount = getInt(CREATE_KEY)
                restartCount = getInt(RESTART_KEY)
                startCount = getInt(START_KEY)
                resumeCount = getInt(RESUME_KEY)
            }

        }

        // Emit LogCat message
        Log.i(TAG, "Entered the onCreate() method")

        // TODO:
        // Update the appropriate count variable
        // Update the user interface via the displayCounts() method
        createCount += 1
        displayCounts()

    }

    // Lifecycle callback overrides

    public override fun onStart() {
        super.onStart()

        // Emit LogCat message
        Log.i(TAG, "Entered the onStart() method")

        // TODO:
        // Update the appropriate count variable
        startCount += 1
        // Update the user interface
        displayCounts()
    }

    public override fun onResume() {
        super.onResume()

        // Emit LogCat message
        Log.i(TAG, "Entered the onResume() method")
        // Follow the previous 2 examples provided




        // TODO:
        // Update the appropriate count variable
        resumeCount += 1
        // Update the user interface
        displayCounts()
    }

    public override fun onPause() {
        super.onPause()

        // Emit LogCat message
        Log.i(TAG, "Entered the onPause() method")
        // Follow the previous 2 examples provided

    }

    public override fun onStop() {
        super.onStop()

        // Emit LogCat message
        Log.i(TAG, "Entered the onStop() method")
        // Follow the previous 2 examples provided


    }

    public override fun onRestart() {
        super.onRestart()

        // Emit LogCat message
        Log.i(TAG, "Entered the onRestart() method")
        // Follow the previous 2 examples provided


        // TODO:
        // Update the appropriate count variable
        restartCount += 1
        // Update the user interface
        displayCounts()

    }

    public override fun onDestroy() {
        super.onDestroy()

        // Emit LogCat message
        Log.i(TAG, "Entered the onDestroy() method")
        // Follow the previous 2 examples provided


    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        // TODO:
        // Save state information with a collection of key-value pairs
        savedInstanceState.apply {
            putInt(CREATE_KEY, createCount)
            putInt(RESTART_KEY, restartCount)
            putInt(START_KEY, startCount)
            putInt(RESUME_KEY, resumeCount)
        }

        super.onSaveInstanceState(savedInstanceState)

    }

    // Updates the displayed counters
    private fun displayCounts() {
        // TODO:
        // set text for text view variables
        textViewCreate.setText("onCreate() calls: $createCount")
        textViewRestart.setText("onRestart() calls: $restartCount")
        textViewResume.setText("onResume() calls: $resumeCount")
        textViewStart.setText("onStart() calls: $startCount")
    }

    companion object {

        private val RESTART_KEY = "restart"
        private val RESUME_KEY = "resume"
        private val START_KEY = "start"
        private val CREATE_KEY = "create"

        // String for LogCat documentation
        private val TAG = "Lab-ActivityOne"
    }
}
