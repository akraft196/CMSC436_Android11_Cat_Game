package course.labs.todomanager

import android.annotation.TargetApi
import android.app.Activity
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.text.ParseException
import java.util.Date

import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.View.inflate
import android.widget.TextView
import course.labs.todomanager.ToDoItem.Priority
import course.labs.todomanager.ToDoItem.Status
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ToDoManagerActivity : ListActivity() {

    internal lateinit var mAdapter: ToDoListAdapter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a new TodoListAdapter for this ListActivity's ListView
        mAdapter = ToDoListAdapter(applicationContext)

        // Put divider between ToDoItems and FooterView
        listView.setFooterDividersEnabled(true)

        // TODO - Inflate footerView for footer_view.xml file
        //setContentView(R.layout.footer_view)
        val footerView = inflate(this, R.layout.footer_view, null)

        // TODO - Add footerView to ListView
        listView.addFooterView(footerView)

        // TODO - Attach Listener to FooterView
        footerView.setOnClickListener {
            val intent = Intent(this, AddToDoActivity::class.java)
            startActivityForResult(intent, ADD_TODO_ITEM_REQUEST)
        }

        // TODO - Attach the adapter to this ListActivity's ListView
        listView.adapter = mAdapter
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.i(TAG, "Entered onActivityResult()")

        // TODO - Check result code and request code
        // if user submitted a new ToDoItem
        // Create a new ToDoItem from the data Intent
        // and then add it to the adapter
        if(requestCode == ADD_TODO_ITEM_REQUEST && resultCode == Activity.RESULT_OK){
            //set priority from appropriate returned extras string
            val priority: Priority
            val priorityString = data?.extras?.get(ToDoItem.PRIORITY) as String
            if (priorityString == "HIGH"){
                priority = ToDoItem.Priority.HIGH
            } else if (priorityString == "MED"){
                priority = ToDoItem.Priority.MED
            } else if (priorityString == "LOW"){
                priority = ToDoItem.Priority.LOW
            } else {
                priority = Priority.INV
            }

            //set status from appropriate returned extras string
            val status: Status
            val statusString = data?.extras?.get(ToDoItem.STATUS) as String
            if (statusString == "DONE"){
                status = ToDoItem.Status.DONE
            } else if (statusString == "NOTDONE"){
                status = Status.NOTDONE
            } else {
                status = Status.INV
            }

            //set date from appropriate returned extras string
            val date: LocalDateTime
            val dateString = data?.extras?.get(ToDoItem.DATE) as String
            val finalDate: Date

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            date = LocalDateTime.parse(dateString, formatter)
            finalDate = Date(date.year-1900, date.monthValue-1, date.dayOfMonth, date.hour, date.minute, date.second) //lol I have no idea why it adds 1900 years and 1 month

            val title = data?.extras?.get(ToDoItem.TITLE) as String

            val newToDo = ToDoItem(title, priority, status, finalDate)
            mAdapter.add(newToDo)
        }

    }

    // Do not modify below here

    public override fun onResume() {
        super.onResume()

        // Load saved ToDoItems, if necessary

        if (mAdapter.count == 0)
            loadItems()
    }

    override fun onPause() {
        super.onPause()

        // Save ToDoItems

        saveItems()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all")
        menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MENU_DELETE -> {
                mAdapter.clear()
                return true
            }
            MENU_DUMP -> {
                dump()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun dump() {
        for (i in 0 until mAdapter.count) {
            val data = (mAdapter.getItem(i) as ToDoItem).toLog()
            Log.i(TAG,
                    "Item " + i + ": " + data.replace(ToDoItem.ITEM_SEP, ","))
        }
    }

    // Load stored ToDoItems
    private fun loadItems() {
        var reader: BufferedReader? = null
        try {
            val fis = openFileInput(FILE_NAME)
            reader = BufferedReader(InputStreamReader(fis))

            var title: String? = null
            var priority: String? = null
            var status: String? = null
            var date: Date? = null

            do {
                title = reader.readLine();
                if (title == null)
                    break
                priority = reader.readLine()
                status = reader.readLine()
                date = ToDoItem.FORMAT.parse(reader.readLine())
                mAdapter.add(ToDoItem(title, Priority.valueOf(priority),
                        Status.valueOf(status), date))

            }
            while (true)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        } finally {
            if (null != reader) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    // Save ToDoItems to file
    private fun saveItems() {
        var writer: PrintWriter? = null
        try {
            val fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            writer = PrintWriter(BufferedWriter(OutputStreamWriter(
                    fos)))

            for (idx in 0 until mAdapter.count) {

                writer.println(mAdapter.getItem(idx))

            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            writer?.close()
        }
    }


    companion object {

        private val ADD_TODO_ITEM_REQUEST = 0
        private val FILE_NAME = "TodoManagerActivityData.txt"
        private val TAG = "Lab-UserInterface"

        // IDs for menu items
        private val MENU_DELETE = Menu.FIRST
        private val MENU_DUMP = Menu.FIRST + 1
    }
}