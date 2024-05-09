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
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.database.FirebaseDatabase

class GuestLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_login)

        val guest_name = findViewById<TextView>(R.id.guest_login_name_txt)
        val guest_email = findViewById<TextView>(R.id.guest_login_email_txt)
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

            val name_txt=guest_name.text.toString().trim()
            val email_txt=guest_email.text.toString().trim()

            if (name_txt.isNotEmpty() && email_txt.isNotEmpty()){
                val emailPattern = "[a-zA-Z0-9._-]+@gmail.com"

                if(!email_txt.matches(emailPattern.toRegex())){
                    Toast.makeText(this,"Invalid Email", Toast.LENGTH_LONG).show()
                }else{

                    val database = FirebaseDatabase.getInstance()

                    val new_email = email_txt.replace(".", ",")
                    var userRef = database.getReference("guests").child(new_email)
                    userRef.setValue(null)
                        .addOnSuccessListener {
                            // Guest information saved successfully
                            userRef.child("email").setValue(email_txt)
                            userRef.child("name").setValue(name_txt)
                            Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show()
                            val intent = Intent(this, GuestHomePageActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            // Failed to save guest information
                            Toast.makeText(this, "Try Again", Toast.LENGTH_LONG).show()
                        }
                }

            }else{
                Toast.makeText(this,"Please fill in all fields", Toast.LENGTH_LONG).show()
            }
        }

        login_txt.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}