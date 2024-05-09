package com.umairkhalid.ebuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AboutMeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)

        val back_btn = findViewById<ImageButton>(R.id.aboutme_back_btn)
        val update_btn=findViewById<Button>(R.id.aboutme_update_btn)
        val aboutme_txt=findViewById<EditText>(R.id.aboutme_txt)

        back_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

        update_btn.setOnClickListener{

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("users")
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid
            val aboutme = aboutme_txt.text.toString()

            if (userId != null) {
                myRef.child(userId).child("about").setValue(aboutme)
                    .addOnSuccessListener {
                        // Bio updated successfully
                        Toast.makeText(this@AboutMeActivity, "About updated successfully", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        // Failed to update bio
                        Log.d("Update Bio Error","${e.message}")
                        Toast.makeText(this@AboutMeActivity, "Please Try Again", Toast.LENGTH_SHORT).show()
                    }
            }

//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
        }

        val aboutme_layout=findViewById<LinearLayout>(R.id.aboutme_layout)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        aboutme_layout.startAnimation(slideUp)

    }
}