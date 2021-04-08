package fr.lightiz.oned.home_page

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fr.lightiz.oned.R
import fr.lightiz.oned.data.DatabaseManager.Singleton.dbRefReminders
import fr.lightiz.oned.models.Reminder

class RemindersAdapter(
    private val reminderList: List<Reminder>
) : RecyclerView.Adapter<RemindersAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val reminderTitle:TextView? = view.findViewById(R.id.item_reminder_title)
        val reminderDate:TextView? = view.findViewById(R.id.item_reminder_date)
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        val reminderSwitch:Switch? = view.findViewById(R.id.item_reminder_enabled_switch)
        val reminderEdit: ImageView = view.findViewById(R.id.item_reminder_edit)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var loggedUser: FirebaseUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_reminders, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentReminder = reminderList[position]
        
        holder.reminderTitle?.text = currentReminder.title
        holder.reminderDate?.text = "" + currentReminder.reminderDate.date + "/" + (currentReminder.reminderDate.month + 1) + "/" + (currentReminder.reminderDate.year + 1900) + " - " + (currentReminder.reminderDate.hours + 1) + ":" + (currentReminder.reminderDate.minutes + 1)
        if(holder.reminderSwitch != null && currentReminder.isEnabled){
            holder.reminderSwitch.toggle()
        }

        auth = FirebaseAuth.getInstance()
        loggedUser = auth.currentUser

        holder.reminderEdit.setOnClickListener {
            // TODO: 31/03/2021
        }

        // TODO: 07/04/2021 Delete ????

        holder.reminderSwitch?.setOnClickListener {
            if(currentReminder.isEnabled){
                dbRefReminders.child(loggedUser.uid).child(position.toString()).child("enabled").setValue(false)
            }else {
                dbRefReminders.child(loggedUser.uid).child(position.toString()).child("enabled").setValue(true)
            }
        }
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }
}