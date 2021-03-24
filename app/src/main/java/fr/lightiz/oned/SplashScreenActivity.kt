package fr.lightiz.oned

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import java.util.*

class SplashScreenActivity : AppCompatActivity() {

    var homeActivity = MainActivity()

    private val splashScreenTime:Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen)

        val handler = Handler()
        handler.postDelayed({ goToMainPage() }, splashScreenTime)
    }

    private fun goToMainPage(){
        val intent = Intent(applicationContext, homeActivity.javaClass)
        startActivity(intent)
        finish()
    }
}