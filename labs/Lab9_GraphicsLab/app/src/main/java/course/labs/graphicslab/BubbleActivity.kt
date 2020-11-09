package course.labs.graphicslab

import java.util.Random
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.media.SoundPool.OnLoadCompleteListener
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout

class BubbleActivity : Activity() {

    // The Main view
    private var mFrame: RelativeLayout? = null

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

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        // Set up user interface
        mFrame = findViewById<View>(R.id.frame) as RelativeLayout

        // Load basic bubble Bitmap
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.b64)

    }

    override fun onResume() {
        super.onResume()

        // Manage bubble popping sound
        // Use AudioManager.STREAM_MUSIC as stream type

        mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        mStreamVolume = mAudioManager!!
                .getStreamVolume(AudioManager.STREAM_MUSIC).toFloat() / mAudioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        val musicAttribute = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

        mSoundPool = SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(musicAttribute)
                .build()

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

    override fun onPause() {
        mSoundPool!!.unload(mSoundID)
        mSoundPool!!.release()
        mSoundPool = null
        super.onPause()
    }

    // BubbleView is a View that displays a bubble.
    // This class handles animating, drawing, and popping amongst other actions.
    // A new BubbleView is created for each bubble on the display

    inner class BubbleView internal constructor(context: Context, x: Float, y: Float) : View(context) {
        private val mPainter = Paint()
        private var mMoverFuture: ScheduledFuture<*>? = null
        private var mScaledBitmapWidth: Int = 0
        private var mScaledBitmap: Bitmap? = null

        // location, speed and direction of the bubble
        private var mXPos: Float
        private var mYPos: Float
        private var mDx: Float = 0.toFloat()
        private var mDy: Float = 0.toFloat()
        private val mRadius: Float
        private val mRadiusSquared: Float
        private var mRotate: Long = 0
        private var mDRotate: Long = 0

        // Return true if the BubbleView is still on the screen after the move
        // operation
        private// TODO - Return true if the BubbleView is still on the screen after
        // the move operation
        val isOutOfView: Boolean
            get() = !(mXPos < 0 - mScaledBitmapWidth || mXPos > mDisplayWidth + mScaledBitmapWidth || mYPos < 0 - mScaledBitmapWidth || mYPos > mDisplayHeight + mScaledBitmapWidth)

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

                // TODO - set rotation in range [1..3]
                mDRotate = (r.nextInt(3-1) + 1).toLong()

            } else {

                mDRotate = 0

            }
        }

        private fun setSpeedAndDirection(r: Random) {

            // Used by test cases
            when (speedMode) {

                SINGLE -> {

                    mDx = 40f
                    mDy = 40f
                }

                STILL -> {

                    // No speed
                    mDx = 0f
                    mDy = 0f
                }

                RANDOM -> {
                    mDx = -3f + r.nextFloat() * (3f + 3f)
                    mDy = -3f + r.nextFloat() * (3f + 3f)
                }
            }// TODO - Set movement direction and speed
            // Limit movement speed in the x and y
            // direction to [-3..3] pixels per movement.

        }

        private fun createScaledBitmap(r: Random) {

            if (speedMode != RANDOM) {
                mScaledBitmapWidth = BITMAP_SIZE * 3
            } else {

                // TODO - set scaled bitmap size in range [1..3] * BITMAP_SIZE
                mScaledBitmapWidth = BITMAP_SIZE * (r.nextInt(3-1) + 1)
            }

            // TODO - create the scaled bitmap using size set above
            mScaledBitmap = Bitmap.createScaledBitmap(mBitmap, mScaledBitmapWidth, mScaledBitmapWidth, false)
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
                // TODO - implement movement logic.
                // Each time this method is run the BubbleView should
                // move one step. If the BubbleView exits the display,
                // stop the BubbleView's Worker Thread.
                // Otherwise, request that the BubbleView be redrawn.
                Log.i(TAG, "bubble speed: x: $mDx y: $mDy")
                if(moveWhileOnScreen()){
                    this@BubbleView.invalidate()
                }
                else {
                    this.stop(false)
                }
            }, 0, REFRESH_RATE.toLong(), TimeUnit.MILLISECONDS)
        }

        // Cancel the Bubble's movement
        // Remove Bubble from mFrame
        // Play pop sound if the BubbleView was popped

        internal fun stop(wasPopped: Boolean) {

            if (null != mMoverFuture) {

                if (!mMoverFuture!!.isDone) {
                    mMoverFuture!!.cancel(true)
                }

                // This work will be performed on the UI Thread
                mFrame!!.post {
                    // TODO - Remove the BubbleView from mFrame
                    mFrame!!.removeView(this@BubbleView)

                    // If the bubble was popped by user,
                    // play the popping sound and Log that pop was performed
                    if (wasPopped) {
                        Log.i(TAG, "Pop!")
                        mSoundPool!!.play(mSoundID, mStreamVolume,
                                mStreamVolume, 1, 0, 1.0f)
                    }
                    Log.i(TAG, "Bubble removed from view!")
                }
            }
        }

        // Draw the Bubble at its current location
        @Synchronized
        override fun onDraw(canvas: Canvas) {

            // TODO - save the canvas
            canvas.save()
            // TODO - increase the rotation of the original image by mDRotate
            mRotate += mDRotate
            // TODO Rotate the canvas by current rotation
            // Hint - Rotate around the bubble's center, not its position
            canvas.rotate(mRotate.toFloat(), mXPos + (mScaledBitmapWidth / 2), mYPos + (mScaledBitmapWidth / 2))

            // TODO - draw the bitmap at it's new location
            canvas.drawBitmap(mScaledBitmap, mXPos, mYPos, mPainter)
            // TODO - restore the canvas
            canvas.restore()
        }

        // Returns true if the BubbleView is still on the screen after the move
        // operation
        @Synchronized
        private fun moveWhileOnScreen(): Boolean {

            // TODO - Move the BubbleView
            mXPos += mDx
            mYPos += mDy

            return isOutOfView

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
        // TODO- Handle addition and deletion of bubbles from options menu
        // Added bubbles should be given random locations.
        // The bubble to delete is the most recently added bubble
        // that is still in the frame.

        // Hint: You can get all Views in mFrame using the
        // ViewGroup.getChildCount() method
        when (item.itemId) {
            R.id.menu_add_bubble -> {
                val r = Random()
                val newestBubble = BubbleView(applicationContext, r.nextInt(mDisplayWidth / 4).toFloat() + mDisplayWidth / 4, r.nextInt(mDisplayHeight / 4).toFloat() + mDisplayHeight / 4)
                mFrame!!.addView(newestBubble)
                newestBubble.start()
                return true
            }
            R.id.menu_delete_bubble -> {
                (mFrame!!.getChildAt(mFrame!!.childCount - 1) as BubbleView).stop(true)
                //mFrame!!.removeViewAt(mFrame!!.childCount - 1)
                return true
            }
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

        // These variables are for testing purposes, do not modify
        private val RANDOM = 0
        private val SINGLE = 1
        private val STILL = 2
        private var speedMode = RANDOM
        private val BITMAP_SIZE = 64
        private val REFRESH_RATE = 40
        private val TAG = "Lab-Graphics"
    }
}