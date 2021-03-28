package fr.lightiz.oned.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.lightiz.oned.templates.Account
import fr.lightiz.oned.database.DatabaseManager.Singleton.accountList
import fr.lightiz.oned.database.DatabaseManager.Singleton.dbRefAccounts
import fr.lightiz.oned.templates.Device
import fr.lightiz.oned.templates.Reminder

class DatabaseManager {
    object Singleton {
        var dbRefAccounts = FirebaseDatabase.getInstance().getReference("accounts")
        var dbRefReminders = FirebaseDatabase.getInstance().getReference("reminders")
        var dbRefDevices = FirebaseDatabase.getInstance().getReference("devices")

        var accountList = arrayListOf<Account>()
        var devicesList = arrayListOf<Device>()
        var reminderList = arrayListOf<Reminder>()
    }

    fun updateData(callback: () -> Unit){
        dbRefAccounts.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                accountList.clear()
                for(ds in snapshot.children){
                    val account = ds.getValue(Account::class.java)

                    if(account != null){
                        accountList.add(account)
                    }else {
                        continue
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {  }
        })
        callback()
    }
}