package course.labs.lab5_lifecycle_aware
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

//ToDo: define your own view model that would be used to store the data when activity is destroyed during rotation

class CounterViewModel : ViewModel() {
    private val mCounter = MutableLiveData<Int>()

    val counter: LiveData<Int>
        get() = mCounter

    var historyCounter = ArrayList<String>()


    init{
        mCounter.value = 0
    }

    fun getCount(): MutableLiveData<Int> {
        Log.i(TAG, "CounterViewModel - entered getCount")
        return mCounter
    }

    fun incCount() {
        Log.i(TAG, "CounterViewModel - entered incCount")
        mCounter.setValue(mCounter.value?.inc())
    }

    fun resetCount() {
        Log.i(TAG, "CounterViewModel - entered resetCount")
        mCounter.setValue(0)
    }

    fun historyAdd(item: String){
        historyCounter.add(item)
    }

    companion object {
        val TAG = "Lab5"
    }
}