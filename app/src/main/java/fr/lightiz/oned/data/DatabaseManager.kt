package fr.lightiz.oned.data

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.lightiz.oned.AccountLogin
import fr.lightiz.oned.models.Account
import fr.lightiz.oned.data.DatabaseManager.Singleton.accountList
import fr.lightiz.oned.data.DatabaseManager.Singleton.dbRefAccounts
import fr.lightiz.oned.data.DatabaseManager.Singleton.dbRefReminders
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

    fun updateData(callback: () -> Unit, loggedUser: FirebaseUser, context: Context){
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
                if(loggedUser == null || loggedUser.uid == null || !snapshot.hasChild(loggedUser.uid)){
                    Toast.makeText(context, "You are not logged in, you're going to be redirected.", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, AccountLogin::class.java))
                }
            }
            override fun onCancelled(error: DatabaseError) {  }
        })
        dbRefReminders.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (reminderList.isEmpty()) {
                    for (ds in snapshot.child(loggedUser.uid).children) {
                        reminderList.add(ds.getValue(Reminder::class.java) as Reminder)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        callback()
    }
}