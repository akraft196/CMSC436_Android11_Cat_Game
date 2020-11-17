package course.labs.locationlab

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.util.Log
import org.w3c.dom.DOMException
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.*
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

class PlaceDownloaderTask(parent: PlaceViewActivity?, hasNetwork: Boolean) : AsyncTask<Location?, Void?, PlaceRecord?>() {
    // False if you don't have network access
    private var mHasNetwork = false
    private val mParent: WeakReference<PlaceViewActivity?>

    override fun doInBackground(vararg location: Location?): PlaceRecord? {
        var place: PlaceRecord?
        if (mHasNetwork) {

            // Get the PlaceBadge information
            place = getPlaceFromURL(generateURL(USERNAME, location[0]!!))
            place.location = location[0]
            if ("" !== place.countryName) {
                place.flagBitmap = getFlagFromURL(place.flagUrl)
            } else {
                place.flagBitmap = sStubBitmap
            }
        } else {
            place = PlaceRecord()
            place.location = location[0]
            place.flagBitmap = sStubBitmap
            if (place.intersects(sMockLoc1)) {
                place.countryName = sMockCountryName1
                place.place = sMockPlaceName1
            } else if (place.intersects(sMockLoc2)) {
                place.countryName = sMockCountryName1
                place.place = sMockPlaceName2
            } else {
                place.countryName = sMockCountryNameInvalid
                place.place = sMockPlaceNameInvalid
            }
        }
        return place
    }

    override fun onPostExecute(result: PlaceRecord?) {
        if (null != result && null != mParent.get()) {
            mParent.get()!!.addNewPlace(result)
        }
    }

    private fun getPlaceFromURL(vararg params: String): PlaceRecord {
        var result: String? = null
        var bufferedReader: BufferedReader? = null
        try {
            val url = URL(params[0])
            mHttpUrl = url.openConnection() as HttpURLConnection
            bufferedReader = BufferedReader(InputStreamReader(
                    mHttpUrl!!.inputStream))
            val sb = StringBuffer("")
            var line = bufferedReader.readLine()

            while (line != null) {
                sb.append(""" $line""".trimIndent() + "\n")
                line = bufferedReader.readLine()
            }
            result = sb.toString()
        } catch (e: MalformedURLException) {
        } catch (e: IOException) {
        } finally {
            try {
                bufferedReader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mHttpUrl!!.disconnect()
        }
        return placeDataFromXml(result)
    }

    companion object {
        // TODO - set your www.geonames.org account name as USERNAME.
        private const val USERNAME = "YOUR_USER_NAME"
        private val sMockLoc1 = Location(
                LocationManager.NETWORK_PROVIDER)
        private val sMockLoc2 = Location(
                LocationManager.NETWORK_PROVIDER)
        private val sMockLoc3 = Location(
                LocationManager.NETWORK_PROVIDER)
        private var sMockCountryName1: String? = null
        private var sMockCountryNameInvalid: String? = null
        private var sMockPlaceName1: String? = null
        private var sMockPlaceName2: String? = null
        private var sMockPlaceNameInvalid: String? = null
        private var sStubBitmap: Bitmap? = null
        private var mHttpUrl: HttpURLConnection? = null

        private fun placeDataFromXml(xmlString: String?): PlaceRecord {
            val builder: DocumentBuilder
            var countryName = ""
            var countryCode = ""
            var placeName = ""
            val factory = DocumentBuilderFactory.newInstance()
            try {
                builder = factory.newDocumentBuilder()
                val document = builder.parse(InputSource(StringReader(
                        xmlString)))
                val list = document.documentElement.childNodes
                for (i in 0 until list.length) {
                    val curr = list.item(i)
                    val list2 = curr.childNodes
                    for (j in 0 until list2.length) {
                        val curr2 = list2.item(j)
                        if (curr2.nodeName != null) {
                            if (curr2.nodeName == "countryName") {
                                countryName = curr2.textContent
                            } else if (curr2.nodeName == "countryCode") {
                                countryCode = curr2.textContent
                            } else if (curr2.nodeName == "name") {
                                placeName = curr2.textContent
                            }
                        }
                    }
                }
            } catch (e: DOMException) {
                e.printStackTrace()
            } catch (e: ParserConfigurationException) {
                e.printStackTrace()
            } catch (e: SAXException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return PlaceRecord(generateFlagURL(countryCode.toLowerCase(Locale.US)),
                    countryName, placeName)
        }

        private fun generateURL(username: String, location: Location): String {
            return ("http://www.geonames.org/findNearbyPlaceName?username="
                    + username + "&style=full&lat=" + location.latitude
                    + "&lng=" + location.longitude)
        }

        private fun generateFlagURL(countryCode: String): String {
            return "https://flagpedia.net/data/flags/small/${countryCode.toLowerCase(Locale.ROOT)}.png"
        }

        fun getFlagFromURL(flagUrl: String?): Bitmap? {

            if (flagUrl == null){
                return null
            }
            var `in`: InputStream? = null
            Log.i("temp", flagUrl)
            try {
                val url = URL(flagUrl)
                mHttpUrl = url.openConnection() as HttpURLConnection
                `in` = mHttpUrl!!.inputStream
                return BitmapFactory.decodeStream(`in`)
            } catch (e: MalformedURLException) {
                Log.e("DEBUG", e.toString())
            } catch (e: IOException) {
                Log.e("DEBUG", e.toString())
            } finally {
                try {
                    `in`?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                mHttpUrl!!.disconnect()
            }
            return null
        }

    }

    init {
        mParent = WeakReference(parent)
        mHasNetwork = hasNetwork
        if (null != parent) {
            sStubBitmap = BitmapFactory.decodeResource(parent.resources,
                    R.drawable.stub)
            sMockLoc1.latitude = 37.422
            sMockLoc1.longitude = -122.084
            sMockCountryName1 = parent
                    .getString(R.string.mock_name_united_states_string)
            sMockPlaceName1 = parent.getString(R.string.the_greenhouse_string)
            sMockLoc2.latitude = 38.996667
            sMockLoc2.longitude = -76.9275
            sMockPlaceName2 = parent.getString(R.string.berwyn_string)
            sMockLoc3.latitude = 0.0
            sMockLoc3.longitude = 0.0
            sMockCountryNameInvalid = ""
            sMockPlaceNameInvalid = ""
        }
    }
}