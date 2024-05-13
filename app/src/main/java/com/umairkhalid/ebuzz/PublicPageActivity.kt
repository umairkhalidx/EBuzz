package com.umairkhalid.ebuzz

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class PublicPageActivity : AppCompatActivity(),ClickListner {

    private var mAuth = FirebaseAuth.getInstance();

    private lateinit var comments_layout : LinearLayout
    private lateinit var send_layout : LinearLayout
    private lateinit var main_layout : ConstraintLayout
    private lateinit var transparentView : View
    private lateinit var click_layout : LinearLayout

    private var current_layout: Int = -1
    private lateinit var comment_backBtnId: ImageButton
    private lateinit var send_backBtnId: ImageButton

    private val PICK_IMAGE_REQUEST = 71
    private var type:Int =0
    private var file_path: Uri? = null
    private lateinit var page_logo : ImageView
    private lateinit var cover_img : ImageView
    lateinit var pageName : String
    var ownerID :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_public_page)

        current_layout = 1
        pageName = intent.getStringExtra("PAGENAME").toString()

        Toast.makeText(this,"Loading...",Toast.LENGTH_LONG).show()

        val back_btn = findViewById<ImageButton>(R.id.publicpage_back_btn)
        val follow_btn =findViewById<Button>(R.id.publicpage_follow_btn)
        val publicPageName = findViewById<TextView>(R.id.publicpage_username_txt)
        page_logo = findViewById<ImageView>(R.id.publicpage_user_img)
        cover_img =findViewById<ImageView>(R.id.publicpage_cover_img)
        val description_txt =findViewById<TextView>(R.id.publicpage_description_txt)
        val leave_btn = findViewById<Button>(R.id.publicpage_leavepage_btn)
        val addpost_btn = findViewById<Button>(R.id.publicpage_addpost_btn)


        main_layout = findViewById(R.id.rootLayout)
        transparentView=findViewById(R.id.transparentView)
        click_layout = findViewById(R.id.click_layout)
        comments_layout = findViewById(R.id.publicpage_comments_layout)
        send_layout = findViewById(R.id.publicpage_send_layout)
        transparentView.visibility=View.GONE
        comments_layout.visibility=View.GONE
        send_layout.visibility=View.GONE
        click_layout.visibility=View.GONE


        back_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

        leave_btn.setOnClickListener{
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Confirmation")
            builder.setMessage("Are you sure you want to leave this page?")

            builder.setPositiveButton("Yes") { dialog, _ ->

                var currentUser = FirebaseAuth.getInstance().currentUser
                val uid = currentUser?.uid.toString()
                val database = FirebaseDatabase.getInstance()
                val usersRef = database.getReference("users")

                usersRef.child(uid).child("MyPages").child(pageName).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this,"Page Unfollowed",Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                        onBackPressed()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        // Handle failure
                        Toast.makeText(this,"Please Try Again",Toast.LENGTH_LONG).show()
                        Log.e("RemoveEntry", "Error removing type entry: ${e.message}")
                    }
            }

            builder.setNegativeButton("No") { dialog, _ ->
                // Handle 'No' button click
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        addpost_btn.setOnClickListener{
            val intent = Intent(this, UploadPostActivity::class.java)
            intent.putExtra("CATEGORY", "PAGE")
            intent.putExtra("PAGENAME", pageName)
            startActivity(intent)
        }

        follow_btn.setOnClickListener{

            var currentUser = FirebaseAuth.getInstance().currentUser
            val uid = currentUser?.uid.toString()

            val database = FirebaseDatabase.getInstance()
            var my_ref = database.getReference("users").child(uid)

            my_ref.child("MyPages").child(pageName).setValue(true)

            Toast.makeText(this, "Page Followed", Toast.LENGTH_LONG).show()

        }


        var database = FirebaseDatabase.getInstance()
        val my_ref = database.getReference("pages")
        var currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        var pic_url:String=""
        var cover_url:String=""

        my_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.child(pageName).child("name").value.toString()
                val desc = dataSnapshot.child(pageName).child("description").value.toString()
                pic_url = dataSnapshot.child(pageName).child("photo").value.toString()
                cover_url = dataSnapshot.child(pageName).child("cover").value.toString()
                ownerID =  dataSnapshot.child(pageName).child("owner").value.toString()

                publicPageName.text = name
                description_txt.text = desc

                if (pic_url.length>10) {
                    Picasso.get().load(pic_url).into(page_logo)
                }
                if (cover_url.length>10 ) {
                    Picasso.get().load(cover_url).into(cover_img)

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Log.d("TAG", "Unable to retrieve Data")
            }
        })



        val curr = mAuth.currentUser
        val userID= curr?.uid.toString()

        page_logo.setOnClickListener {
            if(userID==ownerID){
                type=1
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            }
        }
        cover_img.setOnClickListener{
            if(userID==ownerID){
                type=2
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            }
        }

        var adapter_data_list : ArrayList<recycleview_post_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.publicpage_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            true
        )

        val databaseref = FirebaseDatabase.getInstance()
        val current = mAuth.currentUser
        val id= current?.uid.toString()
        var postRef = databaseref.getReference("pages").child(pageName).child("posts")

        postRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (Snapshot in dataSnapshot.children) {
                    val userID = Snapshot.child("userID").getValue(String::class.java)
                    val postID = Snapshot.child("postID").getValue(String::class.java)
                    val type = Snapshot.child("type").getValue(String::class.java)
                    val text = Snapshot.child("text").getValue(String::class.java)
                    val photo = Snapshot.child("photo").getValue(String::class.java)
                    val video = Snapshot.child("video").getValue(String::class.java)
                    val time = Snapshot.child("time").getValue(String::class.java)
                    val description_txt = Snapshot.child("description").getValue(String::class.java)

                    var postType = -1
                    if(type == "Text"){
                        postType= 0
                    }else if(type == "Photo"){
                        postType= 1
                    }
                    else if(type == "Video"){
                        postType= 2
                    }
                    val databaseUser = FirebaseDatabase.getInstance()
                    val usersRef = databaseUser.getReference("users")

                    usersRef.child(userID.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(Snapshot: DataSnapshot) {
                            if (Snapshot.exists()) {
                                val userName = Snapshot.child("name").getValue(String::class.java)
                                val userPicture = Snapshot.child("picture").getValue(String::class.java)

                                val postData  = recycleview_post_data(userID,postID,userPicture,userName,photo,"",text,description_txt,postType,0)
                                adapter_data_list.add(postData)

                                val adapter = recycleview_post_adapter(adapter_data_list, this@PublicPageActivity)
                                recyclerView.adapter = adapter

                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle error
                            Log.d("TAG", "Error getting user data: ${databaseError.message}")
                        }
                    })
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Log.d("Error", databaseError.message)
                Toast.makeText(this@PublicPageActivity,"Unable to Fetch Data",Toast.LENGTH_LONG).show()
            }
        })



