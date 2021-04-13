package fr.lightiz.oned

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import fr.lightiz.oned.add_reminderpage.AddReminderDeviceItemDecoration
import fr.lightiz.oned.add_reminderpage.AddReminderDevicesAdapter
import fr.lightiz.oned.data.DatabaseManager
import fr.lightiz.oned.data.DatabaseManager.Singleton.devicesList
import fr.lightiz.oned.models.Device

class PopupAddReminderDevices(addReminder: AddReminder, devicesMap_: MutableMap<Device, Boolean>): Dialog(addReminder.context) {
    private lateinit var close: ImageView
    private lateinit var devicesRecyclerView: RecyclerView
    private lateinit var selectAll: CheckBox
    private lateinit var selectNothing: CheckBox

    private var devicesMap = devicesMap_

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_add_reminders_devices)

        close = findViewById(R.id.popup_add_reminders_devices_close)
        devicesRecyclerView = findViewById(R.id.popup_add_reminders_devices_devices_rv)
        selectAll = findViewById(R.id.popup_add_reminders_devices_devices_select_all_cb)
        selectNothing = findViewById(R.id.popup_add_reminders_devices_devices_select_nothing_cb)

        DatabaseManager().updateData(
            {
                devicesRecyclerView.layoutManager = LinearLayoutManager(context)
                devicesRecyclerView.adapter = AddReminderDevicesAdapter(devicesList, devicesMap, selectAll, selectNothing)
                devicesRecyclerView.addItemDecoration(AddReminderDeviceItemDecoration())
            }, FirebaseAuth.getInstance().currentUser, context, TextView(context)
        )

        close.setOnClickListener {
            hide()
            dismiss()
        }

        selectAll.setOnClickListener {
            if(selectNothing.isChecked){
                selectNothing.toggle()
            }
            if(!devicesMap.containsValue(false)){
                selectNothing()
                selectNothing.toggle()
                return@setOnClickListener
            }

            selectAll()
        }

        selectNothing.setOnClickListener {
            if(selectAll.isChecked){
                selectAll.toggle()
            }
            if(!devicesMap.containsValue(true)){
                selectAll()
                selectAll.toggle()
                return@setOnClickListener
            }

            selectNothing()
        }
    }

    fun selectAll(){
        for (device in devicesMap.keys){
            devicesMap[device] = true
        }

        var i:Int = 0
        while(i < devicesRecyclerView.adapter!!.itemCount){
            val holder: AddReminderDevicesAdapter.ViewHolder = devicesRecyclerView.findViewHolderForAdapterPosition(i) as AddReminderDevicesAdapter.ViewHolder
            if(!holder.selectedCheckBox.isChecked){
                holder.selectedCheckBox.toggle()
            }
            i ++
        }
    }

    fun selectNothing(){
        for (device in devicesMap.keys){
            devicesMap[device] = false
        }

        var i:Int = 0
        while(i < devicesRecyclerView.adapter!!.itemCount){
            val holder: AddReminderDevicesAdapter.ViewHolder = devicesRecyclerView.findViewHolderForAdapterPosition(i) as AddReminderDevicesAdapter.ViewHolder
            if(holder.selectedCheckBox.isChecked){
                holder.selectedCheckBox.toggle()
            }
            i ++
        }
    }
}