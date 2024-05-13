package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class PagesActivity : AppCompatActivity() , ClickListner{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pages)
        val back_btn = findViewById<ImageButton>(R.id.pages_back_btn)
        val search_btn = findViewById<ImageButton>(R.id.pages_searchbar_btn)
        val search_txt = findViewById<EditText>(R.id.pages_searchbar_txt)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }


        var adapter_data_list : ArrayList<recycleview_pages_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.pages_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            true
        )


        var currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid.toString()

        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")
        val pagesRef = database.getReference("pages")

        usersRef.child(userId).child("MyPages").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pageList = mutableListOf<String>()

                for (pageSnapshot in dataSnapshot.children) {
                    val pageName = pageSnapshot.key.toString()
                    if (pageName != null) {
                        pageList.add(pageName)
                    }
                }


                for (i in 0 until pageList.size step 2) {
                    val pageName1 = pageList[i]
                    val pageName2 = if (i + 1 < pageList.size) pageList[i + 1] else ""

                    // Retrieve data for the first page
                    pagesRef.child(pageName1).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(pageDataSnapshot1: DataSnapshot) {
                            val pagename1 = pageDataSnapshot1.child("name").getValue(String::class.java).toString()
                            val pageimage1 = pageDataSnapshot1.child("photo").getValue(String::class.java).toString()

                            // Retrieve data for the second page
                            if (pageName2.isNotEmpty()) {
                                pagesRef.child(pageName2).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(pageDataSnapshot2: DataSnapshot) {
                                        val pagename2 = pageDataSnapshot2.child("name").getValue(String::class.java).toString()
                                        val pageimage2 = pageDataSnapshot2.child("photo").getValue(String::class.java).toString()

                                        val data = recycleview_pages_data(pageimage1, pagename1, pageimage2, pagename2, 2)
                                        adapter_data_list.add(data)
                                        val adapter = recycleview_pages_adapter(adapter_data_list,this@PagesActivity)
                                        recyclerView.adapter = adapter

                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Handle error
                                        Log.e("PageData", "Error getting data for page $pageName2: ${databaseError.message}")
                                        Toast.makeText(this@PagesActivity,"Unable to Fetch Data",Toast.LENGTH_LONG).show()
                                    }
                                })
                            } else {
                                // Only one page in the pair, create a recycleview_pages_data object for it
                                val data = recycleview_pages_data(pageimage1, pagename1, "", "", 1)
                                adapter_data_list.add(data)
                                val adapter = recycleview_pages_adapter(adapter_data_list,this@PagesActivity)
                                recyclerView.adapter = adapter
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle error
                            Log.e("PageData", "Error getting data for page $pageName1: ${databaseError.message}")
                            Toast.makeText(this@PagesActivity,"Unable to Fetch Data",Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Log.e("PageList", "Error getting page list for user $userId: ${databaseError.message}")
                Toast.makeText(this@PagesActivity,"Unable to Fetch Data",Toast.LENGTH_LONG).show()
            }
        })

//        val v1  = recycleview_pages_data("","Pages 1","","Pages 2",2)
//        val v2  = recycleview_pages_data("","Pages 3","","Pages 4",2)
//        val v3  = recycleview_pages_data("","Pages 5","","Pages 6",2)
//        val v4  = recycleview_pages_data("","Pages 7","","Pages 8",2)
//        val v5  = recycleview_pages_data("","Pages 9","","",1)
//
//
//        adapter_data_list.add(v1)
//        adapter_data_list.add(v2)
//        adapter_data_list.add(v3)
//        adapter_data_list.add(v4)
//        adapter_data_list.add(v5)
//
//
//
//        val adapter = recycleview_pages_adapter(adapter_data_list,this)
//        recyclerView.adapter = adapter
//
//        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
//        recyclerView.startAnimation(slideUp)


    }

    override fun onCLick_fun(position: Int,pagename:String,operation:Int)
    {
        val intent = Intent(this, PublicPageActivity::class.java)
        intent.putExtra("PAGENAME", pagename)
        startActivity(intent)

    }
}