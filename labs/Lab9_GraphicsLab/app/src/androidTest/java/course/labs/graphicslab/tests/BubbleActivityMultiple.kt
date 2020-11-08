package course.labs.graphicslab.tests

import course.labs.graphicslab.BubbleActivity

import com.robotium.solo.*
import android.test.ActivityInstrumentationTestCase2
import junit.framework.Assert
import kotlin.jvm.Throws

class BubbleActivityMultiple : ActivityInstrumentationTestCase2<BubbleActivity>(BubbleActivity::class.java) {
    private var solo: Solo? = null

    @Throws(Exception::class)
    public override fun setUp() {
        solo = Solo(instrumentation, activity)
    }

    @Throws(Exception::class)
    public override fun tearDown() {
        solo!!.finishOpenedActivities()
    }

    fun testRun() {

        val delay = 2000

        // Wait for activity: 'course.labs.TouchLab.BubbleActivity'
        solo!!.waitForActivity(course.labs.graphicslab.BubbleActivity::class.java, delay)

        // Set Still Mode
        solo!!.clickOnMenuItem("Still Mode")

        // Click on action bar item
        solo!!.clickOnMenuItem("Add a Bubble")

        solo!!.sleep(delay)

        // Assert that a bubble was displayed
        Assert.assertEquals("Bubble hasn't appeared", 1, solo!!.getCurrentViews<course.labs.graphicslab.BubbleActivity.BubbleView>(course.labs.graphicslab.BubbleActivity.BubbleView::class.java).size)

        // Click on action bar item
        solo!!.clickOnMenuItem("Add a Bubble")

        solo!!.sleep(delay)

        // Assert that a bubble was displayed
        Assert.assertEquals("Second bubble hasn't appeared", 2, solo!!.getCurrentViews<course.labs.graphicslab.BubbleActivity.BubbleView>(course.labs.graphicslab.BubbleActivity.BubbleView::class.java).size)

        solo!!.sleep(delay)

        // Give misbehaving bubbles a chance to move off screen
        // Assert that there are two bubbles on the screen
        Assert.assertEquals(
                "There should be two bubbles on the screen",
                2,
                solo!!.getCurrentViews<course.labs.graphicslab.BubbleActivity.BubbleView>(
                        course.labs.graphicslab.BubbleActivity.BubbleView::class.java)
                        .size)

    }
}
