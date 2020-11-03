package course.labs.graphicslab.tests

import course.labs.graphicslab.BubbleActivity

import com.robotium.solo.*
import android.test.ActivityInstrumentationTestCase2
import junit.framework.Assert
import kotlin.jvm.Throws

class BubbleActivityPop : ActivityInstrumentationTestCase2<BubbleActivity>(BubbleActivity::class.java) {
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
        solo!!.waitForActivity(course.labs.graphicslab.BubbleActivity::class.java,
                delay)

        // Set Still Mode
        solo!!.clickOnMenuItem("Still Mode")

        // Click on action bar item
        solo!!.clickOnMenuItem("Add a Bubble")

        solo!!.sleep(delay)

        // Assert that a bubble was displayed
        Assert.assertEquals(
                "Bubble hasn't appeared",
                1,
                solo!!.getCurrentViews<course.labs.graphicslab.BubbleActivity.BubbleView>(
                        course.labs.graphicslab.BubbleActivity.BubbleView::class.java)
                        .size)

        // Click on action bar item
        solo!!.clickOnMenuItem("Delete a Bubble")

        solo!!.sleep(delay)

        // Assert that there are no more bubbles
        Assert.assertEquals(
                "The bubble was not popped",
                0,
                solo!!.getCurrentViews<course.labs.graphicslab.BubbleActivity.BubbleView>(
                        course.labs.graphicslab.BubbleActivity.BubbleView::class.java)
                        .size)

    }
}
