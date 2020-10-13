package course.labs.lab5_lifecycle_aware

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.view.View
import android.widget.TextView
import android.content.Context
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.history_list.view.*


class LifecycleMainActivity : AppCompatActivity() {
    private lateinit var mAdapter: HistoryListAdapter
    private lateinit var historyListView: ListView
    private lateinit var mCounterViewModel: CounterViewModel
    private lateinit var counterText: TextView
    private lateinit var addButton: Button
    private lateinit var resetButton: Button



    private fun getScreenOrientation(context: Context): String {
        val orientationList = listOf("Portrait","Landscape","Reverse Portrait","Reverse Landscape")
        val orientation = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
        return orientationList[orientation]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    //ToDo: Implement your own logic to display appropriate text ,increase and reset the counter and populate the history list
        // Add the Activity observer to preserve the counts till now and add  it to the list when the activity is created again
        mAdapter = HistoryListAdapter(applicationContext)
        historyListView = findViewById(R.id.listview)
        mCounterViewModel = ViewModelProviders.of(this).get(CounterViewModel::class.java)
        counterText = findViewById(R.id.DisplayText)
        addButton = findViewById(R.id.PrButton)
        resetButton = findViewById(R.id.ResButton)

        historyListView.adapter = mAdapter

        //set up the observer to update the text view whenever the value of counter in mCounterViewModel changes, which SHOULD be every time addButton or resetButton is clicked (unless the count is already 0 and reset is clicked)
        val counterObserver = Observer<Int> { counterText.text = getScreenOrientation(this) + "-" + mCounterViewModel.getCount().value }
        mCounterViewModel.counter.observe(this, counterObserver)

        //set up the buttons to do the things
        addButton.setOnClickListener {
            mCounterViewModel.incCount()
        }

        resetButton.setOnClickListener {
            mCounterViewModel.resetCount()
        }
    }

    override fun onPause() {
        super.onPause()
        mCounterViewModel.historyAdd(counterText.text.toString())
        mCounterViewModel.resetCount()
    }

    override fun onDestroy() {
        super.onDestroy()

        mAdapter.setHistory(mCounterViewModel.historyCounter)
    }
}
