package com.umairkhalid.ebuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FeedbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val back_btn = findViewById<ImageButton>(R.id.feedback_back_btn)
        val submit_btn = findViewById<Button>(R.id.feedback_submit_btn)
        val email_txt = findViewById<EditText>(R.id.feedback_email_txt)
        val description =findViewById<EditText>(R.id.feedback_description_txt)
        val rating_bar = findViewById<RatingBar>(R.id.feedback_ratingbar)

        val feedback_layout = findViewById<LinearLayout>(R.id.feedback_layout)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        feedback_layout.startAnimation(slideUp)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        submit_btn.setOnClickListener{

            val email = email_txt.text.toString().trim()
            val description_txt=description.text.toString().trim()
            val rating = rating_bar.rating.toString()

            if (description_txt.isNotEmpty() && email.isNotEmpty()){

                val database_C = FirebaseDatabase.getInstance()
                val credentialsRef = database_C.getReference("credentials")

                credentialsRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {

                            val userSnapshot = dataSnapshot.children.first()
                            // Get the userID field from the userSnapshot
                            val userID = userSnapshot.child("userID").getValue(String::class.java)

                            val database = FirebaseDatabase.getInstance()
                            val feedbackRef = database.getReference("feedbacks")

                            // Get current user ID
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            val id = currentUser?.uid.toString()

                            if(userID == id){

                                // Get current date and time
                                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                                val dateTime = "$currentDate $currentTime"


                                val feedbackKey = feedbackRef.push().key.toString()

                                feedbackRef.child(feedbackKey).setValue(null)
                                feedbackRef.child(feedbackKey).child("userID").setValue(id)
                                feedbackRef.child(feedbackKey).child("email").setValue(email)
                                feedbackRef.child(feedbackKey).child("description").setValue(description_txt)
                                feedbackRef.child(feedbackKey).child("feedback").setValue(rating)
                                feedbackRef.child(feedbackKey).child("time").setValue(dateTime)


                                Toast.makeText(this@FeedbackActivity,"Feedback Submitted Successfully", Toast.LENGTH_LONG).show()

                                onBackPressed()
                                finish()
                            }
                            else{
                                Toast.makeText(this@FeedbackActivity,"Please Enter your Email Address", Toast.LENGTH_LONG).show()
                            }



                        } else {
                            // No user found with the specified email address
                            Toast.makeText(this@FeedbackActivity,"Email Address Not Found", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle errors
                        Toast.makeText(this@FeedbackActivity,"Please try Again", Toast.LENGTH_LONG).show()
                        Log.d("Feedback Error", databaseError.message)

                    }
                })


            }else{
                Toast.makeText(this,"Please fill in all fields", Toast.LENGTH_LONG).show()
            }
        }
    }
}