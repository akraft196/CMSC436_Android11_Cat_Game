package course.labs.locationlab

import android.os.SystemClock
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log

// Adapted from code found at:
// http://mobiarch.wordpress.com/2012/07/17/testing-with-mock-location-data-in-android/

class MockLocationProvider(private val mProviderName: String, ctx: Context) {

    private val mLocationManager: LocationManager = ctx
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun pushLocation(lat: Double, lon: Double) {
        val mockLocation = Location(mProviderName)
        mockLocation.latitude = lat
        mockLocation.longitude = lon
        mockLocation.altitude = 0.0
        mockLocation.time = System.currentTimeMillis()
        mockLocation.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        mockLocation.accuracy = 5f
        mLocationManager.setTestProviderLocation(mProviderName, mockLocation)
    }

    fun shutdown() {
        mLocationManager.removeTestProvider(mProviderName)
    }

    init {
        try {
            mLocationManager.addTestProvider(mProviderName, false, false, false,
                    false, true, true, true, 0, 5)
        } catch (e: IllegalArgumentException) {
            // ignore
        }
        mLocationManager.setTestProviderEnabled(mProviderName, true)
    }
}