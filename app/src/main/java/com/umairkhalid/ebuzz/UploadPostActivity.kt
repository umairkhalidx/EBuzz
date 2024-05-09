package com.umairkhalid.ebuzz

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UploadPostActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance();
    private lateinit var user_img:ImageView
    private lateinit var username:TextView
    var type = 0

    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE_REQUEST = 2
    private var imageUri: Uri? = null
    var status = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_post)

        val back_btn=findViewById<ImageButton>(R.id.upload_post_back_btn)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        user_img = findViewById<ImageView>(R.id.uploadpost_user_image)
        username = findViewById<TextView>(R.id.uploadpost_user_name)
        val post_btn = findViewById<Button>(R.id.uploadpost_post_btn)
        val post_layout = findViewById<LinearLayout>(R.id.uploadpost_post_cardview)
        val upload_textview = findViewById<EditText>(R.id.uploadpost_text_view)
        val upload_imageview = findViewById<LinearLayout>(R.id.uploadpost_photovideo_layout)
        val uploadGallery =findViewById<Button>(R.id.uploadpost_upload_btn_gallery)
        val uploadCamera =findViewById<Button>(R.id.uploadpost_upload_btn_camera)
        val description = findViewById<EditText>(R.id.uploadpost_description)


        post_layout.visibility=View.GONE
        upload_textview.visibility=View.GONE
        upload_imageview.visibility=View.GONE


        val category_spinner=findViewById<Spinner>(R.id.uploadpost_catergory_spinner)

        category_spinner.prompt = "Click Me"
        val categories = arrayOf("Click Me","Text", "Photo", "Video")

        val category_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        category_spinner.adapter = category_adapter

        category_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                category_spinner.prompt = selectedCategory

                when (selectedCategory) {
                    "Click Me" -> {
                        post_layout.visibility=View.GONE
                        upload_textview.visibility=View.GONE
                        upload_imageview.visibility=View.GONE
                        type = 0
                    }
                    "Text" -> {
                        post_layout.visibility=View.VISIBLE
                        upload_textview.visibility=View.VISIBLE
                        upload_imageview.visibility=View.GONE
                        type = 1


                    }
                    "Photo" -> {
                        post_layout.visibility=View.VISIBLE
                        upload_textview.visibility=View.GONE
                        upload_imageview.visibility=View.VISIBLE
                        type= 2
                        // Button to take a photo
                        uploadCamera.setOnClickListener{
                            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            try {
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                            } catch (e: ActivityNotFoundException) {
                                // Camera app not found
                                Toast.makeText(this@UploadPostActivity, "Camera app not found", Toast.LENGTH_SHORT).show()
                            }
                        }

                        // Button to choose from gallery
                        uploadGallery.setOnClickListener{
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "image/*"
                            startActivityForResult(intent, PICK_IMAGE_REQUEST)
                        }
                    }
                    "Video" -> {
                        //This will be changed to support video

                        post_layout.visibility=View.VISIBLE
                        upload_textview.visibility=View.GONE
                        upload_imageview.visibility=View.VISIBLE
                        type = 3
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                category_spinner.prompt = "Click Me"
            }
        }

        post_btn.setOnClickListener{
            if(type==0){

            }else if (type==1){

                val database = FirebaseDatabase.getInstance()
                val curr = mAuth.currentUser
                val id= curr?.uid.toString()
                var postRef = database.getReference("users").child(id).child("posts")

                // Get current date and time
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                val dateTime = "$currentDate $currentTime"


                val postKey = postRef.push().key.toString()
                val content_txt = upload_textview.text.toString().trim()
                val description_txt = upload_textview.text.toString().trim()

                postRef.child(postKey).setValue(null)
                postRef.child(postKey).child("userID").setValue(id)
                postRef.child(postKey).child("postID").setValue(postKey)
                postRef.child(postKey).child("type").setValue("Text")
                postRef.child(postKey).child("text").setValue(content_txt)
                postRef.child(postKey).child("photo").setValue("")
                postRef.child(postKey).child("video").setValue("")
                postRef.child(postKey).child("time").setValue(dateTime)
                postRef.child(postKey).child("description").setValue(description_txt)



                Toast.makeText(this@UploadPostActivity,"Post Uploaded Successfully", Toast.LENGTH_LONG).show()

                onBackPressed()
                finish()

            }
            else if (type==2){

                if (status == true){

                    Toast.makeText(this,"Uploading Post...",Toast.LENGTH_LONG).show()

                    val database = FirebaseDatabase.getInstance()
                    val curr = mAuth.currentUser
                    val id= curr?.uid.toString()
                    var postRef = database.getReference("users").child(id).child("posts")

                    // Get current date and time
                    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                    val dateTime = "$currentDate $currentTime"


                    val postKey = postRef.push().key.toString()
                    val imageURL = imageUri.toString()
                    val description_txt = upload_textview.text.toString().trim()


                    postRef.child(postKey).setValue(null)
                    postRef.child(postKey).child("userID").setValue(id)
                    postRef.child(postKey).child("postID").setValue(postKey)
                    postRef.child(postKey).child("type").setValue("Photo")
                    postRef.child(postKey).child("text").setValue("")
                    postRef.child(postKey).child("photo").setValue(imageURL)
                    postRef.child(postKey).child("video").setValue("")
                    postRef.child(postKey).child("time").setValue(dateTime)
                    postRef.child(postKey).child("description").setValue(description_txt)



                    val storageRef = FirebaseStorage.getInstance().reference

                    val postImageRef = storageRef.child("post_images/$postKey.jpg")
                    val uploadTask = postImageRef.putFile(imageUri!!)

                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        // Image uploaded successfully, now get the download URL
                        Toast.makeText(this,"Please Wait....",Toast.LENGTH_SHORT).show()

                        postImageRef.downloadUrl.addOnSuccessListener { uri ->
                            // Save download URL to Firebase Realtime Database
                            val image_url = uri.toString()
                            val database = FirebaseDatabase.getInstance()
                            val myRef = database.getReference("users/$id/posts/$postKey/photo")

                            myRef.setValue(image_url).addOnSuccessListener {
                                Toast.makeText(this@UploadPostActivity,"Post Uploaded Successfully", Toast.LENGTH_LONG).show()

                                onBackPressed()
                                finish()

                            }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Unable to Upload,Please Try Again", Toast.LENGTH_SHORT).show()
                                    Log.d("TAG", "Failed To Upload Image")

                                }
                        }
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Unable to Upload,Please Try Again", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "Failed To Upload Image")

                    }

                }else{
                    Toast.makeText(this,"Uploading Image...",Toast.LENGTH_LONG).show()
                }

            }else if (type==3){

            }


        }

        var database = FirebaseDatabase.getInstance()
        val my_ref = database.getReference("users")
        var currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if(userId!=null){

            my_ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pic_url= dataSnapshot.child(userId).child("picture").value.toString()
                    val name= dataSnapshot.child(userId).child("name").value.toString()
                    username.text=name

                    if(pic_url.length>10){
                        Picasso.get().load(pic_url).into(user_img)
                    }

                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Log.d("TAG", "Unable to retrieve Data")
                    Toast.makeText(this@UploadPostActivity,"Unable to get User Image", Toast.LENGTH_LONG).show()

                }
            })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Camera intent result, handle the captured image
            val imageBitmap = data?.extras?.get("data") as? Bitmap

            imageBitmap?.let {
                imageUri = getImageUriFromBitmap(it)
                status = true
            }

        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            // Gallery intent result, handle the selected image
            imageUri = data.data
            status = true

        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "Image Title",
            null
        )
        return Uri.parse(path)
    }

}