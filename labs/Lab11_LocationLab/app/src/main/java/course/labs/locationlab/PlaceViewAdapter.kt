package course.labs.locationlab

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class PlaceViewAdapter(mContext: Context) : BaseAdapter() {

    private val list = ArrayList<PlaceRecord>()

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var newView = convertView
        val holder: ViewHolder
        val curr = list[position]
        if (null == convertView) {
            holder = ViewHolder()
            newView = inflater!!.inflate(R.layout.place_badge_view, parent, false)
            holder.flag = newView.findViewById<View>(R.id.flag) as ImageView
            holder.country = newView.findViewById<View>(R.id.country_name) as TextView
            holder.place = newView.findViewById<View>(R.id.place_name) as TextView
            newView.tag = holder
        } else {
            holder = newView?.tag as ViewHolder
        }
        holder.flag!!.setImageBitmap(curr.flagBitmap)
        holder.country?.text = curr.countryName
        holder.place?.text = curr.place
        return newView
    }

    internal class ViewHolder {
        var flag: ImageView? = null
        var country: TextView? = null
        var place: TextView? = null
    }

    fun intersects(location: Location?): Boolean {
        for (item in list) {
            if (item.intersects(location)) {
                return true
            }
        }
        return false
    }

    fun add(listItem: PlaceRecord) {
        list.add(listItem)
        notifyDataSetChanged()
    }

    fun removeAllViews() {
        list.clear()
        notifyDataSetChanged()
    }

    companion object {
        private var inflater: LayoutInflater? = null
    }

    init {
        inflater = LayoutInflater.from(mContext)
    }
}