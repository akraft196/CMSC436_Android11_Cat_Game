package course.labs.locationlab.tests

import android.test.ActivityInstrumentationTestCase2
import com.robotium.solo.Solo
import course.labs.locationlab.PlaceViewActivity
import course.labs.locationlab.R
import org.junit.Test

class TestTwoValidLocations : ActivityInstrumentationTestCase2<PlaceViewActivity>(PlaceViewActivity::class.java) {
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

    @Test
    fun testRun() {
        val delay = 2000
        val longDelay = 5000

        // Wait for activity: 'course.labs.locationlab.PlaceViewActivity'
        solo!!.waitForActivity(PlaceViewActivity::class.java,
                2000)

        // Click on action bar item
        solo!!.clickOnActionBarItem(R.id.place_one)
        solo!!.sleep(delay)

        // Click on Get New Place
        solo!!.clickOnView(solo!!.getView(R.id.footer))
        solo!!.sleep(delay)

        // Assert that PlaceBadge is shown
        assertTrue("PlaceBadge is not shown!", solo!!.waitForText(
                solo!!.getString(R.string.the_greenhouse_string), 1, longDelay.toLong()))

        // Click on action bar item
        solo!!.clickOnActionBarItem(R.id.place_two)
        solo!!.sleep(delay)

        // Click on Get New Place
        solo!!.clickOnView(solo!!.getView(R.id.footer))
        solo!!.sleep(delay)

        // Assert that PlaceBadge is shown
        assertTrue("PlaceBadge is not shown!", solo!!.waitForText(
                solo!!.getString(R.string.berwyn_string), 1, longDelay.toLong()))

        // Click on PlaceBadge
        solo!!.clickOnText(solo!!.getString(R.string.berwyn_string))
        solo!!.sleep(delay)
        assertTrue("Detail view not shown!", solo!!.waitForText("Date"))
    }
}