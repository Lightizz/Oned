package fr.lightiz.oned

import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
}

//        val logo = findViewById<ImageView>(R.id.splash_screen_logo)
//        logo.setOnClickListener {  }
//        val runnable: Runnable = object : Runnable {
//            override fun run() {
//                logo.animate().rotationBy(360F).withEndAction(this).setDuration(3000).setInterpolator(LinearInterpolator()).start()
//            }
//        }
//        logo.animate().rotationBy(360F).withEndAction(runnable).setDuration(3000).setInterpolator(LinearInterpolator()).start()