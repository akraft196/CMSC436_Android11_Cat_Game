package course.labs.locationlab.tests

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.filters.SdkSuppress
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.By
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.Until
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 21)
class PermGrantedTest {
    private var mDevice: UiDevice? = null
    @Before
    fun startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        mDevice?.pressHome()

        // Wait for launcher
        val launcherPackage = mDevice?.launcherPackageName
        Assert.assertThat(launcherPackage, CoreMatchers.`is`(CoreMatchers.notNullValue()))
        mDevice?.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT.toLong())

        // Launch the app
        val context = InstrumentationRegistry.getContext()
        val intent = context.packageManager
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE)
        if (null == intent) Assert.fail()

        // Clear out any previous instances
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

        // Wait for the app to appear
        mDevice?.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT.toLong())
        //Give permission if it is not given already
        //
        val permissionDialog = mDevice?.wait(Until.findObject(By.text("Allow only while using the app")), 2000)
        permissionDialog?.click()
    }

    @Test
    fun testPermissionRequested() {
        val getNewPlace = mDevice!!.wait(Until.findObject(By.text("Get New Place")), 2000)
        Assert.assertNotNull("Permission wasn't granted", getNewPlace)
        getNewPlace.click()
    }

    companion object {
        private const val LAUNCH_TIMEOUT = 10000
        private const val BASIC_SAMPLE_PACKAGE = "course.labs.locationlab"
    }
}