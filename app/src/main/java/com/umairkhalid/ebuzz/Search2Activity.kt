package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Search2Activity : AppCompatActivity(), ClickListner {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search2)



        val back_btn=findViewById<ImageButton>(R.id.search_back_btn)

        back_btn.setOnClickListener{
//            val intent = Intent(this, HomePageActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()

        }

        val searchText = findViewById<EditText>(R.id.search_searchbar_txt)
        val searchBtn = findViewById<ImageButton>(R.id.search_searchbar_btn)

        val searchtype = intent.getStringExtra("TYPE")
        val database = FirebaseDatabase.getInstance()
        var usersRef = database.getReference("users")

        if(searchtype == "PEOPLE"){
            usersRef = database.getReference("users")

            searchBtn.setOnClickListener{

                var adapter_data_list : ArrayList<recycleview_search_data> = ArrayList()

                val recyclerView : RecyclerView = findViewById(R.id.search_recycleview)
                recyclerView.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                val text = searchText.text.toString().trim()
                if (text.isNotEmpty()){
                    val query = usersRef.orderByChild("name").equalTo(text)
                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {

                                for (userSnapshot in dataSnapshot.children) {
                                    val userId = userSnapshot.key // Get the user ID
                                    val userName = userSnapshot.child("name").getValue(String::class.java)
                                    val userPicture = userSnapshot.child("picture").getValue(String::class.java)
                                    val type = userSnapshot.child("profiletype").getValue(String::class.java)

                                    if (userId != null && userName != null && userPicture != null) {
                                        val data  = recycleview_search_data(userId,userPicture,userName,type,0)
                                        adapter_data_list.add(data)
//
                                        val adapter = recycleview_search2_adapter(adapter_data_list,this@Search2Activity)
                                        recyclerView.adapter = adapter

//                                    val slideUp = AnimationUtils.loadAnimation(this@Search2Activity, R.anim.slide_up_layout)
//                                    recyclerView.startAnimation(slideUp)
                                    }
                                }
                            } else {
                                Toast.makeText(this@Search2Activity,"User Not Found",Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle error
                            Log.d("TAG", "Error searching for user: ${databaseError.message}")
                            Toast.makeText(this@Search2Activity,"Please Try Again",Toast.LENGTH_LONG).show()

                        }
                    })


                }else{
                    Toast.makeText(this,"Please Enter a Username",Toast.LENGTH_LONG).show()
                }

            }
        }


        if(searchtype == "PAGE"){
            usersRef = database.getReference("pages")

            searchBtn.setOnClickListener{

                var adapter_data_list : ArrayList<recycleview_search_data> = ArrayList()

                val recyclerView : RecyclerView = findViewById(R.id.search_recycleview)
                recyclerView.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                val text = searchText.text.toString().trim()
                if (text.isNotEmpty()){
                    val query = usersRef.orderByChild("name").equalTo(text)
                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {

                                for (userSnapshot in dataSnapshot.children) {
                                    val userId = userSnapshot.key // Get the user ID
                                    val pageName = userSnapshot.child("name").getValue(String::class.java)
                                    val pagePicture = userSnapshot.child("photo").getValue(String::class.java)

                                    if (userId != null && pageName != null && pagePicture != null) {
                                        val data  = recycleview_search_data(userId,pagePicture,pageName,"Public",1)
                                        adapter_data_list.add(data)
//
                                        val adapter = recycleview_search2_adapter(adapter_data_list,this@Search2Activity)
                                        recyclerView.adapter = adapter

//                                    val slideUp = AnimationUtils.loadAnimation(this@Search2Activity, R.anim.slide_up_layout)
//                                    recyclerView.startAnimation(slideUp)
                                    }
                                }
                            } else {
                                Toast.makeText(this@Search2Activity,"User Not Found",Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle error
                            Log.d("TAG", "Error searching for user: ${databaseError.message}")
                            Toast.makeText(this@Search2Activity,"Please Try Again",Toast.LENGTH_LONG).show()

                        }
                    })


                }else{
                    Toast.makeText(this,"Please Enter a Username",Toast.LENGTH_LONG).show()
                }

            }
        }




//        val v1  = recycleview_search_data("","John Doe","")
//        val v2  = recycleview_search_data("","Emma Phillips","")
//        val v3  = recycleview_search_data("","John Cooper","")
//
//        adapter_data_list.add(v1)
//        adapter_data_list.add(v2)
//        adapter_data_list.add(v3)
//
//        // 3- Adapter
//        val adapter = recycleview_search2_adapter(adapter_data_list,this)
//        recyclerView.adapter = adapter
    }

    override fun onCLick_fun(position: Int,userid:String,category:Int)
    {
        if (category==0){
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra("USERID", userid)
            startActivity(intent)
        }
        else if (category==1){
            val intent = Intent(this, PublicPageActivity::class.java)
            intent.putExtra("PAGENAME", userid)
            startActivity(intent)
        }

    }

}