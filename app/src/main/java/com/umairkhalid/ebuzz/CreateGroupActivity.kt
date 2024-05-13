package com.umairkhalid.ebuzz

import android.content.Intent
import android.net.Uri
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
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class CreateGroupActivity : AppCompatActivity() {

    private var profilePictureUri: Uri? = null
    private lateinit var selected_group_type: String

    private lateinit var back_btn: ImageButton
    private lateinit var name_txt: EditText
    private lateinit var description: EditText
    private lateinit var upload_btn: Button
    private lateinit var create_btn: Button
    private lateinit var group_type_spinner: Spinner

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        back_btn = findViewById<ImageButton>(R.id.creategroup_back_btn)
        name_txt = findViewById<EditText>(R.id.creategroup_groupname_txt)
        description = findViewById<EditText>(R.id.creategroup_description_txt)
        upload_btn = findViewById<Button>(R.id.creategroup_upload_photo_btn)
        create_btn =findViewById<Button>(R.id.creategroup_create_btn)
        group_type_spinner = findViewById<Spinner>(R.id.creategroup_grouptype_spinner)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        val creategroup_layout=findViewById<LinearLayout>(R.id.creategroup_layout)

        group_type_spinner.prompt = "Select Group Type"
        val group_types = arrayOf("Select Group Type","Educational", "Business", "Motivational",
            "Entertainment","Other")

        val group_type_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, group_types)
        group_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        group_type_spinner.adapter = group_type_adapter

        group_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                group_type_spinner.prompt = group_types[position]
                selected_group_type = group_types[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                group_type_spinner.prompt = "Select Group Type"
                selected_group_type = ""
            }
        }

        // ////////////////////////////////////////// //
        //   SELECTING PHOTO FOR UPLOAD AS COVER      //
        // ////////////////////////////////////////// //
        upload_btn.setOnClickListener {
            openGalleryForImage()
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        create_btn.setOnClickListener {
            upload_btn.isEnabled= false
            saveGroup()
        }
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                profilePictureUri = uri
                // You can display the selected image here if needed
            }
        }

    private fun openGalleryForImage() {
        galleryLauncher.launch("image/*")
    }

    fun saveGroup(){
        val gname = name_txt.text.toString().trim()
        if(gname.isEmpty()){
            name_txt.error = "Kindly Enter Group Name"
            return
        }
        if (profilePictureUri == null) {
            // Handle case when user didn't select a profile picture
            Log.d("Profile Image error!", "Image not selected!")
            return
        }
        if(selected_group_type.isEmpty()){
            Log.d("Group Type Error", "kindly Select Group Type.")
            return
        }

        val gDescription = description.text.toString()

        // ////////////////////////////////////// //
        //       Starting Storage in DB           //
        // ////////////////////////////////////// //

        val userId = auth.currentUser?.uid ?: return
        val groupImageRef = storage.reference.child("group_images/${UUID.randomUUID()}")
        val groupNodeRef = database.reference.child("groups")

        groupImageRef.putFile(profilePictureUri!!)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                groupImageRef.downloadUrl
            }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    // Generate a new unique key using push()
                    val newGroupRef = groupNodeRef.push()
                    // Get the unique key generated by push() and store it somewhere else
                    val groupId = newGroupRef.key.toString()
                    val group = Group_Data(userId, gname, selected_group_type, gDescription, downloadUri.toString(), groupId)

                    // Save the data at the generated key
                    newGroupRef.setValue(group)
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                // saving the groupID to owner user node as well
                                saveGroupIDInUserNode(groupId, userId)

                                // Profile saved successfully, navigate to the next activity
                                Log.d("Group Creation", "Group Successfully Created.")
                                onBackPressed()
                                finish()
                            } else {
                                // Handle error while saving profile to database
                                Log.e("Group Creation failed", "Failed to save Group to database", dbTask.exception)
                            }
                        }
                } else {
                    // Handle error while uploading profile picture
                    Log.e("Group Image Failure", "Failed to upload group picture", task.exception)
                }
            }

    }

    fun saveGroupIDInUserNode(groupID:String, userId:String){
        val userNodeRef = database.reference.child("users").child(userId).child("groups")
        userNodeRef.setValue(groupID)
            .addOnCompleteListener { dbTask ->
                if (dbTask.isSuccessful) {
                    // Profile saved successfully, navigate to the next activity
                    Log.d("Log Record Saving Data", "Group Successfully Saved to Owner Users Node.")
                } else {
                    // Handle error while saving profile to database
                    Log.e("Log Record Failure Alert", "Failed to save Group to Owner Node", dbTask.exception)
                }
            }
    }
}