package com.umairkhalid.ebuzz

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance();

    private lateinit var user_img :ImageView


    private lateinit var mDateSetListener: DatePickerDialog.OnDateSetListener
    var user = UserData("", "", "", "", "", "", "", "","","")
    var check = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val name = findViewById<EditText>(R.id.editprofile_name_txt)
        user_img = findViewById<ImageView>(R.id.editprofile_user_img)
        val contact = findViewById<EditText>(R.id.editprofile_contact_txt)
        val country_spinner = findViewById<Spinner>(R.id.editprofile_country_spinner)
        val province_spinner = findViewById<Spinner>(R.id.editprofile_province_spinner)
        val age = findViewById<TextView>(R.id.editprofile_age_txt)
        val upload_btn = findViewById<Button>(R.id.editprofile_upload_btn)
        val editprofile_layout = findViewById<LinearLayout>(R.id.editprofile_layout)
        val back_btn = findViewById<ImageButton>(R.id.editprofile_back_btn)


        var database = FirebaseDatabase.getInstance()
        val my_ref = database.getReference("users")
        var currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if(userId!=null){

            my_ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pic_url= dataSnapshot.child(userId).child("picture").value.toString()

                    if(pic_url.length>10){
                        Picasso.get().load(pic_url).into(user_img)
                    }

                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Log.d("TAG", "Unable to retrieve Data")
                    Toast.makeText(this@EditProfileActivity,"Unable to get User Image",Toast.LENGTH_LONG).show()

                }
            })
        }


        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        editprofile_layout.startAnimation(slideUp)


        province_spinner.prompt = "Select Province"
        val provinces = arrayOf(
            "Select Province", "Punjab", "Sindh", "Balochistan",
            "KPK", "Kashmir", "Gilgit"
        )

        country_spinner.prompt = "Select Country"
        val countries = arrayOf("Select Country", "Pakistan")

        val province_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        province_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        province_spinner.adapter = province_adapter

        province_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
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
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                country_spinner.prompt = countries[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                country_spinner.prompt = "Select Country"
            }
        }


        check = false
        fetchUserData(){
            name.setText(user.name)
            contact.setText(user.contact)
            country_spinner.prompt = user.country
            province_spinner.prompt = user.province
            age.setText(user.age)
            check = true
        }


        upload_btn.setOnClickListener {
            if (check == true) {

                val name_txt = name.text.toString().trim()
                val contact_txt = contact.text.toString().trim()
                val country_txt = country_spinner.selectedItem.toString().trim()
                val province_txt = province_spinner.selectedItem.toString().trim()
                val age_txt = age.text.toString()
                val age_str = R.string.enter_your_age.toString()


                if (name_txt.isNotEmpty() && contact_txt.isNotEmpty() && country_txt.isNotEmpty() && province_txt.isNotEmpty() && age_txt != age_str) {
                    if (contact_txt.length.toInt() != 11 || !contact_txt.matches("^[0-9]+\$".toRegex())) {
                        Toast.makeText(this, "Invalid Contact Info", Toast.LENGTH_LONG).show()
                    } else if (country_txt == "Select Country") {
                        Toast.makeText(this, "Invalid Country", Toast.LENGTH_LONG).show()
                    } else if (province_txt == "Select Province") {
                        Toast.makeText(this, "Invalid Province", Toast.LENGTH_LONG).show()
                    } else {

                        val curr = mAuth.currentUser
                        val uid = curr?.uid.toString()
                        val database = FirebaseDatabase.getInstance()
                        var my_ref = database.getReference("users")



                        my_ref.child(uid).child("name").setValue(name_txt)
                        my_ref.child(uid).child("contact").setValue(contact_txt)
                        my_ref.child(uid).child("country").setValue(country_txt)
                        my_ref.child(uid).child("province").setValue(province_txt)
                        my_ref.child(uid).child("age").setValue(age_txt)


                        Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_LONG).show()
                        onBackPressed()
                        finish()
                    }

                } else {
                    Toast.makeText(this,"Kindly fill all Fields",Toast.LENGTH_LONG).show()

                }
            } else {
                Toast.makeText(this, "Fetching User Data", Toast.LENGTH_LONG).show()
            }

        }

        back_btn.setOnClickListener {
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

        mDateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val monthFormatted = month + 1
                val date = "$monthFormatted/$dayOfMonth/$year"
                age.setText(date)
                age.setTextColor(Color.BLACK)
            }


    }

    private fun fetchUserData(callback:()->Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val database = FirebaseDatabase.getInstance()
            val userRef = database.getReference("users").child(uid)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        user.userID = uid.toString()
                        user.name = snapshot.child("name").value.toString()
                        user.email = snapshot.child("email").value.toString()
                        user.contact = snapshot.child("contact").value.toString()
                        user.country = snapshot.child("country").value.toString()
                        user.province = snapshot.child("province").value.toString()
                        user.age = snapshot.child("age").value.toString()
                        user.password = ""
                        user.profileType = snapshot.child("profiletype").value.toString()
                        user.profileCategory = snapshot.child("category").value.toString()
                        callback()

                    } else {
                        // No data found for the user
                        Toast.makeText(this@EditProfileActivity, "No Data Found", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Toast.makeText(
                        this@EditProfileActivity,
                        "Unable to Fetch User Data",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            // User is not logged in
            Toast.makeText(this, "User Looged Out", Toast.LENGTH_LONG).show()
        }
    }
}