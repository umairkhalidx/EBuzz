package com.umairkhalid.ebuzz

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import java.util.Calendar

class SignupActivity : AppCompatActivity() {

    private lateinit var mDateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val layout1=findViewById<LinearLayout>(R.id.signup_layout1)
        val layout2=findViewById<LinearLayout>(R.id.signup_layout2)
        val layout3=findViewById<LinearLayout>(R.id.signup_layout3)
        layout2.visibility = View.GONE
        layout3.visibility = View.GONE

        val animation=findViewById<LottieAnimationView>(R.id.signup_animation)
        animation.playAnimation()

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        layout1.startAnimation(slideUp)

        //Layout 1 Variables
        val next_btn=findViewById<Button>(R.id.signup_layout1_nextbtn)
        val name=findViewById<EditText>(R.id.signup_name_txt)
        val email=findViewById<EditText>(R.id.signup_email_txt)
        val password=findViewById<EditText>(R.id.signup_password_txt)
        val confirmPass=findViewById<EditText>(R.id.signup_confirm_pass_txt)
        val login_txt_1=findViewById<TextView>(R.id.signup_login_txt_1)

        val spannableString = SpannableString("Log in")
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        login_txt_1.text = spannableString


        next_btn.setOnClickListener{
            layout1.visibility = View.GONE
            layout2.visibility = View.VISIBLE
            layout3.visibility = View.VISIBLE
            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
            layout2.startAnimation(slideUp)
        }

        login_txt_1.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Layout 2 Variables

        val signup_btn=findViewById<Button>(R.id.signup_layout1_signupbtn)
        val contact=findViewById<EditText>(R.id.signup_contact_txt)
        val country_spinner=findViewById<Spinner>(R.id.signup_country_spinner)
        val province_spinner=findViewById<Spinner>(R.id.signup_province_spinner)
        val age=findViewById<TextView>(R.id.signup_age_txt)
        val login_txt_2=findViewById<TextView>(R.id.signup_login_txt_2)

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

        val spannableString_2 = SpannableString("Log in")
        spannableString_2.setSpan(UnderlineSpan(), 0, spannableString_2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        login_txt_2.text = spannableString_2


        signup_btn.setOnClickListener{
            val intent = Intent(this, OTPActivity::class.java)
            startActivity(intent)
            finish()
        }

        login_txt_2.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        age.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(
                this@SignupActivity,
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