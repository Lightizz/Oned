package fr.lightiz.oned

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class AddReminder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        val goBack = findViewById<ImageView>(R.id.add_reminder_goBack)
        goBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return@setOnClickListener
        }
    }
}