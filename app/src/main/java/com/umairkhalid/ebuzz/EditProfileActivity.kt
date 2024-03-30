package com.umairkhalid.ebuzz

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {

    private lateinit var mDateSetListener: DatePickerDialog.OnDateSetListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val name=findViewById<EditText>(R.id.editprofile_name_txt)
        val contact=findViewById<EditText>(R.id.editprofile_contact_txt)
        val country_spinner=findViewById<Spinner>(R.id.editprofile_country_spinner)
        val province_spinner=findViewById<Spinner>(R.id.editprofile_province_spinner)
        val age=findViewById<TextView>(R.id.editprofile_age_txt)
        val upload_btn=findViewById<Button>(R.id.editprofile_upload_btn)
        val editprofile_layout=findViewById<LinearLayout>(R.id.editprofile_layout)
        val back_btn=findViewById<ImageButton>(R.id.editprofile_back_btn)

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        editprofile_layout.startAnimation(slideUp)


        province_spinner.prompt = "Select Province"
        val provinces = arrayOf("Select Province","Punjab", "Sindh", "Balochistan",
            "KPK","Kashmir","Gilgit")

        country_spinner.prompt = "Select Country"
        val countries = arrayOf("Select Country","Pakistan")

        val province_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        province_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        province_spinner.adapter = province_adapter

        province_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                province_spinner.prompt = provinces[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                province_spinner.prompt = "Select Province"
            }
        }

        val country_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        country_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        country_spinner.adapter = country_adapter

        country_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                country_spinner.prompt = countries[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                country_spinner.prompt = "Select Country"
            }
        }

        upload_btn.setOnClickListener{
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish()
        }

        back_btn.setOnClickListener{
//            val intent = Intent(this, HomePageActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

        age.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(
                this@EditProfileActivity,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,
                month,
                day
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        mDateSetListener = DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
            val monthFormatted = month + 1
            val date = "$monthFormatted/$dayOfMonth/$year"
            age.setText(date)
            age.setTextColor(Color.BLACK)
        }


    }
}