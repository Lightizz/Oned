package fr.lightiz.oned

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import fr.lightiz.oned.data.DatabaseManager
import fr.lightiz.oned.data.DatabaseManager.Singleton.reminderList
import fr.lightiz.oned.home_page.ReminderItemDecoration
import fr.lightiz.oned.home_page.RemindersAdapter
import fr.lightiz.oned.models.Account
import fr.lightiz.oned.models.Device
import fr.lightiz.oned.models.Reminder
import fr.lightiz.oned.tools.RandomDeviceKeyGenerator
import java.text.SimpleDateFormat
import java.util.*

class AccountRegister : AppCompatActivity() {
    lateinit var auth:FirebaseAuth

    private lateinit var register_login: TextView
    private lateinit var register_register_button: TextView
    private lateinit var register_name_input: EditText
    private lateinit var register_email_input: EditText
    private lateinit var register_age_input: EditText
    private lateinit var register_password_input: EditText
    private lateinit var register_progressbar: ProgressBar
    private lateinit var thermsAndConditionsCheckBox: CheckBox

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
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

        setContentView(R.layout.activity_account_register)

        auth = FirebaseAuth.getInstance()

        register_login = findViewById(R.id.account_register_login)
        register_register_button = findViewById(R.id.account_register_register_button)
        register_name_input = findViewById(R.id.account_register_name_input)
        register_email_input = findViewById(R.id.account_register_email_input)
        register_age_input = findViewById(R.id.account_register_age_input)
        register_password_input = findViewById(R.id.account_register_password_input)
        register_progressbar = findViewById(R.id.account_register_progressbar)
        thermsAndConditionsCheckBox = findViewById(R.id.account_register_accept_therms_and_rules)

        register_login.setOnClickListener {
            startActivity(Intent(this, AccountLogin::class.java))
            finish()
            return@setOnClickListener
        }

        register_register_button.setOnClickListener {
            val name:String = register_name_input.text.toString().trim()
            val email:String = register_email_input.text.toString().trim()
            val age:String = register_age_input.text.toString().trim()
            val password:String = register_password_input.text.toString().trim()

            if(name.isEmpty()){
                register_name_input.error = "This field is required!"
                register_name_input.requestFocus()
                return@setOnClickListener
            }

            if(email.isEmpty()){
                register_email_input.error = "This field is required!"
                register_email_input.requestFocus()
                return@setOnClickListener
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                register_email_input.error = "Please provide a correct e-mail!"
                register_email_input.requestFocus()
                return@setOnClickListener
            }

            if(age.isEmpty()){
                register_age_input.error = "This field is required!"
                register_age_input.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()){
                register_password_input.error = "This field is required!"
                register_password_input.requestFocus()
                return@setOnClickListener
            }else if (password.length > 35 || password.length < 5){
                register_password_input.error = "Your password have to be between 6 and 35 characters long!"
                register_password_input.requestFocus()
                return@setOnClickListener
            }

            if(!thermsAndConditionsCheckBox.isChecked){
                thermsAndConditionsCheckBox.error = "This field is required!"
                thermsAndConditionsCheckBox.requestFocus()
                return@setOnClickListener
            }

            // TODO: faire le système de checkbox "therms of conditions" obligatoire et si checkbox exemple reminder est coché ou pas générer un reminder ou pas selon le choix
            register_progressbar.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isComplete) {
                    val exempleReminderCheckBox = findViewById<CheckBox>(R.id.account_register_reminder_example)

                    val currentDate = Date()
                    val modifiedDate = Date(currentDate.year, currentDate.month, currentDate.date + 1, currentDate.hours, currentDate.minutes)

                    val localDevice = Device(RandomDeviceKeyGenerator.generateKey(), "Android phone", 0)
                    val devices = arrayListOf<Device>()
                    devices.add(localDevice)

                    val initReminder = Reminder("Example", modifiedDate, devices, true)
                    val reminders = arrayListOf<Reminder>()
                    reminders.add(initReminder)

                    val account = Account(name, email, password, age, true)
                    FirebaseDatabase.getInstance().getReference("accounts").child(FirebaseAuth.getInstance().currentUser.uid).setValue(account)
                    FirebaseDatabase.getInstance().getReference("devices").child(FirebaseAuth.getInstance().currentUser.uid).setValue(devices)
                    if(exempleReminderCheckBox.isChecked){
                        FirebaseDatabase.getInstance().getReference("reminders").child(FirebaseAuth.getInstance().currentUser.uid).setValue(reminders)
                    }
                    Toast.makeText(this, "You successfully registered your self! Thanks!", Toast.LENGTH_SHORT).show()
                    register_progressbar.visibility = View.INVISIBLE

                    val loggedUser: FirebaseUser? = auth.currentUser

                    val dbManager = DatabaseManager()
                    if (loggedUser != null) {
                        dbManager.updateData( {
                        }, loggedUser, this)
                    }

                    finishAndRemoveTask()
                    Toast.makeText(this, "You have to restart the app to initialize your riminders", Toast.LENGTH_LONG).show()

                    return@addOnCompleteListener

//                    startActivity(Intent(this, MainActivity::class.java))
                    FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                }else {
                    Toast.makeText(this, "An error has occurred while creating your account, if it persist, please contact support", Toast.LENGTH_LONG).show()
                    register_progressbar.visibility = View.INVISIBLE
                }
            }
        }
    }
}