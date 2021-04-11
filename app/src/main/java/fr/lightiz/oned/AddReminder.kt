package fr.lightiz.oned

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class AddReminder : AppCompatActivity() {
    val context: Context = this

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

        val goBack = findViewById<ImageView>(R.id.add_reminder_goBack)
        goBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return@setOnClickListener
        }

        val popupDevices = findViewById<Button>(R.id.add_reminder_devices_popup_button)
        popupDevices.setOnClickListener {
            val popup = PopupAddReminderDevices(this)
            popup.create()
            popup.show()
        }
    }
}
