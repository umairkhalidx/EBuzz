package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView

class GuestLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_login)

        val guest_name = findViewById<TextView>(R.id.guest_login_name_txt)
        val getin_btn =findViewById<Button>(R.id.guest_login_getin_btn)
        val login_txt= findViewById<TextView>(R.id.guest_login_txt_1)

        val layout1=findViewById<LinearLayout>(R.id.guest_login_layout1)

        val animation=findViewById<LottieAnimationView>(R.id.guest_login_animation)
        animation.playAnimation()

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        layout1.startAnimation(slideUp)

        val spannableString = SpannableString("Log in")
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        login_txt.text = spannableString

        getin_btn.setOnClickListener{
            val intent = Intent(this, GuestHomePageActivity::class.java)
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