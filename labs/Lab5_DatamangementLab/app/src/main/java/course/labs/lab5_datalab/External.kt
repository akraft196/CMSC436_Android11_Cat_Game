package course.labs.lab5_datalab

import android.content.pm.PackageManager
import android.os.Build
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.*
import java.lang.StringBuilder

class External : AppCompatActivity() {
    protected lateinit var inputText: EditText
    protected lateinit var response: TextView
    protected lateinit var saveButton: Button
    protected lateinit var readButton: Button

    private val filename = "SampleFile.txt"
    private val filepath = "MyFileStorage"
    protected lateinit var myExternalFile: File
    internal var myData = ""
    private val isExternalStorageReadOnly: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState) {
                true
            } else false
        }

    private val isExternalStorageAvailable: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED == extStorageState) {
                true
            } else false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_external)

        inputText = findViewById(R.id.myInputText)
        response = findViewById(R.id.response)
        saveButton = findViewById(R.id.saveExternalStorage)
        readButton = findViewById(R.id.getExternalStorage)

        saveButton.setOnClickListener{
            if (needsRuntimePermission()) {
                requestPermissions(arrayOf(WRITE_PERM), 0)
            } else {
                if(isExternalStorageAvailable && !isExternalStorageReadOnly) {
                    Save()
                }
            }
        }

        readButton.setOnClickListener {
            if (needsRuntimePermission()) {
                requestPermissions(arrayOf(READ_PERM), 0)
            } else {
                if (isExternalStorageAvailable) {
                    Get()
                }
            }
        }
    }

    /*
        Save and Get written with help from here: https://www.javatpoint.com/kotlin-android-read-and-write-external-storage
     */

    fun Save() {
        val file = File(getExternalFilesDir(filepath), filename)
        if (!file.exists()){
            file.createNewFile()
        }

        if(file.exists()){
            Log.i(TAG, "file created successfully")
        }
        else{
            Log.i(TAG, "file was not created")
        }

        try {
            val content = inputText.text.toString()
            var outputStream = FileOutputStream(file)

            outputStream.write(content.toByteArray())
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        inputText.text.clear()
        makeToast(filename + " saved to External Storage...")
    }

    fun Get() {
        val file = File(getExternalFilesDir(filepath), filename)

        val inputStream = FileInputStream(file)
        val inputReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputReader)
        val stringBuilder = StringBuilder()

        var text = ""

        if(file.exists()) {
            text = bufferedReader.readText()
            inputStream.close()

            inputText.setText(text)
            makeToast(filename + "data retrieved from External Storage...")
        }
        else{
            makeToast("File not found")
        }
    }


    fun makeToast(string: String){
        val toast = Toast.makeText(applicationContext, string, Toast.LENGTH_SHORT)
        response.text = string
        toast.show()
    }

    private fun needsRuntimePermission(): Boolean {
        // Check the SDK version and whether the permission is already granted.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
            READ_PERM
        ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
            WRITE_PERM
        ) != PackageManager.PERMISSION_GRANTED
    }

    companion object{
        val READ_PERM = "android.permission.READ_EXTERNAL_STORAGE"
        val WRITE_PERM = "android.permission.WRITE_EXTERNAL_STORAGE"
        val TAG = "Lab5"
    }
}
