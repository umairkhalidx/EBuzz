package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val email=findViewById<TextView>(R.id.forgot_email_txt)
        val password=findViewById<TextView>(R.id.forgot_password_txt)
        val confirmPass=findViewById<TextView>(R.id.forgot_confirm_pass_txt)
        val login_txt=findViewById<TextView>(R.id.forgot_login_txt)
        val back_btn=findViewById<ImageButton>(R.id.forgot_back_btn)
        val send_btn=findViewById<Button>(R.id.forgot_send_btn)
        val forgot_layout=findViewById<LinearLayout>(R.id.forgot_layout)

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        forgot_layout.startAnimation(slideUp)

        val spannableString = SpannableString("Log in")
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        login_txt.text = spannableString



        send_btn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        back_btn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        login_txt.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}