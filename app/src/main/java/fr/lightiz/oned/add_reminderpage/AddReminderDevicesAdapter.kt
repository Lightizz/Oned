package fr.lightiz.oned.add_reminderpage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import fr.lightiz.oned.MainActivity
import fr.lightiz.oned.R
import fr.lightiz.oned.models.Device

class AddReminderDevicesAdapter(
    private val deviceList: List<Device>,
    private val deviceMap: MutableMap<Device, Boolean>,
    private val selectAll: CheckBox,
    private val selectNothing: CheckBox,
    private val context: Context
) : RecyclerView.Adapter<AddReminderDevicesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val deviceName: TextView = view.findViewById(R.id.item_popup_add_reminders_device_name)
        val selectedCheckBox: CheckBox = view.findViewById(R.id.item_popup_add_reminders_device_enabled)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_popup_add_reminders_device, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDevice = deviceList[position]

        holder.deviceName.text = currentDevice.name

        if(!holder.selectedCheckBox.isChecked){
            holder.selectedCheckBox.toggle()
        }
        for (device in deviceList){
            deviceMap[device] = true
        }
        if(!selectAll.isChecked) {
            selectAll.toggle()
        }
        if(selectNothing.isChecked){
               selectNothing.toggle()
        }
        holder.selectedCheckBox.setOnClickListener {
            if(deviceMap[currentDevice] == true) {
                deviceMap[currentDevice] = false
                if(selectAll.isChecked){
                    selectAll.toggle()
                }

                //TODO Trouver pourquoi la devices map prend 2 fois les vales et donc finir de résoudre le problème

                if(!deviceMap.containsValue(true) && !selectNothing.isChecked){
                    selectNothing.toggle()
                }
            }else {
                deviceMap[currentDevice] = true
                if(selectNothing.isChecked){
                    selectNothing.toggle()
                }
                if(!deviceMap.containsValue(false) && !selectAll.isChecked){
                    selectAll.toggle()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }
}
