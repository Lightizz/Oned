package fr.lightiz.oned

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import java.util.*

class SplashScreenActivity : AppCompatActivity() {

    var homeActivity = MainActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val handler = Handler()
        handler.postDelayed({ goToMainPage() }, 1000)
    }

    private fun goToMainPage(){
        val intent = Intent(applicationContext, homeActivity.javaClass)
        startActivity(intent)
        finish()
    }
}