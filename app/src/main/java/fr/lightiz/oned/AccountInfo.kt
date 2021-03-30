package fr.lightiz.oned

import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import fr.lightiz.oned.models.Account


class AccountInfo : AppCompatActivity() {
    private lateinit var user: FirebaseUser
    private lateinit var dbRef: DatabaseReference

    private lateinit var userID:String

    private lateinit var disconnect: ImageView
    private lateinit var name: TextView
    private lateinit var age: TextView
    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var format: TextView
    private lateinit var goBack: ImageView
    private lateinit var edit: ImageView

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

        setContentView(R.layout.activity_account_info)

        disconnect = findViewById(R.id.account_info_disconnect_img)
        name = findViewById(R.id.account_info_value_name)
        age = findViewById(R.id.account_info_value_age)
        email = findViewById(R.id.account_info_value_email)
        password = findViewById(R.id.account_info_value_password)
        format = findViewById(R.id.account_info_value_hour_format)
        goBack = findViewById(R.id.account_info_goBack)
        edit = findViewById(R.id.account_info_edit)

        disconnect.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, AccountLogin::class.java))
            finish()
            return@setOnClickListener
        }

        edit.setOnClickListener {
            // TODO: 30/03/2021 Make a popup window with editable values on edit texts and apply button
        }

        goBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return@setOnClickListener
        }

        user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "You are not logged in, you're going to be redirected.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AccountLogin::class.java))
            finish()
            return
        }
        dbRef = FirebaseDatabase.getInstance().getReference("accounts")
        userID = user.uid

        dbRef.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val account = snapshot.getValue(Account::class.java)
                if(account != null){
                    val name_:String = account.name
                    val age_:String = account.age
                    val email_:String = account.email
                    val password_:String = account.password
                    val format_:Boolean = account.fullHourSystem

                    name.text = name_
                    age.text = age_
                    email.text = email_
                    password.text = password_
                    if(format_){
                        format.text = "24h"
                    }else {
                        format.text = "12h"
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AccountInfo, "An error has occured, try again or contact support", Toast.LENGTH_LONG).show()
            }
        })

    }
}