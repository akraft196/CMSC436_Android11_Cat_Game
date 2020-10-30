package course.labs.alarmslab

import android.app.Activity
import android.app.AlarmManager
import android.os.Bundle
import android.view.View
import android.widget.EditText

class AlarmCreateActivity : Activity() {
    private val mAlarmManager: AlarmManager? = null
    private val mTweetTextView: EditText? = null
    private val mDelayTextView: EditText? = null
    private val mID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState?: Bundle())
        // TODO create reference to the AlarmManager using ALARM_SERVICE as well as locate necessary text views

    }

    fun set(v: View) {

        val tweetText = mTweetTextView!!.text.toString()
        val delay = Integer.parseInt(mDelayTextView!!.text.toString()) * 1000L

        // TODO Create Intents for the alarm service. Also add the tweet as an extra to the Intent
        // Additionally you will need to create a pending intent that will use the original intent
        // you create to start the AlarmTweetService. Use the PendingIntent.getService() method. Be
        // aware you will need to pass a unique value for the request code (why?) as well as the
        // flag PendingIntent.FLAG_ONE_SHOT.


        // TODO make log statement that tweet was sent to AlarmTweetService


        // TODO use AlarmManager set method to set Alarm

    }

    fun clear(v: View) {
        // TODO clear views

    }

    companion object {


        val TWEET_STRING = "TWEET"

        private val TAG = "AlarmCreateActivity"
    }
}
