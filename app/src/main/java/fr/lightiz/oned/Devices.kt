package fr.lightiz.oned

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fr.lightiz.oned.data.DatabaseManager
import fr.lightiz.oned.data.DatabaseManager.Singleton.devicesList
import fr.lightiz.oned.devices_page.DevicesAdapter
import fr.lightiz.oned.devices_page.DevicesItemDecoration

class Devices : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var loggedUser: FirebaseUser

    private lateinit var devicesRecyclerView: RecyclerView
    private lateinit var emptyListText:TextView

    private lateinit var goBack: ImageView
    private lateinit var add: ImageView

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

        setContentView(R.layout.activity_devices)

        auth = FirebaseAuth.getInstance()
        loggedUser = auth.currentUser

        emptyListText = findViewById(R.id.device_list_empty_list_txt)
        devicesRecyclerView = findViewById(R.id.device_list_rv)
        goBack = findViewById(R.id.device_list_goBack)
        add = findViewById(R.id.device_list_add)

        if(loggedUser == null || loggedUser.uid == null) {
            Toast.makeText(this, "You are not logged in, you're going to be redirected.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AccountLogin::class.java))
            finish()
            return
        }

        val dbManager = DatabaseManager()
        dbManager.updateData( {
            if(devicesList.isEmpty()){
                emptyListText.visibility = View.VISIBLE
                return@updateData
            }
            devicesRecyclerView.adapter = DevicesAdapter(devicesList, this)
            devicesRecyclerView.addItemDecoration(DevicesItemDecoration())
        }, loggedUser, this, TextView(this))

        goBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return@setOnClickListener
        }

        add.setOnClickListener {
            startActivity(Intent(this, AddDevice::class.java))
            finish()
            return@setOnClickListener
        }
    }
}