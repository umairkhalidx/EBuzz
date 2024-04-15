package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class Search1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search1)

        val back_btn=findViewById<ImageButton>(R.id.search1_back_btn)

        val group_btn = findViewById<Button>(R.id.search1_groups_btn)
        val pages_btn = findViewById<Button>(R.id.search1_pages_btn)
        val people_btn = findViewById<Button>(R.id.search1_people_btn)

        val group_img = findViewById<ImageView>(R.id.search1_groups_img)
        val pages_img = findViewById<ImageView>(R.id.search1_pages_img)
        val people_img = findViewById<ImageView>(R.id.search1_people_img)

        val group_txt = findViewById<TextView>(R.id.search1_groups_txt)
        val pages_txt = findViewById<TextView>(R.id.search1_pages_txt)
        val people_txt = findViewById<TextView>(R.id.search1_people_txt)

        back_btn.setOnClickListener{
//            val intent = Intent(this, HomePageActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()

        }

        group_btn.setOnClickListener{
            val intent = Intent(this, Search2Activity::class.java)
            startActivity(intent)
        }
        pages_btn.setOnClickListener{
            val intent = Intent(this, Search2Activity::class.java)
            startActivity(intent)
        }
        people_btn.setOnClickListener{
            val intent = Intent(this, Search2Activity::class.java)
            startActivity(intent)
        }


        group_img.setOnClickListener{
            val intent = Intent(this, Search2Activity::class.java)
            startActivity(intent)
        }
        pages_img.setOnClickListener{
            val intent = Intent(this, Search2Activity::class.java)
            startActivity(intent)
        }
        people_img.setOnClickListener{
            val intent = Intent(this, Search2Activity::class.java)
            startActivity(intent)
        }


        group_txt.setOnClickListener{
            val intent = Intent(this, Search2Activity::class.java)
            startActivity(intent)
        }
        pages_txt.setOnClickListener{
            val intent = Intent(this, Search2Activity::class.java)
            startActivity(intent)
        }
        people_txt.setOnClickListener{
            val intent = Intent(this, Search2Activity::class.java)
            startActivity(intent)
        }

    }
}