//        val v1  = recycleview_post_data("","","","Pagename","","","","",1,0)
//        val v2  = recycleview_post_data("","","","Pagename","","","","",0,0)
//        val v3  = recycleview_post_data("","","","Pagename","","","","",1,0)
//        val v4  = recycleview_post_data("","","","Pagename","","","","",1,0)
//
//
//        adapter_data_list.add(v1)
//        adapter_data_list.add(v2)
//        adapter_data_list.add(v3)
//        adapter_data_list.add(v4)
//
//
//        // 3- Adapter
//        val adapter = recycleview_post_adapter(adapter_data_list,this)
//        recyclerView.adapter = adapter

    }
    override fun onCLick_fun(position: Int, username: String, operation: Int) {

        if(operation==1){

            current_layout = 2

            send_layout.visibility= View.GONE
            click_layout.visibility= View.VISIBLE
            comments_layout.visibility= View.VISIBLE
            transparentView.visibility = View.VISIBLE
            transparentView.setOnTouchListener { _, _ -> true }

            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
            click_layout.startAnimation(slideUp)

            comments_layout.post{

                comment_backBtnId = comments_layout.findViewById(R.id.comments_back_btn)
                val send_btn = comments_layout.findViewById<ImageButton>(R.id.comments_send_btn_1)
                val message_txt = comments_layout.findViewById<EditText>(R.id.comments_comment_txt)


                comment_backBtnId.setOnClickListener {

                    transparentView.visibility = View.GONE

                    val slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down_layout)
                    slideDownAnimation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {
                            // Animation start event
                        }

                        override fun onAnimationEnd(animation: Animation) {
                            comments_layout.visibility = View.GONE
                            click_layout.visibility = View.GONE
                            send_layout.visibility = View.GONE

                        }

                        override fun onAnimationRepeat(animation: Animation) {
                        }
                    })
                    click_layout.startAnimation(slideDownAnimation)
                }

                var adapter_data_list : ArrayList<recycleview_comment_data> = ArrayList()

                val recyclerView : RecyclerView = findViewById(R.id.comments_recycleView)
                recyclerView.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,
                    false
                )

                val v1  = recycleview_comment_data("","John Doe","",0)
                val v2  = recycleview_comment_data("","Emma Phillips","",0)
                val v3  = recycleview_comment_data("","Jack Watson","",0)


                adapter_data_list.add(v1)
                adapter_data_list.add(v2)
                adapter_data_list.add(v3)


                val adapter = recycleview_comment_adapter(adapter_data_list,this)
                recyclerView.adapter = adapter

            }

        }else{

            current_layout = 3

            send_layout.visibility= View.VISIBLE
            click_layout.visibility= View.VISIBLE
            comments_layout.visibility= View.GONE
            transparentView.visibility = View.VISIBLE
            transparentView.setOnTouchListener { _, _ -> true }

            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
            click_layout.startAnimation(slideUp)

            send_layout.post{

                send_backBtnId= send_layout.findViewById(R.id.send_back_btn)
                val search_btn = send_layout.findViewById<ImageButton>(R.id.send_searchbar_btn)
                val search_txt = send_layout.findViewById<EditText>(R.id.send_searchbar_txt)
                val send_btn = send_layout.findViewById<Button>(R.id.send_send_btn)
                val repost_btn = send_layout.findViewById<Button>(R.id.send_repost_btn)


                send_backBtnId.setOnClickListener {

                    transparentView.visibility = View.GONE

                    val slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down_layout)
                    slideDownAnimation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {
                            // Animation start event
                        }

                        override fun onAnimationEnd(animation: Animation) {
                            comments_layout.visibility = View.GONE
                            click_layout.visibility = View.GONE
                            send_layout.visibility = View.GONE
                        }

                        override fun onAnimationRepeat(animation: Animation) {
                        }
                    })
                    click_layout.startAnimation(slideDownAnimation)
                }

                var adapter_data_list : ArrayList<recycleview_sendpost_data> = ArrayList()

                val recyclerView : RecyclerView = findViewById(R.id.send_recycleView)
                recyclerView.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,
                    false
                )

                val v1  = recycleview_sendpost_data("","User 1","","User 2","","User 3",3)
                val v2  = recycleview_sendpost_data("","User 4","","User 5","","User 6",3)
                val v3  = recycleview_sendpost_data("","User 7","","User 8","","",2)


                adapter_data_list.add(v1)
                adapter_data_list.add(v2)
                adapter_data_list.add(v3)


                val adapter = recycleview_sendpost_adapter(adapter_data_list,this)
                recyclerView.adapter = adapter

            }

        }

    }
    override fun onBackPressed() {

        if (current_layout == 1) {
            this.finish()
            super.onBackPressed()
        } else if (current_layout == 2) {
            current_layout = 1
            comment_backBtnId.performClick()

        } else if (current_layout == 3) {
            current_layout = 1
            send_backBtnId.performClick()
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            file_path = data.data
            // Set the selected image to the profileImageView
            if(type==1){
                Toast.makeText(this,"Uploading...",Toast.LENGTH_LONG).show()

                val my_database = FirebaseDatabase.getInstance()
                var my_ref = my_database.getReference("pages")
                val curr = mAuth.currentUser
                val userId= curr?.uid.toString()

                val imageURL = file_path.toString()
                val page_name = pageName.toString()
                my_ref.child(page_name).child("photo").setValue(imageURL)


                val storageRef = FirebaseStorage.getInstance().reference

                val profileImageRef = storageRef.child("page_images/$userId.jpg")
                val uploadTask = profileImageRef.putFile(file_path!!)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    // Image uploaded successfully, now get the download URL
                    profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Save download URL to Firebase Realtime Database
                        val image_url = uri.toString()
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("pages/$page_name/photo")

                        myRef.setValue(image_url).addOnSuccessListener {
                            Picasso.get().load(file_path).into(page_logo)
                            Toast.makeText(this, "Page picture updated!", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Unable to Upload,Please Try Again", Toast.LENGTH_SHORT).show()
                                Log.d("TAG", "Failed To Upload Profile Image")

                            }
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Unable to Upload,Please Try Again", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Failed To Upload Profile Image")

                }

            }
            else if(type==2){

                Toast.makeText(this,"Uploading...",Toast.LENGTH_LONG).show()

                val my_database = FirebaseDatabase.getInstance()
                var my_ref = my_database.getReference("pages")
                val currUser = mAuth.currentUser
                val userId= currUser?.uid.toString()

                val imageURL = file_path.toString()
                val page_name = pageName.toString()
                my_ref.child(page_name ).child("page").setValue(imageURL)


                val storageRef = FirebaseStorage.getInstance().reference

                val profileImageRef = storageRef.child("page_images/$userId.jpg")
                val uploadTask = profileImageRef.putFile(file_path!!)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    // Image uploaded successfully, now get the download URL
                    profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Save download URL to Firebase Realtime Database
                        val image_url = uri.toString()
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("pages/$page_name/cover")

                        myRef.setValue(image_url).addOnSuccessListener {
                            Picasso.get().load(file_path).into(cover_img)
                            Toast.makeText(this, "Cover updated!", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Unable to Upload,Please Try Again", Toast.LENGTH_SHORT).show()
                                Log.d("TAG", "Failed To Upload Cover Image")

                            }
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Unable to Upload,Please Try Again", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Failed To Upload Cover Image")

                }

            }
        }
    }
}