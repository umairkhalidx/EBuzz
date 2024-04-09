package com.umairkhalid.ebuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RatingBar

class FeedbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val back_btn = findViewById<ImageButton>(R.id.feedback_back_btn)
        val submit_btn = findViewById<Button>(R.id.feedback_submit_btn)
        val email_txt = findViewById<EditText>(R.id.feedback_email_txt)
        val description =findViewById<EditText>(R.id.feedback_description_txt)
        val rating_bar = findViewById<RatingBar>(R.id.feedback_ratingbar)

        val feedback_layout = findViewById<LinearLayout>(R.id.feedback_layout)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        feedback_layout.startAnimation(slideUp)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        submit_btn.setOnClickListener{
            onBackPressed()
            finish()
        }
    }
}