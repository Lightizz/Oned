package fr.lightiz.oned

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AddDevice : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var loggedUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_add_device)

        auth = FirebaseAuth.getInstance()
        loggedUser = auth.currentUser

        if(loggedUser == null || loggedUser.uid == null) {
            Toast.makeText(this, "You are not logged in, you're going to be redirected.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AccountLogin::class.java))
            finish()
            return
        }
    }
}