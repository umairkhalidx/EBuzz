package com.umairkhalid.ebuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView

class CreatePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_page)

        val back_btn = findViewById<ImageButton>(R.id.createpage_back_btn)
        val name_txt = findViewById<EditText>(R.id.createpage_email_txt)
        val description = findViewById<EditText>(R.id.createpage_description_txt)
        val upload_btn = findViewById<Button>(R.id.createpage_upload_photo_btn)
        val create_btn =findViewById<Button>(R.id.createpage_create_btn)
        val page_type_spinner = findViewById<Spinner>(R.id.createpage_pagetype_spinner)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        val createpage_layout=findViewById<LinearLayout>(R.id.createpage_layout)

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        createpage_layout.startAnimation(slideUp)

        page_type_spinner.prompt = "Select Page Type"
        val page_types = arrayOf("Select Page Type","Educational", "Business", "Motivational",
            "Entertainment","Other")

        val page_type_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, page_types)
        page_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        page_type_spinner.adapter = page_type_adapter

        page_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                page_type_spinner.prompt = page_types[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                page_type_spinner.prompt = "Select Page Type"
            }
        }

    }
}