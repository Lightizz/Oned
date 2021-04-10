package fr.lightiz.oned

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import fr.lightiz.oned.add_reminderpage.AddReminderDeviceItemDecoration
import fr.lightiz.oned.add_reminderpage.AddReminderDevicesAdapter
import fr.lightiz.oned.data.DatabaseManager
import fr.lightiz.oned.data.DatabaseManager.Singleton.devicesList
import fr.lightiz.oned.models.Device

class PopupAddReminderDevices(addReminder: AddReminder): Dialog(addReminder.context) {
    private lateinit var close: ImageView
    private lateinit var devicesRecyclerView: RecyclerView
    private lateinit var selectAll: CheckBox
    private lateinit var selectNothing: CheckBox

    var devicesMap = mutableMapOf<Device, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_add_reminders_devices)

        close = findViewById(R.id.popup_add_reminders_devices_close)
        devicesRecyclerView = findViewById(R.id.popup_add_reminders_devices_devices_rv)
        selectAll = findViewById(R.id.popup_add_reminders_devices_devices_select_all_cb)
        selectNothing = findViewById(R.id.popup_add_reminders_devices_devices_select_nothing_cb)

        DatabaseManager().updateData({}, FirebaseAuth.getInstance().currentUser, context, TextView(context))
        for(device in devicesList){
            devicesMap[device] = true
        }

        close.setOnClickListener {
            dismiss()
        }

        selectAll.setOnClickListener {
            for (device in devicesMap.keys){
                devicesMap[device] = true
            }
        }

        selectNothing.setOnClickListener {
            for (device in devicesMap.keys){
                devicesMap[device] = false
            }
        }

        devicesRecyclerView.adapter = AddReminderDevicesAdapter(devicesList, devicesMap)
        devicesRecyclerView.addItemDecoration(AddReminderDeviceItemDecoration())
    }
}