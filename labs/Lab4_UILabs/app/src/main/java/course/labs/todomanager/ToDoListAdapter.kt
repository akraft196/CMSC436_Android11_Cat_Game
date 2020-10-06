package course.labs.todomanager

import java.util.ArrayList

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.CompoundButton.inflate
import android.widget.RelativeLayout
import android.widget.TextView
import course.labs.todomanager.ToDoItem.Status

class ToDoListAdapter(private val mContext: Context) : BaseAdapter() {

    private var inflater: LayoutInflater = LayoutInflater.from(mContext)
    private val mItems = ArrayList<ToDoItem>()

    // Add a ToDoItem to the adapter
    // Notify observers that the data set has changed

    fun add(item: ToDoItem) {

        mItems.add(item)
        notifyDataSetChanged()

    }

    // Clears the list adapter of all items.

    fun clear() {

        mItems.clear()
        notifyDataSetChanged()

    }

    // Returns the number of ToDoItems

    override fun getCount(): Int {

        return mItems.size

    }

    // Retrieve the number of ToDoItems

    override fun getItem(pos: Int): Any {

        return mItems[pos]

    }

    // Get the ID for the ToDoItem
    // In this case it's just the position

    override fun getItemId(pos: Int): Long {

        return pos.toLong()

    }

    // TODO - Create a View for the ToDoItem at specified position
    // Remember to check whether convertView holds an already allocated View
    // before created a new View.
    // Consider using the ViewHolder pattern to make scrolling more efficient
    // See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        // TODO - Get the current ToDoItem
        val viewHolder: ViewHolder

        if (null == convertView) {

            viewHolder = ViewHolder()

            // TODO - Inflate the View for this ToDoItem
            var inflater = LayoutInflater.from(mContext)
            val view = inflater.inflate(R.layout.todo_item, null)
            view.tag = viewHolder

            viewHolder.position = view.verticalScrollbarPosition
            viewHolder.mItemLayout = view as RelativeLayout
            viewHolder.mTitleView = view.findViewById(R.id.titleView)
            viewHolder.mPriorityView = view.findViewById(R.id.priorityView)
            viewHolder.mDateView = view.findViewById(R.id.dateView)
            viewHolder.mStatusView = view.findViewById(R.id.statusCheckBox)

        } else {
            viewHolder = convertView.tag as ViewHolder
            viewHolder.mStatusView!!.setOnCheckedChangeListener(null)

        }

        // TODO - Fill in specific ToDoItem data
        // Remember that the data that goes in this View
        // corresponds to the user interface elements defined
        // in the layout file
        val currentItem = getItem(position) as ToDoItem

        // TODO - Display Title in TextView
        viewHolder.mTitleView?.text = currentItem.title


        // TODO - Set up Status CheckBox
        if(mItems[position].status == Status.DONE){
            viewHolder.mStatusView?.setChecked(true)
        } else {
            viewHolder.mStatusView?.setChecked(false)
        }

        // TODO - Display Priority in a TextView
        viewHolder.mPriorityView?.text = currentItem.priority.toString()
        // TODO - Display Time and Date
        viewHolder.mDateView?.text = ToDoItem.FORMAT.format(currentItem.date)

        return viewHolder.mItemLayout

    }

    internal class ViewHolder {
        var position: Int = 0
        var mItemLayout: RelativeLayout? = null
        var mTitleView: TextView? = null
        var mStatusView: CheckBox? = null
        var mPriorityView: TextView? = null
        var mDateView: TextView? = null
    }

    companion object {

        private val TAG = "Lab-UserInterface"
    }
}
