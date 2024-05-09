package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val email=findViewById<EditText>(R.id.forgot_email_txt)
        val login_txt=findViewById<TextView>(R.id.forgot_login_txt)
        val back_btn=findViewById<ImageButton>(R.id.forgot_back_btn)
        val send_btn=findViewById<Button>(R.id.forgot_send_btn)
        val forgot_layout=findViewById<LinearLayout>(R.id.forgot_layout)

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        forgot_layout.startAnimation(slideUp)

        val spannableString = SpannableString("Log in")
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        login_txt.text = spannableString


        send_btn.setOnClickListener{
            val email_txt=email.text.toString().trim()
            if (email_txt.isNotEmpty()){

                resetPassword(email.text.toString().trim())
                //No need to explicitly check if email exists in the Authentication just send a Password Reset Email and
                //if email does not exist in the firebase authentication the email will not be sent


//                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email.text.toString().trim())
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            val signInMethods = task.result?.signInMethods
//                            if (signInMethods != null && signInMethods.isNotEmpty()) {
//                                // Email exists, signInMethods will contain the sign-in methods associated with the email
//                                resetPassword(email.text.toString().trim())
//                            } else {
//                                // Email does not exist
//                                Toast.makeText(this,email_txt,Toast.LENGTH_LONG).show()
//
////                                Toast.makeText(this,"Please Enter Your Associated Email",Toast.LENGTH_LONG).show()
//                            }
//                        } else {
//                            // Failed to check email existence
//                            val errorMessage = task.exception?.message ?: "Unknown error occurred"
//                            Log.e("Email Check Failure", "Failed to check email existence: $errorMessage")
//                            Toast.makeText(this,"Please Try Again",Toast.LENGTH_LONG).show()
//                        }
//                    }

            }else{
                Toast.makeText(this,"Please Enter an Email",Toast.LENGTH_LONG).show()
            }

        }


        back_btn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        login_txt.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun resetPassword(userEmail:String){
        FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset email sent successfully
                    Toast.makeText(this,"Reset Email Sent Successfully",Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Failed to send password reset email
                    val errorMessage = task.exception?.message ?: "Unknown error occurred"
                    Toast.makeText(this,"Please Try Again",Toast.LENGTH_LONG).show()
                    Log.e("Password Reset Failure", "Failed to send password reset email: $errorMessage")
                }
            }
     }
}