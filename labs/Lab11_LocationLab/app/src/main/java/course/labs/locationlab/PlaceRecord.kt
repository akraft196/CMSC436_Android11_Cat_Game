package course.labs.locationlab

import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import java.util.*

class PlaceRecord {
    var flagUrl: String? = null
    var countryName: String? = null
    var place: String? = null
    var flagBitmap: Bitmap? = null
    var location: Location? = null
    var dateVisited: Date? = null

    constructor(flagUrl: String?, country: String?, place: String?) {
        this.flagUrl = flagUrl
        this.countryName = country
        this.place = place
    }
    constructor(){}
    constructor(intent: Intent) {
        flagUrl = intent.getStringExtra("mFlagUrl")
        countryName = intent.getStringExtra("mCountryName")
        place = intent.getStringExtra("mPlaceName")
        location = intent.getParcelableExtra("mLocation") as? Location
        dateVisited = intent.getSerializableExtra("mDateVisited") as? Date
        flagBitmap = intent.getParcelableExtra("mFlagBitmap") as? Bitmap
    }

    fun packageToIntent(): Intent {
        val intent = Intent()
        intent.putExtra("mFlagUrl", flagUrl)
        intent.putExtra("mCountryName", countryName)
        intent.putExtra("mPlaceName", place)
        intent.putExtra("mLocation", location)
        intent.putExtra("mDateVisited", dateVisited)
        intent.putExtra("mFlagBitmap", flagBitmap)
        return intent
    }

    fun intersects(newLocation: Location?): Boolean {
        val tolerance = 1000.0
        return location!!.distanceTo(newLocation) <= tolerance
    }

    override fun toString(): String {
        return "Place: $place Country: $countryName"
    }
}