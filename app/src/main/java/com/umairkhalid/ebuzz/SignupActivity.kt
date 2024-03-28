package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val layout1=findViewById<LinearLayout>(R.id.signup_layout1)
        val layout2=findViewById<LinearLayout>(R.id.signup_layout2)
        val layout3=findViewById<LinearLayout>(R.id.signup_layout3)
        layout2.visibility = View.GONE
        layout3.visibility = View.GONE

        val animation=findViewById<LottieAnimationView>(R.id.signup_animation)
        animation.playAnimation()

        //Layout 1 Variables
        val next_btn=findViewById<Button>(R.id.signup_layout1_nextbtn)
        val name=findViewById<TextView>(R.id.signup_name_txt)
        val email=findViewById<TextView>(R.id.signup_email_txt)
        val password=findViewById<TextView>(R.id.signup_password_txt)
        val confirmPass=findViewById<TextView>(R.id.signup_confirm_pass_txt)
        val login_txt_1=findViewById<TextView>(R.id.signup_login_txt_1)


        next_btn.setOnClickListener{
            layout1.visibility = View.GONE
            layout2.visibility = View.VISIBLE
            layout3.visibility = View.VISIBLE
            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
            layout2.startAnimation(slideUp)
        }

        login_txt_1.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Layout 2 Variables

        val signup_btn=findViewById<Button>(R.id.signup_layout1_signupbtn)
        val contact=findViewById<TextView>(R.id.signup_contact_txt)
        val country=findViewById<Spinner>(R.id.signup_country_spinner)
        val city=findViewById<Spinner>(R.id.signup_city_spinner)
        val age=findViewById<TextView>(R.id.signup_age_txt)
        val login_txt_2=findViewById<TextView>(R.id.signup_login_txt_2)

        signup_btn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        login_txt_2.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }




    }
}