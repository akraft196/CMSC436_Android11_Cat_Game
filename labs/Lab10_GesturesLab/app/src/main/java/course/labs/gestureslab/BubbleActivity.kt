package course.labs.gestureslab

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.gesture.*
import android.gesture.GestureOverlayView.OnGesturePerformedListener
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class BubbleActivity : Activity(), OnGesturePerformedListener {

    // The Main view
    private var mFrame: FrameLayout? = null

    // Bubble image's bitmap
    private var mBitmap: Bitmap? = null

    // Display dimensions
    private var mDisplayWidth: Int = 0
    private var mDisplayHeight: Int = 0

    // Sound variables

    // AudioManager
    private var mAudioManager: AudioManager? = null
    // SoundPool
    private var mSoundPool: SoundPool? = null
    // ID for the bubble popping sound
    private var mSoundID: Int = 0
    // Audio volume
    private var mStreamVolume: Float = 0.toFloat()

    // Gesture Detector
    private var mGestureDetector: GestureDetector? = null

    // Gesture Library
    private var mLibrary: GestureLibrary? = null

    @SuppressLint("ClickableViewAccessibility")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        // Set up user interface
        mFrame = findViewById<View>(R.id.frame) as FrameLayout

        // Load basic bubble Bitmap
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.b64)

        // TODO - Fetch GestureLibrary from raw
        mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures)


        val gestureOverlay = findViewById<View>(R.id.gestures_overlay) as GestureOverlayView

        // TODO - Make this the target of gesture detection callbacks
        gestureOverlay.addOnGesturePerformedListener(this)

        // TODO - implement OnTouchListener to pass all events received by the
        // gestureOverlay to the basic gesture detector

        gestureOverlay.setOnTouchListener { v, event -> mGestureDetector!!.onTouchEvent(event)}


        // Uncomment next line to turn off gesture highlights
        // gestureOverlay.setUncertainGestureColor(Color.TRANSPARENT);

        // Loads the gesture library
        mLibrary?.apply {
            if (!load()) {
                Log.i(TAG, "Could not load Gesture Library")
            }
        }
    }

        override fun onResume() {
            super.onResume()

            // Manage bubble popping sound
            // Use AudioManager.STREAM_MUSIC as stream type
            mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

            mStreamVolume = mAudioManager!!
                .getStreamVolume(AudioManager.STREAM_MUSIC).toFloat() / mAudioManager!!.getStreamMaxVolume(
                AudioManager.STREAM_MUSIC
            )

            val musicAttribute = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            mSoundPool = SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(musicAttribute)
                .build()

            mSoundID = mSoundPool!!.load(this, R.raw.bubble_pop, 1)
            // setupGestureDetector()
            mSoundPool!!.setOnLoadCompleteListener { soundPool, sampleId, status -> setupGestureDetector() }

            mSoundID = mSoundPool!!.load(this, R.raw.bubble_pop, 1)


        }

        override fun onWindowFocusChanged(hasFocus: Boolean) {
            super.onWindowFocusChanged(hasFocus)
            if (hasFocus) {

                // Get the size of the display so this View knows where borders are
                mDisplayWidth = mFrame!!.width
                mDisplayHeight = mFrame!!.height

            }
        }

        // Set up GestureDetector
        private fun setupGestureDetector() {

            mGestureDetector = GestureDetector(this,
                object : GestureDetector.SimpleOnGestureListener() {

                    // If a fling gesture starts on a BubbleView then change the
                    // BubbleView's velocity

                    override fun onFling(
                        event1: MotionEvent,
                        event2: MotionEvent, velocityX: Float, velocityY: Float
                    ): Boolean {

                        // TODO - Implement onFling actions.
                        // You can get all Views in mFrame one at a time
                        // using the ViewGroup.getChildAt() method
                        for(v in 0 until mFrame!!.childCount){
                            val bubble = mFrame!!.getChildAt(v) as BubbleView
                            if(bubble.intersects(event1.x, event1.y)){
                                bubble.deflect(velocityX, velocityY)
                            }
                        }

                        return true
                    }

                    // If a single tap intersects a BubbleView, then pop the
                    // BubbleView
                    // Otherwise, create a new BubbleView at the tap's location
                    // and add
                    // it to mFrame. You can get all views from mFrame with
                    // ViewGroup.getChildAt()

                    override fun onSingleTapConfirmed(event: MotionEvent): Boolean {

                        // TODO - Implement onSingleTapConfirmed actions.
                        // You can get all Views in mFrame using the
                        // ViewGroup.getChildCount() method
                        for(v in 0 until mFrame!!.childCount){
                            val bubble = mFrame!!.getChildAt(v) as BubbleView
                            if(bubble.intersects(event.x, event.y)){
                                bubble.stop(true)
                                return true
                            }
                        }
                        val newBubble = BubbleView(applicationContext, event.x, event.y)
                        mFrame!!.addView(newBubble)
                        newBubble.start()

                        return true
                    }

                    // Good practice to override this method because all
                    // gestures start with a ACTION_DOWN event
                    override fun onDown(event: MotionEvent): Boolean {
                        return true
                    }
                })
        }

        override fun onPause() {

            // Release all SoundPool resources

            mSoundPool!!.unload(mSoundID)
            mSoundPool!!.release()
            mSoundPool = null

            super.onPause()
        }

        override fun onGesturePerformed(overlay: GestureOverlayView, gesture: Gesture) {

            // TODO - Get gesture predictions and assign it to 'predictions' variable below
            val predictions: ArrayList<Prediction>? = mLibrary!!.recognize(gesture)

            if (predictions!!.size > 0) {

                // Get highest-ranked prediction
                val prediction = predictions[0]


                // TODO - Ignore predictions with a score of < MIN_PRED_SCORE and display a
                // toast message informing the user that no prediction was made. If
                // the prediction
                // matches the openMenu gesture, open the menu. If the prediction
                // matches
                // the addTen gesture, add 10 bubbles to the screen.
                val r = Random()
                if(prediction.score >= MIN_PRED_SCORE){
                    when(prediction.name) {
                        "addTen" -> {
                            for(i in 0 until 10){
                                val newBubble = BubbleView(applicationContext, r.nextInt(mDisplayWidth / 2).toFloat() + mDisplayWidth / 2, r.nextInt(mDisplayHeight / 2).toFloat() + mDisplayHeight / 2)
                                mFrame!!.addView(newBubble)
                                newBubble.start()
                            }
                        }
                        "openMenu" -> {
                            openOptionsMenu()
                        }
                    }
                }

            } else {


            }
        }
        // BubbleView is a View that displays a bubble.
        // This class handles animating, drawing, and popping amongst other
        // actions.
        // A new BubbleView is created for each bubble on the display

        inner class BubbleView internal constructor(context: Context, x: Float, y: Float) :
            View(context) {
            private val mPainter = Paint()
            private var mMoverFuture: ScheduledFuture<*>? = null
            private var mScaledBitmapWidth: Int = 0
            private var mScaledBitmap: Bitmap? = null
            private val BITMAP_SIZE = 64
            private val REFRESH_RATE = 40
            // location, speed and direction of the bubble
            private var mXPos: Float = 0.toFloat()
            private var mYPos: Float = 0.toFloat()
            private var mDx: Float = 0.toFloat()
            private var mDy: Float = 0.toFloat()
            private val mRadius: Float
            private val mRadiusSquared: Float
            private var mRotate: Long = 0
            private var mDRotate: Long = 0

            // Return true if the BubbleView is not on the screen after the move
            // operation
            private val isOutOfView: Boolean
                get() = (mXPos < 0 - mScaledBitmapWidth || mXPos > mDisplayWidth
                        || mYPos < 0 - mScaledBitmapWidth || mYPos > mDisplayHeight)

            init {
                Log.i(TAG, "Creating Bubble at: x:$x y:$y")

                // Create a new random number generator to
                // randomize size, rotation, speed and direction
                val r = Random()

                // Creates the bubble bitmap for this BubbleView
                createScaledBitmap(r)

                // Radius of the Bitmap
                mRadius = (mScaledBitmapWidth / 2).toFloat()
                mRadiusSquared = mRadius * mRadius

                // Adjust position to center the bubble under user's finger
                mXPos = x - mRadius
                mYPos = y - mRadius

                // Set the BubbleView's speed and direction
                setSpeedAndDirection(r)

                // Set the BubbleView's rotation
                setRotation(r)

                mPainter.isAntiAlias = true

            }

            private fun setRotation(r: Random) {

                if (speedMode == RANDOM) {

                    // Set rotation in range [1..3]
                    mDRotate = ((r.nextInt(3 * BITMAP_SIZE) + 1) / mScaledBitmapWidth).toLong()
                } else {
                    mDRotate = 0
                }
            }

            private fun setSpeedAndDirection(r: Random) {

                // Used by test cases
                when (speedMode) {

                    SINGLE -> {

                        mDx = 20f
                        mDy = 20f
                    }

                    STILL -> {

                        // No speed
                        mDx = 0f
                        mDy = 0f
                    }

                    else -> {

                        // Set movement direction and speed
                        // Limit movement speed in the x and y
                        // direction to [-3..3] pixels per movement.

                        mDx = (r.nextInt(mScaledBitmapWidth * 3) + 1) / mScaledBitmapWidth.toFloat()
                        mDx *= (if (r.nextInt() % 2 == 0) 1 else -1).toFloat()

                        mDy = (r.nextInt(mScaledBitmapWidth * 3) + 1) / mScaledBitmapWidth.toFloat()
                        mDy *= (if (r.nextInt() % 2 == 0) 1 else -1).toFloat()
                    }
                }
            }

            private fun createScaledBitmap(r: Random) {

                if (speedMode != RANDOM) {
                    mScaledBitmapWidth = BITMAP_SIZE * 3
                } else {

                    // Set scaled bitmap size in range [1..3] * BITMAP_SIZE
                    mScaledBitmapWidth = r.nextInt(2 * BITMAP_SIZE) + BITMAP_SIZE

                }

                // Create the scaled bitmap using size set above
                mScaledBitmap = Bitmap.createScaledBitmap(
                    mBitmap!!,
                    mScaledBitmapWidth, mScaledBitmapWidth, false
                )
            }

            // Start moving the BubbleView & updating the display
            fun start() {

                // Creates a WorkerThread
                val executor = Executors
                    .newScheduledThreadPool(1)

                // Execute the run() in Worker Thread every REFRESH_RATE
                // milliseconds
                // Save reference to this job in mMoverFuture
                mMoverFuture = executor.scheduleWithFixedDelay({
                    // Implement movement logic.
                    // Each time this method is run the BubbleView should
                    // move one step. If the BubbleView exits the display,
                    // stop the BubbleView's Worker Thread.
                    // Otherwise, request that the BubbleView be redrawn.

                    if (moveWhileOnScreen()) {
                        postInvalidate()
                    } else
                        stop(false)
                }, 0, REFRESH_RATE.toLong(), TimeUnit.MILLISECONDS)
            }

            // Returns true if the BubbleView intersects position (x,y)
            @Synchronized
            fun intersects(x: Float, y: Float): Boolean {

                // Return true if the BubbleView intersects position (x,y)

                val xDist = x - (mXPos + mRadius)
                val yDist = y - (mYPos + mRadius)

                return xDist * xDist + yDist * yDist <= mRadiusSquared

            }

            // Cancel the Bubble's movement
            // Remove Bubble from mFrame
            // Play pop sound if the BubbleView was popped

            fun stop(wasPopped: Boolean) {

                if (null != mMoverFuture) {

                    if (!mMoverFuture!!.isDone) {
                        mMoverFuture!!.cancel(true)
                    }

                    // This work will be performed on the UI Thread
                    mFrame!!.post {
                        // Remove the BubbleView from mFrame
                        mFrame!!.removeView(this@BubbleView)

                        // If the bubble was popped by user,
                        // play the popping sound
                        if (wasPopped) {
                            mSoundPool!!.play(
                                mSoundID, mStreamVolume,
                                mStreamVolume, 1, 0, 1.0f
                            )
                        }
                    }
                }
            }

            // Change the Bubble's speed and direction
            @Synchronized
            fun deflect(velocityX: Float, velocityY: Float) {
                mDx = velocityX / REFRESH_RATE
                mDy = velocityY / REFRESH_RATE
            }

            // Draw the Bubble at its current location
            @Synchronized
            override fun onDraw(canvas: Canvas) {

                // save the canvas
                canvas.save()

                // Increase the rotation of the original image by mDRotate
                mRotate += mDRotate

                // Rotate the canvas by current rotation
                // Hint - Rotate around the bubble's center, not its position

                canvas.rotate(
                    mRotate.toFloat(),
                    mXPos + mScaledBitmapWidth / 2,
                    mYPos + mScaledBitmapWidth / 2
                )

                // Draw the bitmap at it's new location
                canvas.drawBitmap(mScaledBitmap!!, mXPos, mYPos, mPainter)

                // Restore the canvas
                canvas.restore()

            }

            // Returns true if the BubbleView is still on the screen after the move
            // operation
            @Synchronized
            private fun moveWhileOnScreen(): Boolean {

                // Move the BubbleView

                mXPos += mDx
                mYPos += mDy

                return !isOutOfView

            }
        }

        override fun onBackPressed() {
            openOptionsMenu()
        }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            super.onCreateOptionsMenu(menu)

            menuInflater.inflate(R.menu.menu, menu)

            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.menu_still_mode -> {
                    speedMode = STILL
                    return true
                }
                R.id.menu_single_speed -> {
                    speedMode = SINGLE
                    return true
                }
                R.id.menu_random_mode -> {
                    speedMode = RANDOM
                    return true
                }
                R.id.quit -> {
                    exitRequested()
                    return true
                }
                else -> return super.onOptionsItemSelected(item)
            }
        }

        private fun exitRequested() {
            super.onBackPressed()
        }

        companion object {

            private val MIN_PRED_SCORE = 3.0
            // These variables are for testing purposes, do not modify
            private val RANDOM = 0
            private val SINGLE = 1
            private val STILL = 2
            var speedMode = RANDOM

            private val TAG = "Lab-Gestures"
        }
    }
