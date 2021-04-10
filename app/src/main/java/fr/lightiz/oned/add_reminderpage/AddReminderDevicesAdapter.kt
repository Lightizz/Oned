package fr.lightiz.oned.add_reminderpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.lightiz.oned.R
import fr.lightiz.oned.models.Device

class AddReminderDevicesAdapter(
    private val deviceList: List<Device>,
    private val deviceMap: MutableMap<Device, Boolean>
) : RecyclerView.Adapter<AddReminderDevicesAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val deviceName = view.findViewById<TextView>(R.id.item_popup_add_reminders_device_name)
        val selectedCheckBox = view.findViewById<CheckBox>(R.id.item_popup_add_reminders_device_enabled)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_popup_add_reminders_device, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDevice = deviceList[position]

        holder.deviceName.text = currentDevice.name
        holder.selectedCheckBox.isChecked = deviceMap.get(currentDevice) as Boolean

        holder.selectedCheckBox.setOnClickListener {
            if(holder.selectedCheckBox.isChecked) {
                deviceMap[currentDevice] = false
            }else if(holder.selectedCheckBox.isChecked) {
                deviceMap[currentDevice] = true
            }
        }
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }
}
