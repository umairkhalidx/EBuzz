package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

class OTPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)

        val back_btn = findViewById<ImageButton>(R.id.otp_back_btn)
        val confirm_btn =findViewById<Button>(R.id.otp_confirm_btn)
        val sendagain_btn = findViewById<TextView>(R.id.otp_sendagain_btn)

        val opt_code_layout = findViewById<LinearLayout>(R.id.otp_code_layout)
        val code_num_1 = findViewById<EditText>(R.id.otp_code_num_1)
        val code_num_2 = findViewById<EditText>(R.id.otp_code_num_2)
        val code_num_3 = findViewById<EditText>(R.id.otp_code_num_3)
        val code_num_4 = findViewById<EditText>(R.id.otp_code_num_4)


        val spannableString = SpannableString("Send code again")
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sendagain_btn.text = spannableString


        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        confirm_btn.setOnClickListener{
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}