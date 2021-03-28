package fr.lightiz.oned

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.lightiz.oned.templates.Account
import fr.lightiz.oned.templates.Device
import fr.lightiz.oned.templates.Reminder
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

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_account_register)

        auth = FirebaseAuth.getInstance()

        register_login = findViewById(R.id.account_register_login)
        register_register_button = findViewById(R.id.account_register_register_button)
        register_name_input = findViewById(R.id.account_register_name_input)
        register_email_input = findViewById(R.id.account_register_email_input)
        register_age_input = findViewById(R.id.account_register_age_input)
        register_password_input = findViewById(R.id.account_register_password_input)
        register_progressbar = findViewById(R.id.account_register_progressbar)

        register_login.setOnClickListener {
            startActivity(Intent(this, AccountLogin::class.java))
            finish()
            return@setOnClickListener
        }

        register_register_button.setOnClickListener {
            var name:String = register_name_input.text.toString().trim()
            var email:String = register_email_input.text.toString().trim()
            var age:String = register_age_input.text.toString().trim()
            var password:String = register_password_input.text.toString().trim()

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

            register_progressbar.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isComplete) {
                    var calendar: Calendar = Calendar.getInstance()
                    var dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm")
                    var date:String = dateFormat.format(calendar.time)
                    var currentDate = Date()
                    var modifiedDate = Date(currentDate.year, currentDate.month, currentDate.date + 1, currentDate.hours, currentDate.minutes)

                    var localDevice = Device(RandomDeviceKeyGenerator.generateKey(), /*MainActivity().getDeviceName().toString()*/"Android", 0)
                    var devices = arrayListOf<Device>()
                    devices.add(localDevice)
                    var initReminder = Reminder("Init", modifiedDate, devices, true)
                    var reminders = arrayListOf<Reminder>()
                    reminders.add(initReminder)
                    var account = Account(name, email, password, age, true)
                    FirebaseDatabase.getInstance().getReference("accounts").child(FirebaseAuth.getInstance().currentUser.uid).setValue(account)
                    FirebaseDatabase.getInstance().getReference("devices").child(FirebaseAuth.getInstance().currentUser.uid).setValue(devices)
                    FirebaseDatabase.getInstance().getReference("reminders").child(FirebaseAuth.getInstance().currentUser.uid).setValue(reminders)
                    Toast.makeText(this, "You successfully registered your self! Thanks!", Toast.LENGTH_SHORT).show()
                    register_progressbar.visibility = View.INVISIBLE
                    startActivity(Intent(this, MainActivity::class.java))
                    FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                }else {
                    Toast.makeText(this, "An error has occurred while creating your account, if it persist, please contact support", Toast.LENGTH_LONG).show()
                    register_progressbar.visibility = View.INVISIBLE
                }
            }
        }
    }
}