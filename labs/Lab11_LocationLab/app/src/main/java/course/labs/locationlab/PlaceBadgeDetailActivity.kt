package course.labs.locationlab

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class PlaceBadgeDetailActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO - implement the Activity

        val mFlagView = findViewById<ImageView>(R.id.flag)
        val mCountryNameView = findViewById<TextView>(R.id.country_name)
        val mPlaceNameView = findViewById<TextView>(R.id.place_name)
        val mLocationView = findViewById<TextView>(R.id.gps_coordinates)
        val mDateVisitedView = findViewById<TextView>(R.id.date_visited)

//        val packagedIntent = intent.getParcelableExtra<Intent>(PlaceViewActivity.INTENT_DATA)
//        val mPlaceRecord = PlaceRecord(packagedIntent)

//        mFlagView.setImageBitmap(mPlaceRecord.flagBitmap)

    }
}