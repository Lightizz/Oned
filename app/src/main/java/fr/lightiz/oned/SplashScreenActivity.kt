package fr.lightiz.oned

import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.os.Handler
import android.transition.Explode
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fr.lightiz.oned.data.DatabaseManager
import fr.lightiz.oned.home_page.ReminderItemDecoration
import fr.lightiz.oned.home_page.RemindersAdapter
import org.w3c.dom.Text
import java.util.*

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    var homeActivity = MainActivity()

    private val splashScreenTime:Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            exitTransition = Explode()
        }

        setContentView(R.layout.activity_splash_screen)

        auth = FirebaseAuth.getInstance()
        val loggedUser: FirebaseUser? = auth.currentUser
        if (loggedUser == null) {
            Toast.makeText(this, "You are not logged in, you're going to be redirected.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AccountLogin::class.java))
            finish()
            return
        }

        val handler = Handler()
        handler.postDelayed({ goToMainPage() }, splashScreenTime)

        val dbManager = DatabaseManager()
        dbManager.updateData( {
        }, loggedUser, this, TextView(this))
    }

    private fun goToMainPage(){
        val intent = Intent(applicationContext, homeActivity.javaClass)
        startActivity(intent)
        finish()
    }
}