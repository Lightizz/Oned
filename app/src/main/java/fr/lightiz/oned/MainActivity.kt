package fr.lightiz.oned

import android.annotation.SuppressLint
import android.media.Image
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class MainActivity : AppCompatActivity() {
//  var accountActivity = MainActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main)

        /*
        val accountImage = findViewById<ImageView>(R.id.home_page_nav_bar_account)
        accountImage.setOnClickListener {
            val intent = Intent(applicationContext, accountActivity.javaClass)
            startActivity(intent)
            finish()
        }
        */

        val reminderList = arrayListOf<Reminder>()

        val remindersRecyclerView = findViewById<RecyclerView>(R.id.home_page_reminders_rv)
        remindersRecyclerView.adapter = RemindersAdapter(reminderList)
        remindersRecyclerView.addItemDecoration(ReminderItemDecoration())
    }

    fun devicesLogoToIDConverter(id: Int): ImageView {
        if (id == 0) {
            val i = ImageView(this)
            i.setImageResource(R.drawable.logo_android)
            return i
        } else if (id == 1) {
            val i = ImageView(this)
            i.setImageResource(R.drawable.logo_windows)
            return i
        } else if (id == 2) {
            val i = ImageView(this)
            i.setImageResource(R.drawable.logo_linux)
            return i
        } else if (id == 3) {
            val i = ImageView(this)
            i.setImageResource(R.drawable.logo_macos)
            return i
        } else if (id == 4) {
            val i = ImageView(this)
            i.setImageResource(R.drawable.logo_apple)
            return i
        } else {
            val i = ImageView(this)
            i.setImageResource(R.drawable.logo_android)
            return i
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