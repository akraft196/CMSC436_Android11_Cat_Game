package course.labs.locationlab.tests

import android.test.ActivityInstrumentationTestCase2
import com.robotium.solo.Solo
import course.labs.locationlab.PlaceViewActivity
import course.labs.locationlab.R

class TestOneValidLocation : ActivityInstrumentationTestCase2<PlaceViewActivity>(PlaceViewActivity::class.java) {
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
        val delay = 5000
        val longDelay = 8000

        // Wait for activity: 'course.labs.locationlab.PlaceViewActivity'
        solo!!.waitForActivity(PlaceViewActivity::class.java,
                2000)

        // Click on action bar item
        solo!!.clickOnActionBarItem(R.id.place_one)
        solo!!.sleep(delay)

        // Click on Get New Place
        solo!!.clickOnView(solo!!.getView(R.id.footer))

        // Assert that PlaceBadge is shown
        assertTrue("PlaceBadge is not shown!", solo!!.waitForText(
                solo!!.getString(R.string.the_greenhouse_string), 1, longDelay.toLong()))
        // Click on PlaceBadge

        solo!!.clickOnText(solo!!.getString(R.string.the_greenhouse_string))
        solo!!.sleep(10000)
        assertTrue("Detail view not shown!", solo!!.waitForText("Date"))
    }
}