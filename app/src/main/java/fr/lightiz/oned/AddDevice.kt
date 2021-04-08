package fr.lightiz.oned

import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fr.lightiz.oned.data.DatabaseManager
import fr.lightiz.oned.data.DatabaseManager.Singleton.dbRefDevices
import fr.lightiz.oned.data.DatabaseManager.Singleton.devicesList
import fr.lightiz.oned.models.Device
import fr.lightiz.oned.tools.RandomDeviceKeyGenerator

class AddDevice : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var loggedUser: FirebaseUser

    private lateinit var osTypesSpinner: Spinner
    private lateinit var goBack: ImageView
    private lateinit var confirm: TextView
    private lateinit var name: EditText

    var currentOsTypesSpinnerItem: Int = 0

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

        setContentView(R.layout.activity_add_device)

        osTypesSpinner = findViewById(R.id.add_device_type_spinner)
        goBack = findViewById(R.id.add_device_goBack)
        confirm = findViewById(R.id.add_device_confirm)
        name = findViewById(R.id.add_device_name_input)

        auth = FirebaseAuth.getInstance()
        loggedUser = auth.currentUser

        if(loggedUser == null || loggedUser.uid == null) {
            Toast.makeText(this, "You are not logged in, you're going to be redirected.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AccountLogin::class.java))
            finish()
            return
        }

        osTypesSpinner.onItemSelectedListener = this
        val osTypesSpinnerArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, MainActivity().OS_TYPES_ARRAY)
        osTypesSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        osTypesSpinner.adapter = osTypesSpinnerArrayAdapter

        goBack.setOnClickListener {
            startActivity(Intent(this, Devices::class.java))
            finish()
            return@setOnClickListener
        }

        confirm.setOnClickListener {
            val deviceToAd = Device(RandomDeviceKeyGenerator.generateKey(), name.text.toString(), currentOsTypesSpinnerItem)
            dbRefDevices.child(loggedUser.uid).child((devicesList.size).toString()).setValue(deviceToAd)

            DatabaseManager().updateData({}, loggedUser, this, TextView(this))
            startActivity(Intent(this, Devices::class.java))
            finish()
            return@setOnClickListener
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        AddDevice().currentOsTypesSpinnerItem = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        AddDevice().osTypesSpinner.setSelection(0)
    }
}