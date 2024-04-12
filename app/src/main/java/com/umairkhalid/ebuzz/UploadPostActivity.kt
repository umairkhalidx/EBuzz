package com.umairkhalid.ebuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView

class UploadPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_post)

        val back_btn=findViewById<ImageButton>(R.id.upload_post_back_btn)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        val user_img = findViewById<ImageView>(R.id.uploadpost_user_image)
        val username = findViewById<TextView>(R.id.uploadpost_user_name)
        val post_btn = findViewById<Button>(R.id.uploadpost_post_btn)
        val post_layout = findViewById<LinearLayout>(R.id.uploadpost_post_cardview)
        val upload_textview = findViewById<EditText>(R.id.uploadpost_text_view)
        val upload_imageview = findViewById<LinearLayout>(R.id.uploadpost_photovideo_layout)

        post_layout.visibility=View.GONE
        upload_textview.visibility=View.GONE
        upload_imageview.visibility=View.GONE


        val category_spinner=findViewById<Spinner>(R.id.uploadpost_catergory_spinner)

        category_spinner.prompt = "Click Me"
        val categories = arrayOf("Click Me","Text", "Photo", "Video")

        val category_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        category_spinner.adapter = category_adapter

        category_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                category_spinner.prompt = selectedCategory

                when (selectedCategory) {
                    "Click Me" -> {
                        post_layout.visibility=View.GONE
                        upload_textview.visibility=View.GONE
                        upload_imageview.visibility=View.GONE
                    }
                    "Text" -> {
                        post_layout.visibility=View.VISIBLE
                        upload_textview.visibility=View.VISIBLE
                        upload_imageview.visibility=View.GONE
                    }
                    "Photo" -> {
                        post_layout.visibility=View.VISIBLE
                        upload_textview.visibility=View.GONE
                        upload_imageview.visibility=View.VISIBLE
                    }
                    "Video" -> {
                        //This will be changed to support video

                        post_layout.visibility=View.VISIBLE
                        upload_textview.visibility=View.GONE
                        upload_imageview.visibility=View.VISIBLE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                category_spinner.prompt = "Click Me"
            }
        }

    }
}