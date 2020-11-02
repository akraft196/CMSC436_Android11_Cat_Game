package course.labs.alarmslab

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText

class AlarmCreateActivity : Activity() {
    private var mAlarmManager: AlarmManager? = null
    private var mTweetTextView: EditText? = null
    private var mDelayTextView: EditText? = null
    private var mID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState?: Bundle())
        // TODO create reference to the AlarmManager using ALARM_SERVICE as well as locate necessary text views
        setContentView(R.layout.main)

        mAlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mTweetTextView = findViewById(R.id.text)
        mDelayTextView = findViewById(R.id.time)
    }

    fun set(v: View) {

        val tweetText = mTweetTextView!!.text.toString()
        val delay = Integer.parseInt(mDelayTextView!!.text.toString()) * 1000L

        // TODO Create Intents for the alarm service. Also add the tweet as an extra to the Intent
        // Additionally you will need to create a pending intent that will use the original intent
        // you create to start the AlarmTweetService. Use the PendingIntent.getService() method. Be
        // aware you will need to pass a unique value for the request code (why?) as well as the
        // flag PendingIntent.FLAG_ONE_SHOT.
        val mTweetIntent = Intent(this, AlarmTweetService::class.java).putExtra(TWEET_STRING, tweetText)

        val mTweetPendingIntent = PendingIntent.getService(this, mID, mTweetIntent, 0)
        mID+=1


        // TODO make log statement that tweet was sent to AlarmTweetService
        Log.i(TAG, "Tweet sent to AlarmTweetService")

        // TODO use AlarmManager set method to set Alarm
        mAlarmManager?.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+delay, mTweetPendingIntent)

    }

    fun clear(v: View) {
        // TODO clear views
        mTweetTextView!!.text.clear()
        mDelayTextView!!.setText("5")

    }

    companion object {


        val TWEET_STRING = "TWEET"

        private val TAG = "AlarmCreateActivity"
    }
}
