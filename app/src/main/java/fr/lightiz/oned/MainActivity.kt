package fr.lightiz.oned

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Explode
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fr.lightiz.oned.data.DatabaseManager
import fr.lightiz.oned.data.DatabaseManager.Singleton.reminderList
import fr.lightiz.oned.home_page.ReminderItemDecoration
import fr.lightiz.oned.home_page.RemindersAdapter
import java.util.*


class MainActivity : AppCompatActivity() {

    val OS_TYPES_ARRAY = arrayOf("Android", "Windows10", "Linux", "MacOs", "Apple")

    private lateinit var auth: FirebaseAuth
    private lateinit var loggedUser: FirebaseUser

    private lateinit var emptyListText: TextView
    private lateinit var remindersRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            exitTransition = Explode()
        }

        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        remindersRecyclerView = findViewById(R.id.home_page_reminders_rv)
        emptyListText = findViewById(R.id.home_page_empty_list_txt)

        loggedUser = auth.currentUser
        if (loggedUser == null) {
            Toast.makeText(this, "You are not logged in, you're going to be redirected.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AccountLogin::class.java))
            finish()
            return
        }

        val dbManager = DatabaseManager()
        dbManager.updateData( {
            remindersRecyclerView.adapter = RemindersAdapter(reminderList)
            remindersRecyclerView.addItemDecoration(ReminderItemDecoration())
        }, loggedUser, this, emptyListText)

        val addReminderButton = findViewById<Button>(R.id.home_page_nav_bar_add_btn)
        addReminderButton.setOnClickListener {
            startActivity(Intent(this, AddReminder::class.java))
            finish()
            return@setOnClickListener
        }

        val deviceButton = findViewById<Button>(R.id.home_page_nav_bar_devices_btn)
        deviceButton.setOnClickListener {
            startActivity(Intent(this, Devices::class.java))
            finish()
            return@setOnClickListener
        }

        val accountImage = findViewById<ImageView>(R.id.home_page_nav_bar_account)
        accountImage.setOnClickListener {
            startActivity(Intent(this, AccountInfo::class.java))
            finish()
            return@setOnClickListener
        }

        val moreBg = findViewById<View>(R.id.home_page_more_bg)
        moreBg.setOnClickListener {
            val creditPop = CreditPopup(this)
            creditPop.create()
            creditPop.show()
        }
        val moreImage = findViewById<ImageView>(R.id.home_page_more_img)
        moreImage.setOnClickListener {
            val creditPop = CreditPopup(this)
            creditPop.create()
            creditPop.show()
        }

        createNotificationChannel()

        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_logo_round)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "A"
            val descriptionText = "A"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun devicesLogoToIDConverter(imgDrawable: Int): Int {
        when (imgDrawable) {
            R.drawable.logo_android -> {
                return 0
            }
            R.drawable.logo_windows -> {
                return 1
            }
            R.drawable.logo_linux -> {
                return 2
            }
            R.drawable.logo_macos -> {
                return 3
            }
            R.drawable.logo_apple -> {
                return 4
            }
            else -> {
                return 0
            }
        }
    }

    fun debugTest(context: Context, msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}

//        val logo = findViewById<ImageView>(R.id.splash_screen_logo)
//        logo.setOnClickListener {  }
//        val runnable: Runnable = object : Runnable {
//            override fun run() {
//                logo.animate().rotationBy(360F).withEndAction(this).setDuration(3000).setInterpolator(LinearInterpolator()).start()
//            }
//        }
//        logo.animate().rotationBy(360F).withEndAction(runnable).setDuration(3000).setInterpolator(LinearInterpolator()).start()