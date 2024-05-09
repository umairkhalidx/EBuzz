package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private var  mAuth = FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = mAuth.currentUser
        if(currentUser != null){
            val nextActivityIntent = Intent(this, HomePageActivity::class.java)
            startActivity(nextActivityIntent)
            finish()
        }

        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.login_email_txt)
        val pass = findViewById<EditText>(R.id.login_pass_txt)
        val forgotPass = findViewById<TextView>(R.id.login_forgot_txt)
        val login_btn = findViewById<Button>(R.id.login_login_btn)
        val signup_btn = findViewById<Button>(R.id.login_signup_btn)
        val guest_btn = findViewById<Button>(R.id.login_guest_btn)

        val spannableString = SpannableString("Forgot Your Password?")
        spannableString.setSpan(
            UnderlineSpan(),
            0,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        forgotPass.text = spannableString

        login_btn.setOnClickListener {
            val email_txt=email.text.toString().trim()
            val pass_txt=pass.text.toString().trim()
            signinFun(email_txt,pass_txt)
        }

        guest_btn.setOnClickListener {
            val intent = Intent(this, GuestLoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        signup_btn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        forgotPass.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        val animation = findViewById<LottieAnimationView>(R.id.login_bee_animation)
        animation.playAnimation()

        val login_toplayout = findViewById<LinearLayout>(R.id.login_toplayout)
        val slidedown = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        login_toplayout.startAnimation(slidedown)

    }

    fun signinFun(email:String,pass:String){

        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    val nextActivityIntent = Intent(this, HomePageActivity::class.java)
                    startActivity(nextActivityIntent)
                    finish()
                } else {

                    Toast.makeText(this,"Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

}