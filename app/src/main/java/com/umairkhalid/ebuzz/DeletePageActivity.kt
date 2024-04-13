package com.umairkhalid.ebuzz

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout

class DeletePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_page)

        val back_btn =findViewById<ImageButton>(R.id.deletepage_back_btn)
        val email_txt = findViewById<EditText>(R.id.deletepage_email_txt)
        val password_txt = findViewById<EditText>(R.id.deletepage_password_txt)
        val delete_btn = findViewById<Button>(R.id.deletepage_delete_btn)

        val changepassword_layout=findViewById<LinearLayout>(R.id.deletepage_layout)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        changepassword_layout.startAnimation(slideUp)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        delete_btn.setOnClickListener{
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Confirmation")
            builder.setMessage("After deleting you will not be able to recover your data?")

            builder.setPositiveButton("Continue") { dialog, _ ->

                dialog.dismiss()
                val builder2 = AlertDialog.Builder(this)
                builder2.setTitle("Confirmation")
                builder2.setMessage("Are you sure you want to delete your page?")

                builder2.setPositiveButton("Yes") { dialog2, _ ->
                    // Handle 'Yes' button click
                    dialog2.dismiss()
                    onBackPressed()
                    finish()
                }

                builder2.setNegativeButton("No") { dialog2, _ ->
                    // Handle 'No' button click
                    dialog2.dismiss()
                }

                val dialog2 = builder2.create()
                dialog2.show()

            }

            builder.setNegativeButton("Back") { dialog, _ ->
                // Handle 'No' button click
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
    }
}