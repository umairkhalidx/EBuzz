package com.umairkhalid.ebuzz

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.ToggleButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val back_btn = findViewById<ImageButton>(R.id.settings_back_btn)
        val account_btn = findViewById<Button>(R.id.settings_account_btn)
        val password_btn = findViewById<Button>(R.id.settings_password_btn)
        val report_btn = findViewById<Button>(R.id.settings_report_btn)
        val feedback_btn  = findViewById<Button>(R.id.settings_feedback_btn)


        val settings_layout=findViewById<LinearLayout>(R.id.settings_layout)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        settings_layout.startAnimation(slideUp)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        password_btn.setOnClickListener{
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        feedback_btn.setOnClickListener{
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
        }

        report_btn.setOnClickListener{
            val intent = Intent(this, ReportProblemActivity::class.java)
            startActivity(intent)
        }

        account_btn.setOnClickListener{
            val dialogBuilder = AlertDialog.Builder(this)

            val dialogView = layoutInflater.inflate(R.layout.account_settings_menu, null)
            dialogBuilder.setView(dialogView)

            val profiletype_spinner = dialogView.findViewById<Spinner>(R.id.settingsmenu_profile_type)
            profiletype_spinner.prompt = "Select Profile Type"
            val types = arrayOf("Select Profile Type", "Public", "Private")

            val profiletype_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
            profiletype_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            profiletype_spinner.adapter = profiletype_adapter

            profiletype_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    profiletype_spinner.prompt = types[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    profiletype_spinner.prompt = "Select Profile Type"
                }
            }

            val profilecategory_spinner = dialogView.findViewById<Spinner>(R.id.settingsmenu_profile_category)
            profilecategory_spinner.prompt = "Select Profile Category"
            val categories = arrayOf("Select Profile Category", "Creator", "Educator", "Entertainer","Influencer")

            val profilecategory_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
            profilecategory_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            profilecategory_spinner.adapter = profilecategory_adapter

            profilecategory_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    profilecategory_spinner.prompt = categories[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    profilecategory_spinner.prompt = "Select Profile Category"
                }
            }

            dialogBuilder.setPositiveButton("Save") { dialogInterface, _ ->
                // Handle 'Save' button click
                dialogInterface.dismiss()
                onBackPressed()
                finish()
            }

            dialogBuilder.setNegativeButton("Cancel") { dialogInterface, _ ->
                // Handle 'Cancel' button click
                dialogInterface.dismiss()
            }

            val alertDialog = dialogBuilder.create()
            alertDialog.window?.setBackgroundDrawableResource(R.drawable.account_settings_menu_round)

            alertDialog.show()
        }


    }
}