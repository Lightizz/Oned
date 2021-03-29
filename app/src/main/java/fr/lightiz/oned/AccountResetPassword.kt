package fr.lightiz.oned

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class AccountResetPassword : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var confirm: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var auth:FirebaseAuth

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

        setContentView(R.layout.activity_account_reset_password)

        emailInput = findViewById(R.id.account_reset_email_input)
        confirm = findViewById(R.id.account_reset_confirm_button)
        progressBar = findViewById(R.id.account_reset_progressbar)

        auth = FirebaseAuth.getInstance()

        confirm.setOnClickListener {
            sendResetRequest()
        }
    }

    private fun sendResetRequest() {
        var email:String = emailInput.text.toString().trim()

        if(email.isEmpty()){
            emailInput.error = "This field is required!"
            emailInput.requestFocus()
            return
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailInput.error = "Please provide a correct e-mail!"
            emailInput.requestFocus()
            return
        }

        progressBar.visibility = View.VISIBLE
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this, "An e-mail has been sent to $email !", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "Something went wrong while sending the e-mail, try again or contact the support", Toast.LENGTH_LONG).show()
            }
        }
    }
}