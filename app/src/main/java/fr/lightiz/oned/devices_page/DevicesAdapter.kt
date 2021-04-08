package fr.lightiz.oned.devices_page

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.lightiz.oned.AccountLogin
import fr.lightiz.oned.Devices
import fr.lightiz.oned.MainActivity
import fr.lightiz.oned.R
import fr.lightiz.oned.data.DatabaseManager.Singleton.dbRefDevices
import fr.lightiz.oned.models.Device

class DevicesAdapter(
        private val deviceList: List<Device>,
        private val context: Context?
) : RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder>() {

    class DeviceViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val deviceTitle: TextView? = view.findViewById(R.id.item_devices_title)
        val deviceOSLogo: ImageView? = view.findViewById(R.id.item_devices_os)
        val deviceEdit: ImageView? = view.findViewById(R.id.item_devices_edit)
        val deviceDelete: ImageView? = view.findViewById(R.id.item_devices_delete)
        val deviceKey: TextView? = view.findViewById(R.id.item_device_key)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var loggedUser: FirebaseUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_devices, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val currentDevice = deviceList[position]

        auth = FirebaseAuth.getInstance()
        loggedUser = auth.currentUser

        holder.deviceTitle?.text = currentDevice.name
        holder.deviceOSLogo?.setImageResource(MainActivity().devicesLogoToIDConverter(currentDevice.os))
        holder.deviceKey?.text = currentDevice.deviceKey

        holder.deviceEdit?.setOnClickListener {
            // TODO: 30/03/2021 Make a popup window with editable values on edit texts and apply button
        }

        holder.deviceDelete?.setOnClickListener {
            if(currentDevice.os == 0 && deviceList.size <= 1) {
                Toast.makeText(context, "You can't delete all the device!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dbRefDevices.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (loggedUser == null || loggedUser.uid == null || !snapshot.hasChild(loggedUser.uid)) {
                        context?.startActivity(Intent(context, AccountLogin::class.java))
                    }
                    for (ds in snapshot.child(loggedUser.uid).children) {
                        val device = ds.getValue(Device::class.java) as Device
                        if(device.deviceKey == currentDevice.deviceKey) {
                            dbRefDevices.child(loggedUser.uid).child(ds.key.toString()).removeValue()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

            context?.startActivity(Intent(context, Devices::class.java))
        }
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }
}