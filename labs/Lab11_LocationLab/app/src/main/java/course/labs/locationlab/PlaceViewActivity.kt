package course.labs.locationlab

import android.app.ListActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import java.util.*

class PlaceViewActivity : ListActivity(), LocationListener {
    private var mLastLocationReading: Location? = null
    private var mAdapter: PlaceViewAdapter? = null

    // default minimum time between new readings
    private val mMinTime: Long = 5000

    // default minimum distance between old and new readings.
    private val mMinDistance = 1000.0f
    private var mLocationManager: LocationManager? = null

    // A fake location provider used for testing
    private var mMockLocationProvider: MockLocationProvider? = null
    private var mockLocationOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the app's user interface
        // This class is a ListActivity, so it has its own ListView
        // ListView's adapter should be a PlaceViewAdapter

        // TODO - acquire reference to the LocationManager

        val placesListView = listView

        // TODO - Set an OnItemClickListener on the ListView placesListView to open a detail view when the user
        // clicks on a Place Badge.


        val footerView = layoutInflater.inflate(R.layout.footer_view,
                placesListView, false)
                ?: return


        // TODO - When the footerView's onClick() method is called, it must
        // issue the
        // following log call
        // Log.i(TAG,"Entered footerView.OnClickListener.onClick()");

        // footerView must respond to user clicks.
        // Must handle 3 cases:
        // 1) The current location is new - download new Place Badge. Issue the
        // following log call:
        // Log.i(TAG,"Starting Place Download");

        // 2) The current location has been seen before - issue Toast message.
        // Issue the following log call:
        // Log.i(TAG,"You already have this location badge");

        // 3) There is no current location - response is up to you. The best
        // solution is to disable the footerView until you have a location.
        // Issue the following log call:
        // Log.i(TAG,"Location data is not available");

        placesListView.addFooterView(footerView)
        mAdapter = PlaceViewAdapter(applicationContext)
        listAdapter = mAdapter
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(applicationContext, "android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(applicationContext, "android.permission.ACCESS_COARSE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@PlaceViewActivity, arrayOf("android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION"),
                    MY_PERMISSIONS_LOCATION)
        } else getLocationUpdates()
    }

    private fun getLocationUpdates(){
        try {
            startMockLocationManager()

            // TODO - Check NETWORK_PROVIDER and GPS_PROVIDER for an existing
            // location reading.
            // Only keep this last reading if it is fresh - less than 5 minutes old.


            // TODO - register to receive location updates from NETWORK_PROVIDER

        } catch (e: SecurityException) {
            Log.d(TAG, e.localizedMessage)
        }
    }

    override fun onPause() {
        shutdownMockLocationManager()
        // TODO - unregister for location updates

        super.onPause()
    }

    // Callback method used by PlaceDownloaderTask
    fun addNewPlace(place: PlaceRecord) {
        Log.i(TAG, "Entered addNewPlace()")
        if (place.countryName.isNullOrEmpty()) {
            showToast(getString(R.string.no_country_string))
        } else if (!mAdapter!!.intersects(place.location)) {
            place.dateVisited = Date()
            mAdapter!!.add(place)
            mAdapter!!.notifyDataSetChanged()
        } else {
            showToast(getString(R.string.duplicate_location_string))
        }
    }

    override fun onLocationChanged(currentLocation: Location) {

        // TODO - Handle location updates
        // Cases to consider
        // 1) If there is no last location, keep the current location.
        // 2) If the current location is older than the last location, ignore
        // the current location
        // 3) If the current location is newer than the last locations, keep the
        // current location.

    }

    override fun onProviderDisabled(provider: String) {
        // not implemented
    }

    override fun onProviderEnabled(provider: String) {
        // not implemented
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        // not implemented
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_badges -> {
                mAdapter!!.removeAllViews()
                true
            }
            R.id.place_one -> {
                mMockLocationProvider!!.pushLocation(37.422, -122.084)
                true
            }
            R.id.place_no_country -> {
                mMockLocationProvider!!.pushLocation(0.0, 0.0)
                true
            }
            R.id.place_two -> {
                mMockLocationProvider!!.pushLocation(38.996667, -76.9275)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(toast: String) {
        Toast.makeText(this@PlaceViewActivity, toast, Toast.LENGTH_LONG)
                .show()
    }

    private fun shutdownMockLocationManager() {
        Log.i(TAG, "shut: shutcalled");
        if (mockLocationOn) {
            Log.i(TAG, "shut: shutcalled in side if");
            mMockLocationProvider!!.shutdown()
            mockLocationOn = false
        }
    }

    private fun startMockLocationManager() {
        if (!mockLocationOn) {
            mMockLocationProvider = MockLocationProvider(
                    LocationManager.NETWORK_PROVIDER, this)
            mockLocationOn = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_LOCATION -> {
                var g = 0
                Log.d(TAG, "Perm?: " + permissions.size + " -? " + grantResults.size)
                for (perm in permissions) Log.d(TAG, "Perm: " + perm + " --> " + grantResults[g++])
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) getLocationUpdates() else {
                    Log.i(TAG, "Permission was not granted to access location")
                    finish()
                }
            }
        }
    }

    companion object {
        private const val FIVE_MINS = 5 * 60 * 1000.toLong()
        private const val TAG = "Lab-Location"
        var INTENT_DATA = "course.labs.locationlab.placerecord.IntentData"

        // False if you don't have network access
        @JvmField
        var sHasNetwork = true
        const val MY_PERMISSIONS_LOCATION = 4
    }
}