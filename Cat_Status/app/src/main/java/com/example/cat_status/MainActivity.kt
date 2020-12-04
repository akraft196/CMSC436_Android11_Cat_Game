package com.example.cat_status

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

// author: Ji Luo, Kelvin Ngo, Anna Kraft
// mainActivity view for our project should be handled by Ji Luo. In that activity field there
// should be a button that will activate the CatHouse activity. This mainActivity class will
// simulate that
class MainActivity : AppCompatActivity() {
    private var DefaultProgressTime:Long = 100000
    private var DefaultRate:Long = 1000
    private lateinit var catLists : ArrayList<Cat>
    private var favCat : Cat? = null
    private var uniqueCatID = 0

    private lateinit var foodbar: ProgressBar
    private lateinit var waterbar: ProgressBar
    private lateinit var toyStatu: TextView
    private lateinit var mCountDownTimerFood: CountDownTimer
    private lateinit var mCountDownTimerWater: CountDownTimer
    private lateinit var mCountDownTimerToy: CountDownTimer
    private var mTimeLeftInMillisFood:Long = DefaultProgressTime
    private var mTimeLeftInMillisWater:Long = DefaultProgressTime
    private var mTimeLeftInMillisToy:Long = 0
    private var isPlaying = false
    private lateinit var mNotificationManager: NotificationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val countPrefs = getSharedPreferences(SPCount, Context.MODE_PRIVATE)
        mTimeLeftInMillisFood = countPrefs.getLong(SPFOOD, DefaultProgressTime)
        mTimeLeftInMillisWater = countPrefs.getLong(SPWATER, DefaultProgressTime)
        mTimeLeftInMillisToy = countPrefs.getLong(SPTOY, 0)
        isPlaying = countPrefs.getBoolean(SPPLAY, false)
        // inflate catHouseView whenever the button is clicked
        val catHouseButton = findViewById<ImageButton>(R.id.catHouseButton)

        //Set up foodbar and food image
        foodbar = findViewById<ProgressBar>(R.id.food)
        foodbar.setOnClickListener(View.OnClickListener {
            fillFood()
        })
        val foodImage = findViewById<ImageView>(R.id.foodPNG)
        foodImage.setImageResource(R.drawable.food)
        //set up water bar and image
        waterbar = findViewById<ProgressBar>(R.id.water)
        waterbar.setOnClickListener(View.OnClickListener {
            fillWater()
        })
        val waterImage = findViewById<ImageView>(R.id.waterPNG)
        waterImage.setImageResource(R.drawable.water)

        eating()
        drinking()

        //set up toy and image
        val toyButton = findViewById<ImageView>(R.id.toy)
        toyButton.setImageResource(R.drawable.toy)
        toyButton.setOnClickListener(View.OnClickListener {
            mTimeLeftInMillisToy += 60000
            if(isPlaying){
               mCountDownTimerToy.cancel()
            }
            playing()
        })
        toyStatu = findViewById<TextView>(R.id.toyStatu)
        // catLists stores the cats the current user owns. We'll be using this data later
        // to add cats to our catHouse view
        catLists = arrayListOf<Cat>()



        // this chunk of code handles retrieving saved data when the app is closed.
        // how to save data to be retrieved in the first place is coded further down
        val sharedPreferences = getSharedPreferences(SPTITLE, MODE_PRIVATE)
        val jsonCatLists = sharedPreferences.getString(SPCATKEY, "")
        val jsonFavCat = sharedPreferences.getString(SPFAVKEY, "")
        uniqueCatID = sharedPreferences.getInt(UNIQUEID, 0)

        val gson = Gson()
        if(jsonFavCat != "") {
            favCat = gson.fromJson(jsonFavCat, Cat ::class.java)
        }
        if(jsonCatLists != "") {
            val type = object : TypeToken<List<Cat>>() {}.type
            catLists = gson.fromJson(jsonCatLists, type)
        } else {
            // adding example cats into the catLists to show that it works

            // need to add the cats into a array of cats. The only parameter to create a cat is an
            // id. The id needs to be unique and cannot be -1
            val apperanceGenerator = CatAppearanceGenerator(resources, applicationContext)

            while(uniqueCatID < 10) {
                catLists.add(Cat(uniqueCatID, apperanceGenerator.generateCats(uniqueCatID)))
                uniqueCatID++
            }
        }
        val favCatImage = findViewById<ImageView>(R.id.favoriteCatImage)
        favCatImage.x = -60F

