package fr.lightiz.oned

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AccountLogin : AppCompatActivity() {
    private lateinit var account_login_register: TextView
    private lateinit var account_login_login_button: TextView
    private lateinit var account_login_email_input: TextView
    private lateinit var account_login_password_input: TextView
    private lateinit var account_login_progressbar: ProgressBar
    private lateinit var account_login_forgot_password: TextView

    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_account_login)

        account_login_register = findViewById(R.id.account_login_register)
        account_login_login_button = findViewById(R.id.account_login_login_button)
        account_login_email_input = findViewById(R.id.account_login_email_input)
        account_login_password_input = findViewById(R.id.account_login_password_input)
        account_login_progressbar = findViewById(R.id.account_login_progressbar)
        account_login_forgot_password = findViewById(R.id.account_login_forgot_password)

        auth = FirebaseAuth.getInstance()

        account_login_register.setOnClickListener {
            startActivity(Intent(this, AccountRegister::class.java))
            finish()
            return@setOnClickListener
        }

        account_login_login_button.setOnClickListener{
            loginUser()
        }

        account_login_forgot_password.setOnClickListener {
            startActivity(Intent(this, AccountResetPassword::class.java))
            finish()
            return@setOnClickListener
        }
    }

    private fun loginUser(){
        val email:String = account_login_email_input.text.toString().trim()
        val password: String = account_login_password_input.text.toString().trim()

        if(email.isEmpty()){
            account_login_email_input.error = "This field is required!"
            account_login_email_input.requestFocus()
            return
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            account_login_email_input.error = "Please provide a correct e-mail!"
            account_login_email_input.requestFocus()
            return
        }

        if(password.isEmpty()){
            account_login_password_input.error = "This field is required!"
            account_login_password_input.requestFocus()
            return
        }else if (password.length > 35 || password.length < 5){
            account_login_password_input.error = "Your password have to be between 6 and 35 characters long!"
            account_login_password_input.requestFocus()
            return
        }

        account_login_progressbar.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
            if(it.isComplete){
                var user:FirebaseUser? = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    if(user.isEmailVerified){
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        return@addOnCompleteListener
                    }else {
                        user.sendEmailVerification()
                        Toast.makeText(this, "You haven't verified your e-mail yet. Please go check your e-mail box and verify it!", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                }
            }else {
                Toast.makeText(this, "An error has occured while logging to your account, if it persist, please contact support", Toast.LENGTH_LONG).show()
                account_login_progressbar.visibility = View.INVISIBLE
            }
        }
    }
}