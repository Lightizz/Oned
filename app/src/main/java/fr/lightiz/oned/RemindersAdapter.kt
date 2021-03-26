package fr.lightiz.oned

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class RemindersAdapter(
    private val reminderList: List<Reminder>
) : RecyclerView.Adapter<RemindersAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val reminderTitle:TextView? = view.findViewById<TextView>(R.id.item_reminder_title)
        val reminderDate:TextView? = view.findViewById<TextView>(R.id.item_reminder_date)
        val deviceList = view.findViewById<LinearLayout>(R.id.item_reminder_devicesList)
        val reminderSwitch:Switch? = view.findViewById<Switch>(R.id.item_reminder_enabled_switch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_reminders, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentReminder = reminderList[position]
        
        holder.reminderTitle?.text = currentReminder.title
        holder.reminderDate?.text = currentReminder.reminderDate.toString() //TODO (Change to the correct format)
        if(holder.reminderSwitch != null && currentReminder.isEnabled){
            holder.reminderSwitch?.toggle()
        }
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }
}