        // inflates a view in the main activity with the current favorite cat
        if (favCat != null) {
            val favCatNameView = findViewById<TextView>(R.id.favCatName)


            favCatNameView.text = favCat?.getName()
            favCatNameView.textSize = 20F

            val fileName = "cat_${favCat?.getId()}.png"
            val directory = applicationContext.getDir("imageDir", Context.MODE_PRIVATE)
            val file = File(directory, "$fileName")

            val bmOptions = BitmapFactory.Options()

            var currBitmap = BitmapFactory.decodeFile(file.absolutePath, bmOptions)
            favCatImage.setImageBitmap(currBitmap)
            favCatImage.scaleX = 3F
            favCatImage.scaleY = 3F
        }

        // when this button is pressed, the cat house activity is created. We need the
        // list of cats and the favorite cat if there's any in order to keep the two views
        // (the main activity view and the CatHouseActivity view) consistent
        catHouseButton.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(applicationContext, CatHouseActivity::class.java)
                // we put the cats as an extra in the intent when we start up. We will handle
                // retrieving the data in CatHouseActivity
                intent.putExtra(ICATKEY, catLists)
                intent.putExtra(IFAVKEY, favCat)
                startActivityForResult(intent, 1)
            }
        )
    }

    override fun onStop() {
        val countPrefs = getSharedPreferences(SPCount, Context.MODE_PRIVATE)
        val editor = countPrefs.edit()

        editor.putLong(SPFOOD, mTimeLeftInMillisFood)
        editor.putLong(SPWATER, mTimeLeftInMillisWater)
        editor.apply()
        super.onStop()
    }

    // when the app is paused, we save the list of cats and the favorite cat inside sharedPreference
    // so the data won't be destroyed when the app is closed. as sharedPreferences only handles
    // a select few data types, we have to convert our cat list and favorite cat into a string using
    // Gson and Json
    override fun onPause() {
        val countPrefs = getSharedPreferences(SPCount, Context.MODE_PRIVATE)
        val editor = countPrefs.edit()
        editor.putLong(SPFOOD, mTimeLeftInMillisFood)
        editor.putLong(SPWATER, mTimeLeftInMillisWater)
        editor.putLong(SPTOY, mTimeLeftInMillisToy)
        editor.putBoolean(SPPLAY, isPlaying)
        editor.apply()


        val sharedPreferences = getSharedPreferences(SPTITLE, MODE_PRIVATE)
        val sharePEditor = sharedPreferences.edit()

        val gson = Gson()
        val jsonCatLists = gson.toJson(catLists)
        val jsonFavCat = gson.toJson(favCat)
        sharePEditor.putString(SPFAVKEY, jsonFavCat).apply()
        sharePEditor.putString(SPCATKEY, jsonCatLists).apply()
        sharePEditor.putInt(UNIQUEID, uniqueCatID).apply()
        super.onPause()
    }

    override fun onResume() {
        val countPrefs = getSharedPreferences(SPCount, Context.MODE_PRIVATE)
        mTimeLeftInMillisFood = countPrefs.getLong(SPFOOD, 60000)
        mTimeLeftInMillisWater = countPrefs.getLong(SPWATER, 60000)
        mTimeLeftInMillisToy = countPrefs.getLong(SPTOY, 0)
        isPlaying = countPrefs.getBoolean(SPPLAY, false)
        super.onResume()
    }

    // the intent sent to CatHouseActivity was sent using startActivityForResult(). The results
    // we need are the cat lists and the favorite cat. This is so that any changes made in the
    // CatHouseAtivity view is reflected in the main activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val extras = data?.extras
                catLists = extras?.get(ICATKEY) as ArrayList<Cat>
                if(extras.get(IFAVKEY) != null) {

                    favCat = data?.extras?.get(IFAVKEY) as Cat
                    if (favCat != null) {
                        val favCatImage = findViewById<ImageView>(R.id.favoriteCatImage)
                        val favCatNameView = findViewById<TextView>(R.id.favCatName)

                        favCatNameView.text = favCat?.getName()
                        favCatNameView.textSize = 20F

                        val fileName = "cat_${favCat?.getId()}.png"
                        val directory = applicationContext.getDir("imageDir", Context.MODE_PRIVATE)
                        val file = File(directory, "$fileName")
                        val bmOptions = BitmapFactory.Options()

                        var currBitmap = BitmapFactory.decodeFile(file.absolutePath, bmOptions)
                        favCatImage.setImageBitmap(currBitmap)

                        favCatImage.scaleX = 3F
                        favCatImage.scaleY = 3F

                    }
                } else {
                    favCat = null

                    val favCatNameView = findViewById<TextView>(R.id.favCatName)
                    favCatNameView.text = ""
                    val favCatImage = findViewById<ImageView>(R.id.favoriteCatImage)
                    favCatImage.setImageDrawable(null)
                }

            }
        }
    }

    //cats start eating the food
    private fun eating(){
        mCountDownTimerFood = object : CountDownTimer(mTimeLeftInMillisFood, DefaultRate) {
            override fun onTick(millisUntilFinished: Long) {
                foodbar.setProgress(100*millisUntilFinished.toInt()/DefaultProgressTime.toInt())
                mTimeLeftInMillisFood = millisUntilFinished
            }

            override fun onFinish() {
                foodbar.setProgress(0)
            }

        }
        mCountDownTimerFood.start()
    }

    //cats start drinking the water
    private fun drinking(){
        mCountDownTimerWater = object : CountDownTimer(mTimeLeftInMillisWater, DefaultRate) {
            override fun onTick(millisUntilFinished: Long) {
                waterbar.setProgress(100*millisUntilFinished.toInt()/DefaultProgressTime.toInt())
                mTimeLeftInMillisWater = millisUntilFinished
            }

            override fun onFinish() {
                waterbar.setProgress(0)
            }
        }
        mCountDownTimerWater.start()
    }

    private fun playing(){
        isPlaying = true
        mCountDownTimerToy = object : CountDownTimer(mTimeLeftInMillisToy, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                mTimeLeftInMillisToy = millisUntilFinished

                var hour = "" + mTimeLeftInMillisToy.toInt()/1000/60/60
                var mins = "" + mTimeLeftInMillisToy.toInt()/1000/60
                var second = "" + mTimeLeftInMillisToy.toInt()/1000 % 60
                if(mTimeLeftInMillisToy.toInt()/1000/60/60 < 10){
                    hour = "0$hour"
                }
                if(mTimeLeftInMillisToy.toInt()/1000/60 < 10){
                    mins = "0$mins"
                }
                if(mTimeLeftInMillisToy.toInt()/1000 % 60 < 10){
                    second = "0$second"
                }
                val time = "$hour:$mins:$second"

                toyStatu.setText(time)
            }

            override fun onFinish(){
                isPlaying = false
                toyStatu.setText("00:00")
            }
        }.start()
    }

    //refill food for cats
    private fun fillFood(){
        mCountDownTimerFood.cancel()
        mTimeLeftInMillisFood = DefaultProgressTime
        eating()
    }

    //refill water for cats
    private fun fillWater(){
        mCountDownTimerWater.cancel()
        mTimeLeftInMillisWater = DefaultProgressTime
        drinking()
    }
    /*
    private fun createNoitificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val mChannel = NotificationChannel(channelID1, "LowFoodChannel", NotificationManager.IMPORTANCE_DEFAULT)
            mChannel.description = "Water and Food situation"
            mNotificationManager.createNotificationChannel(mChannel)
        }

    }*/
    /*
    public fun sendOnChannel1(statu: String){
        val title = statu
        var message = ""
        if(statu == "LowFood"){
            message = "Your Food is Low, please come back and refill food for your cat!"
        }
        else if(statu == "LowWater"){
            message = "Your Water is Low, please come back and refill water for your cat!"
        }
        else if(statu == "NoFood"){
            message = "You are out of Food, no new cat will be find until you refill food for your cat!"
        }
        else{
            message = "You are out of Water, no new cat will be find until you refill water for your cat!"
        }
        createNoitificationChannels()
        val notification = NotificationCompat.Builder(this, channelID1)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNoitificationChannels()
        mNotificationManager.notify(1, notification)
    }*/

    companion object {
        const val SPCount = "countPrefs"
        const val SPTITLE = "catTitle"
        const val SPCATKEY = "catListsSP"
        const val SPFAVKEY = "favoriteSP"
        const val SPFOOD = "foodSP"
        const val SPWATER = "waterSP"
        const val SPTOY = "toySP"
        const val SPPLAY = "play"
        const val ICATKEY = "catListsIntent"
        const val IFAVKEY = "favoriteIntent"
        const val UNIQUEID = "uniqueID"
        const val channelID1 = "my_channel_01"
    }
}