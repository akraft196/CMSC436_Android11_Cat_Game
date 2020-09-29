package course.labs.permissionslab

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button

class GoToDangerousActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.go_to_dangerous_activity)

        // TODO - Set startDangerousActivityButton value to the button with id R.id.start_dangerous_activity_button
        val startDangerousActivityButton = findViewById<View>(R.id.start_dangerous_activity_button) as Button
        // TODO - Add onClickListener to the startDangerousActivityButton to call startDangerousActivity()
        startDangerousActivityButton.setOnClickListener {
            if (needsRuntimePermission()) {
                requestPermissions(arrayOf(DANGEROUS_ACTIVITY_PERM), MY_PERMISSIONS_REQUEST_DANGEROUS_ACTIVITY)
            } else {
                startDangerousActivity()
            }

        }

    }

    private fun needsRuntimePermission(): Boolean {
        // Check the SDK version and whether the permission is already granted.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                DANGEROUS_ACTIVITY_PERM
        ) != PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_DANGEROUS_ACTIVITY -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startDangerousActivity()

                } else {

                    Log.i(TAG, "Dangerous App won't open --- Permission was not granted")

                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    private fun startDangerousActivity() {

        Log.i(TAG, "Entered startDangerousActivity()")

        startActivity(Intent(DANGEROUS_ACTIVITY_ACTION))


    }

    companion object {

        private val TAG = "Lab-Permissions"
        val MY_PERMISSIONS_REQUEST_DANGEROUS_ACTIVITY = 2

        private val DANGEROUS_ACTIVITY_ACTION = "course.labs.permissions.DANGEROUS_ACTIVITY"

        private val DANGEROUS_ACTIVITY_PERM = "course.labs.permissions.DANGEROUS_ACTIVITY_PERM"
    }

}
