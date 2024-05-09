package com.umairkhalid.ebuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportProblemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_problem)

        val back_btn=findViewById<ImageButton>(R.id.reportproblem_back_btn)
        val description = findViewById<EditText>(R.id.reportproblem_description_txt)
        val report_btn = findViewById<Button>(R.id.reportproblem_report_btn)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        val problem_spinner=findViewById<Spinner>(R.id.reportproblem_problem_spinner)
        problem_spinner.prompt = "Select Problem"
        val problems = arrayOf("Select Problem","General Usage", "Specific Group", "Specific User",
            "Specific Page","Personal Account","Other")

        val problem_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, problems)
        problem_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        problem_spinner.adapter = problem_adapter

        problem_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                problem_spinner.prompt = problems[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                problem_spinner.prompt = "Select Problem"
            }
        }


        report_btn.setOnClickListener{
            val description_txt=description.text.toString().trim()
            val problem_txt= problem_spinner.selectedItem.toString().trim()

            if (description_txt.isNotEmpty() && problem_txt!="Select Problem"){
                val database = FirebaseDatabase.getInstance()
                val reportsRef = database.getReference("reports")

                // Get current user ID
                val currentUser = FirebaseAuth.getInstance().currentUser
                val id = currentUser?.uid.toString()

                // Get current date and time
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                val dateTime = "$currentDate $currentTime"


                val reportKey = reportsRef.push().key.toString()

                reportsRef.child(reportKey).setValue(null)
                reportsRef.child(reportKey).child("userID").setValue(id)
                reportsRef.child(reportKey).child("problem").setValue(problem_txt)
                reportsRef.child(reportKey).child("description").setValue(description_txt)
                reportsRef.child(reportKey).child("time").setValue(dateTime)


                Toast.makeText(this@ReportProblemActivity,"Report Sent Successfully",Toast.LENGTH_LONG).show()
                onBackPressed()
                finish()


            }else{
                Toast.makeText(this,"Please fill in all fields", Toast.LENGTH_LONG).show()
            }

        }

        val reportproblem_layout=findViewById<LinearLayout>(R.id.reportproblem_layout)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        reportproblem_layout.startAnimation(slideUp)

    }
}