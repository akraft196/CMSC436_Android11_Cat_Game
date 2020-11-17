package course.labs.locationlab.tests

import android.test.ActivityInstrumentationTestCase2
import com.robotium.solo.Solo
import course.labs.locationlab.PlaceViewActivity
import course.labs.locationlab.R

class TestNoCountryLocation : ActivityInstrumentationTestCase2<PlaceViewActivity>(PlaceViewActivity::class.java) {
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
        solo!!.waitForActivity(PlaceViewActivity::class.java,
                delay)

        // Click on action bar item
        solo!!.clickOnActionBarItem(R.id.place_no_country)
        solo!!.sleep(delay)

        // Click on Get New Place
        solo!!.clickOnView(solo!!.getView(R.id.footer))
        val noCountryString = solo!!.getString(R.string.no_country_string)

        // Assert that no country toast is shown
        assertTrue("$noCountryString is not shown!",
                solo!!.waitForText(noCountryString, 1, longDelay.toLong()))
    }
}