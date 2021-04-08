package fr.lightiz.oned.data

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.lightiz.oned.AccountLogin
import fr.lightiz.oned.models.Account
import fr.lightiz.oned.data.DatabaseManager.Singleton.accountList
import fr.lightiz.oned.data.DatabaseManager.Singleton.dbRefAccounts
import fr.lightiz.oned.data.DatabaseManager.Singleton.dbRefDevices
import fr.lightiz.oned.data.DatabaseManager.Singleton.dbRefReminders
import fr.lightiz.oned.data.DatabaseManager.Singleton.devicesList
import fr.lightiz.oned.data.DatabaseManager.Singleton.reminderList
import fr.lightiz.oned.models.Device
import fr.lightiz.oned.models.Reminder

class DatabaseManager {
    object Singleton {
        var dbRefAccounts = FirebaseDatabase.getInstance().getReference("accounts")
        var dbRefReminders = FirebaseDatabase.getInstance().getReference("reminders")
        var dbRefDevices = FirebaseDatabase.getInstance().getReference("devices")

        var accountList = arrayListOf<Account>()
        var reminderList = arrayListOf<Reminder>()
        var devicesList = arrayListOf<Device>()
    }

    fun updateData(
        callback: () -> Unit,
        loggedUser: FirebaseUser,
        context: Context,
        emptyReminderListText: TextView
    ) {
        dbRefAccounts.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                accountList.clear()
                if (loggedUser == null || loggedUser.uid == null || !snapshot.hasChild(loggedUser.uid)) {
                    context.startActivity(Intent(context, AccountLogin::class.java))
                }
                for (ds in snapshot.children) {
                    val account = ds.getValue(Account::class.java)

                    if (account != null) {
                        accountList.add(account)
                    } else {
                        continue
                    }
                }
                if (loggedUser == null || loggedUser.uid == null || !snapshot.hasChild(loggedUser.uid)) {
                    context.startActivity(Intent(context, AccountLogin::class.java))
                }
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        dbRefReminders.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                reminderList.clear()
                if (loggedUser == null || loggedUser.uid == null || !snapshot.hasChild(loggedUser.uid)) {
                    context.startActivity(Intent(context, AccountLogin::class.java))
                }
                for (ds in snapshot.child(loggedUser.uid).children) {
                    reminderList.add(ds.getValue(Reminder::class.java) as Reminder)
                }
                if (reminderList.isEmpty() && emptyReminderListText != null) {
                    emptyReminderListText.visibility = View.VISIBLE
                }
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        dbRefDevices.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                devicesList.clear()
                if (loggedUser == null || loggedUser.uid == null || !snapshot.hasChild(loggedUser.uid)) {
                    context.startActivity(Intent(context, AccountLogin::class.java))
                }
                for (ds in snapshot.child(loggedUser.uid).children) {
                    devicesList.add(ds.getValue(Device::class.java) as Device)
                }
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}