package com.umairkhalid.ebuzz

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChangePasswordActivity : AppCompatActivity() {

    private var  mAuth = FirebaseAuth.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val back_btn =findViewById<ImageButton>(R.id.changepassword_back_btn)
        val email = findViewById<EditText>(R.id.changepassword_email_txt)
        val oldpass = findViewById<EditText>(R.id.changepassword_oldpassword_txt)
//        val newpass = findViewById<EditText>(R.id.changepassword_newpassword_txt)
//        val confirm_pass = findViewById<EditText>(R.id.changepassword_confirmpass_txt)
        val change_btn = findViewById<Button>(R.id.changepassword_change_btn)

        val changepassword_layout=findViewById<LinearLayout>(R.id.changepassword_layout)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        changepassword_layout.startAnimation(slideUp)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        change_btn.setOnClickListener{

            val curr = mAuth.currentUser
            val id= curr?.uid.toString()
            val email_txt=email.text.toString().trim()
            val new_email = email_txt.replace(".", ",")
            val oldpass_txt = oldpass.text.toString().trim()


            if (email_txt.isNotEmpty() && oldpass_txt.isNotEmpty() ){

                if(oldpass_txt.length < 8 ){
                    Toast.makeText(this,"Invalid Password", Toast.LENGTH_LONG).show()
                }
                else{
                    val database = FirebaseDatabase.getInstance()
                    val credentialsRef = database.getReference("credentials").child(new_email)

                    credentialsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            if (snapshot.exists()) {

                                val oldemail = snapshot.child("email").getValue(String::class.java)
                                val oldpassword = snapshot.child("password").getValue(String::class.java)
                                val userID = snapshot.child("userID").getValue(String::class.java)

                                if(oldemail==email_txt && oldpassword==oldpass_txt && userID==id){
                                    val builder = AlertDialog.Builder(this@ChangePasswordActivity)

                                    builder.setTitle("Confirmation")
                                    builder.setMessage("Are you sure you want to change your password?")

                                    builder.setPositiveButton("Yes") { dialog, _ ->
                                        resetPassword(email_txt)
                                        dialog.dismiss()
                                    }

                                    builder.setNegativeButton("No") { dialog, _ ->
                                        // Handle 'No' button click
                                        dialog.dismiss()
                                    }

                                    val dialog = builder.create()
                                    dialog.show()
                                }
                                else{
                                    Toast.makeText(this@ChangePasswordActivity,"Incorrect Credentials",Toast.LENGTH_LONG).show()
                                }

                            } else {
                                // No data found for the user
                                Toast.makeText(this@ChangePasswordActivity, "Incorrect Credentials", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@ChangePasswordActivity, "Unable to Fetch User Data", Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }
            else{
                Toast.makeText(this,"Please fill in all fields", Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun resetPassword(userEmail:String){
        FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset email sent successfully
                    Toast.makeText(this,"Reset Email Sent Successfully",Toast.LENGTH_LONG).show()
                    onBackPressed()
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