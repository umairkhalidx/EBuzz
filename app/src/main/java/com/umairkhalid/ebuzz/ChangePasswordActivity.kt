package com.umairkhalid.ebuzz

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val back_btn =findViewById<ImageButton>(R.id.changepassword_back_btn)
        val email = findViewById<TextView>(R.id.changepassword_email_txt)
        val change_btn = findViewById<Button>(R.id.changepassword_change_btn)

        val changepassword_layout=findViewById<LinearLayout>(R.id.changepassword_layout)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        changepassword_layout.startAnimation(slideUp)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        change_btn.setOnClickListener{
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Confirmation")
            builder.setMessage("Are you sure you want to change your password?")

            builder.setPositiveButton("Yes") { dialog, _ ->
                // Handle 'Yes' button click
                dialog.dismiss()
                onBackPressed()
                finish()
            }

            builder.setNegativeButton("No") { dialog, _ ->
                // Handle 'No' button click
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

    }
}