package com.umairkhalid.ebuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val back_btn = findViewById<ImageButton>(R.id.add_back_btn)
        val post_btn = findViewById<TextView>(R.id.add_post_btn)
        val group_btn = findViewById<TextView>(R.id.add_group_btn)
        val page_btn = findViewById<TextView>(R.id.add_page_btn)

        val menu_layout=findViewById<CardView>(R.id.bottomLayout)

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        menu_layout.startAnimation(slideUp)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        post_btn.setOnClickListener{

        }

        group_btn.setOnClickListener{

        }

        page_btn.setOnClickListener{

        }


    }
}