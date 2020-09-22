package course.labs.fragmentslab

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View

class MainActivity : FragmentActivity(), FriendsFragment.SelectionListener {

    private lateinit var mFriendsFragment: FriendsFragment
    private var mFeedFragment: FeedFragment? = null

    // If there is no fragment_container ID, then the application is in
    // two-pane mode

    private val isInTwoPaneMode: Boolean
        get() = findViewById<View>(R.id.fragment_container) == null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // If the layout is single-pane, create the FriendsFragment
        // and add it to the Activity

        if (!isInTwoPaneMode) {

            mFriendsFragment = FriendsFragment()

            //TODO 1 - add the FriendsFragment


            // Otherwise, save a reference to the FeedFragment for later use


        }

    }

    // Display selected Twitter feed

    override fun onItemSelected(position: Int) {

        Log.i(TAG, "Entered onItemSelected($position)")

        // If in single-pane mode, replace single visible Fragment

        if (!isInTwoPaneMode) {

            // If there is no FeedFragment instance, then create one

            if (mFeedFragment == null)
                mFeedFragment = FeedFragment()

            //TODO 2 - replace the fragment_container with the FeedFragment

            

        }

        // Update Twitter feed display on FriendFragment
        mFeedFragment?.updateFeedDisplay(position)

    }

    companion object {
        private const val TAG = "Lab-Fragments"
    }

}
