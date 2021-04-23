package fr.lightiz.oned

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Explode
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import fr.lightiz.oned.data.DatabaseManager.Singleton.accountList
import fr.lightiz.oned.data.DatabaseManager.Singleton.dbRefReminders
import fr.lightiz.oned.data.DatabaseManager.Singleton.reminderList
import fr.lightiz.oned.models.Account
import fr.lightiz.oned.models.Device
import fr.lightiz.oned.models.Reminder
import java.util.*
import kotlin.collections.ArrayList

class AddReminder : AppCompatActivity() {
    val context: Context = this

    var devicesMap = mutableMapOf<Device, Boolean>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            exitTransition = Explode()
        }

        setContentView(R.layout.activity_add_reminder)

        var loggedUser = FirebaseAuth.getInstance().currentUser
        var currentAccount = Account()

        for(account in accountList){
            if(account.email == loggedUser.email){
                currentAccount = account
                break
            }
        }

        val timePicker = findViewById<TimePicker>(R.id.add_reminder_time)
        timePicker.setIs24HourView(currentAccount.fullHourSystem)

        val goBack = findViewById<ImageView>(R.id.add_reminder_goBack)
        goBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return@setOnClickListener
        }

        val popupDevices = findViewById<Button>(R.id.add_reminder_devices_popup_button)
        popupDevices.setOnClickListener {
            val popup = PopupAddReminderDevices(this, devicesMap)
            popup.create()
            popup.show()
        }

        val confirmBtn = findViewById<TextView>(R.id.add_reminder_confirm)
        confirmBtn.setOnClickListener {
            val datePicker = findViewById<DatePicker>(R.id.add_reminder_date)
            val date = Date(datePicker.year - 1900, datePicker.month, datePicker.dayOfMonth, timePicker.hour - 1, timePicker.minute - 1)

            val titleEditText = findViewById<EditText>(R.id.add_reminder_title_input)

            var selectedDevices = ArrayList<Device>()

            if(devicesMap.isEmpty() || !devicesMap.containsValue(true)){
                popupDevices.requestFocus()
                popupDevices.error = "You didn't selected the devices you want, please select them."
                return@setOnClickListener
            }

            for(device in devicesMap.keys){
                if(devicesMap[device] == true){
                    selectedDevices.add(device)
                }
            }

            if(titleEditText.text.isNullOrEmpty()){
                titleEditText.error = "Please choose a name for your new reminder"
                titleEditText.requestFocus()
                return@setOnClickListener
            }

            //TODO Bug de la date chelou

            val reminder = Reminder(titleEditText.text.toString(), date, selectedDevices, true)

            reminderList.add(reminder)

            dbRefReminders.child(loggedUser.uid).setValue(reminderList)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
