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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email=findViewById<EditText>(R.id.login_email_txt)
        val pass=findViewById<EditText>(R.id.login_pass_txt)
        val forgotPass=findViewById<TextView>(R.id.login_forgot_txt)
        val login_btn=findViewById<Button>(R.id.login_login_btn)
        val signup_btn=findViewById<Button>(R.id.login_signup_btn)
        val guest_btn=findViewById<Button>(R.id.login_guest_btn)

        val spannableString = SpannableString("Forgot Your Password?")
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        forgotPass.text = spannableString

        login_btn.setOnClickListener{
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish()
        }

        guest_btn.setOnClickListener{
            val intent = Intent(this, GuestLoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        signup_btn.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        forgotPass.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        val animation=findViewById<LottieAnimationView>(R.id.login_bee_animation)
        animation.playAnimation()

//        val login_bottomlayout=findViewById<LinearLayout>(R.id.login_bottomlayout)
//        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
//        login_bottomlayout.startAnimation(slideUp)


        val login_toplayout=findViewById<LinearLayout>(R.id.login_toplayout)
        val slidedown = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        login_toplayout.startAnimation(slidedown)
    }
}