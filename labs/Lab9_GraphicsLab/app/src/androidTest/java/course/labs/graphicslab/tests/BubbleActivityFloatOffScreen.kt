package course.labs.graphicslab.tests

import course.labs.graphicslab.BubbleActivity

import com.robotium.solo.*
import android.test.ActivityInstrumentationTestCase2
import junit.framework.Assert
import kotlin.jvm.Throws

class BubbleActivityFloatOffScreen : ActivityInstrumentationTestCase2<BubbleActivity>(BubbleActivity::class.java) {
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

        val shortDelay = 250
        val delay = 2000

        // Wait for activity: 'course.labs.TouchLab.BubbleActivity'
        solo!!.waitForActivity(course.labs.graphicslab.BubbleActivity::class.java,
                delay)

        // Click on action bar item
        solo!!.clickOnMenuItem("Single Speed Mode")

        // Click on action bar item
        solo!!.clickOnMenuItem("Add a Bubble")

        // Check whether bubble appears
        var bubbleAppeared = solo!!.getCurrentViews<course.labs.graphicslab.BubbleActivity.BubbleView>(
                course.labs.graphicslab.BubbleActivity.BubbleView::class.java).size > 0
        var i = 0
        while (i < 8 && !bubbleAppeared) {
            solo!!.sleep(shortDelay)
            bubbleAppeared = solo!!.getCurrentViews<course.labs.graphicslab.BubbleActivity.BubbleView>(
                    course.labs.graphicslab.BubbleActivity.BubbleView::class.java)
                    .size > 0
            i++
        }

        // Assert that a bubble was displayed
        Assert.assertTrue("Bubble hasn't appeared", bubbleAppeared)

        solo!!.sleep(delay)

        // Assert that the bubble has left the screen
        Assert.assertEquals(
                "Bubble hasn't left the screen",
                0,
                solo!!.getCurrentViews<course.labs.graphicslab.BubbleActivity.BubbleView>(
                        course.labs.graphicslab.BubbleActivity.BubbleView::class.java)
                        .size)

    }
}
