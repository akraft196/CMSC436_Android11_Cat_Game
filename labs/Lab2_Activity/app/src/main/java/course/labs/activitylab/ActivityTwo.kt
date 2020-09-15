/* See https://kotlinlang.org/docs/reference/ for reference material on
 the Kotlin language */

package course.labs.activitylab

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView

class ActivityTwo : Activity() {

    // Lifecycle counters

    // TODO:
    // Create counter variables for onCreate(), onRestart(), onStart() and
    // onResume()
    // You will need to increment these variables' values when their
    // corresponding lifecycle methods get called



    // TODO: Create variables for each of the TextViews

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)

        // TODO: Assign the appropriate TextViews to the TextView variables
        // Hint: Access the TextView by calling Activity's findViewById()
        // textView1 = (TextView) findViewById(R.id.textView1);



        val closeButton = findViewById<Button>(R.id.bClose)
        closeButton.setOnClickListener {
            // TODO:
            // This function closes Activity Two
            // Hint: use Context's finish() method


        }

        // Has previous state been saved?
        if (savedInstanceState != null) {

            // TODO:
            // Restore value of counters from saved state
            // Only need 4 lines of code, one for every count variable

        }

        // Emit LogCat message

        Log.i(TAG, "Entered the onCreate() method")

        // TODO:
        // Update the appropriate count variable
        // Update the user interface via the displayCounts() method

    }

    // Lifecycle callback methods overrides

    public override fun onStart() {
        super.onStart()

        // Emit LogCat message
        Log.i(TAG, "Entered the onStart() method")

        // TODO:
        // Update the appropriate count variable
        // Update the user interface

    }

    public override fun onResume() {
        super.onResume()

        // Emit LogCat message
        // Follow the previous 2 examples provided


        // TODO:
        // Update the appropriate count variable
        // Update the user interface

    }

    public override fun onPause() {
        super.onPause()

        // Emit LogCat message
        // Follow the previous 2 examples provided


    }

    public override fun onStop() {
        super.onStop()

        // Emit LogCat message
        // Follow the previous 2 examples provided

    }

    public override fun onRestart() {
        super.onRestart()

        // Emit LogCat message
        // Follow the previous 2 examples provided


        // TODO:
        // Update the appropriate count variable
        // Update the user interface

    }

    public override fun onDestroy() {
        super.onDestroy()

        // Emit LogCat message
        // Follow the previous 2 examples provided


    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {

        // TODO:
        // Save counter state information with a collection of key-value pairs
        // 4 lines of code, one for every count variable


    }

    // Updates the displayed counters
    private fun displayCounts() {
        

    }

    companion object {

        private val RESTART_KEY = "restart"
        private val RESUME_KEY = "resume"
        private val START_KEY = "start"
        private val CREATE_KEY = "create"

        // String for LogCat documentation
        private val TAG = "Lab-ActivityTwo"
    }
}
