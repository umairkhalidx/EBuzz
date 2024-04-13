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

class CreateGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        val back_btn = findViewById<ImageButton>(R.id.creategroup_back_btn)
        val name_txt = findViewById<EditText>(R.id.creategroup_email_txt)
        val description = findViewById<EditText>(R.id.creategroup_description_txt)
        val upload_btn = findViewById<Button>(R.id.creategroup_upload_photo_btn)
        val create_btn =findViewById<Button>(R.id.creategroup_create_btn)
        val group_type_spinner = findViewById<Spinner>(R.id.creategroup_grouptype_spinner)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        val creategroup_layout=findViewById<LinearLayout>(R.id.creategroup_layout)

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        creategroup_layout.startAnimation(slideUp)

        group_type_spinner.prompt = "Select Group Type"
        val group_types = arrayOf("Select Group Type","Educational", "Business", "Motivational",
            "Entertainment","Other")

        val group_type_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, group_types)
        group_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        group_type_spinner.adapter = group_type_adapter

        group_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                group_type_spinner.prompt = group_types[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                group_type_spinner.prompt = "Select Group Type"
            }
        }
    }
}