package course.labs.locationlab.tests

import android.test.ActivityInstrumentationTestCase2
import com.robotium.solo.Solo
import course.labs.locationlab.PlaceViewActivity
import course.labs.locationlab.R

class TestSameLocation : ActivityInstrumentationTestCase2<PlaceViewActivity>(PlaceViewActivity::class.java) {
    private var solo: Solo? = null
    @Throws(Exception::class)
    public override fun setUp() {
        solo = Solo(instrumentation, activity)
        PlaceViewActivity.sHasNetwork = false
    }

    @Throws(Exception::class)
    public override fun tearDown() {
        solo!!.finishOpenedActivities()
    }

    fun testRun() {
        val delay = 2000
        val longDelay = 5000

        // Wait for activity: 'course.labs.locationlab.PlaceViewActivity'
        solo!!.waitForActivity(PlaceViewActivity::class.java, delay)

        // Click on action bar item
        solo!!.clickOnActionBarItem(R.id.place_one)
        solo!!.sleep(delay)

        // Click on Get New Place
        solo!!.clickOnView(solo!!.getView(R.id.footer))
        solo!!.sleep(2000)

        // Assert that PlaceBadge is shown
        assertTrue("PlaceBadge is not shown!", solo!!.waitForText(
                solo!!.getString(R.string.the_greenhouse_string), 1, longDelay.toLong()))

        // Click on action bar item
        solo!!.clickOnActionBarItem(R.id.place_one)
        solo!!.sleep(delay)

        // Click on Get New Place
        solo!!.clickOnView(solo!!.getView(R.id.footer))
        val samePlaceString = solo!!
                .getString(R.string.duplicate_location_string)

        // Assert that duplicate location Toast is shown
        assertTrue("$samePlaceString is not shown!",
                solo!!.waitForText(samePlaceString, 1, longDelay.toLong()))
    }
}