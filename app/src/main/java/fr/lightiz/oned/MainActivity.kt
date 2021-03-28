package fr.lightiz.oned

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.lightiz.oned.database.DatabaseManager
import fr.lightiz.oned.database.DatabaseManager.Singleton.dbRefReminders
import fr.lightiz.oned.home.ReminderItemDecoration
import fr.lightiz.oned.home.RemindersAdapter
import fr.lightiz.oned.templates.Reminder
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val loggedUser:FirebaseUser? = auth.currentUser
        if(loggedUser == null) {
            Toast.makeText(this, "You are not logged in, you're going to be redirected.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AccountLogin::class.java))
            finish()
            return
        }

        if(FirebaseDatabase.getInstance().getReference("reminders").child(loggedUser.uid).get() == null){
            Toast.makeText(this@MainActivity, "A database error has occurred, please contact support.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@MainActivity, AccountLogin::class.java))
            finish()
            return
        }

        val currentUserReminderList = arrayListOf<Reminder>()
        dbRefReminders.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (currentUserReminderList.isEmpty()) {
                    for (ds in snapshot.child(loggedUser.uid).children) {
                        currentUserReminderList.add(ds.getValue(Reminder::class.java) as Reminder)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        if(currentUserReminderList.isEmpty()) {

        }

        val dbManager = DatabaseManager()
        dbManager.updateData{
            val remindersRecyclerView = findViewById<RecyclerView>(R.id.home_page_reminders_rv)
            remindersRecyclerView.adapter = RemindersAdapter(currentUserReminderList)
            remindersRecyclerView.addItemDecoration(ReminderItemDecoration())
        }

        val accountImage = findViewById<ImageView>(R.id.home_page_nav_bar_account)
        accountImage.setOnClickListener {
            startActivity(Intent(this, AccountInfo::class.java))
            finish()
            return@setOnClickListener
        }
    }

    fun devicesLogoToIDConverter(id: Int): ImageView {
        when (id) {
            0 -> {
                val i = ImageView(this)
                i.setImageResource(R.drawable.logo_android)
                return i
            }
            1 -> {
                val i = ImageView(this)
                i.setImageResource(R.drawable.logo_windows)
                return i
            }
            2 -> {
                val i = ImageView(this)
                i.setImageResource(R.drawable.logo_linux)
                return i
            }
            3 -> {
                val i = ImageView(this)
                i.setImageResource(R.drawable.logo_macos)
                return i
            }
            4 -> {
                val i = ImageView(this)
                i.setImageResource(R.drawable.logo_apple)
                return i
            }
            else -> {
                val i = ImageView(this)
                i.setImageResource(R.drawable.logo_android)
                return i
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