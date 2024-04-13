package com.umairkhalid.ebuzz

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView

class recycleview_sendpost_adapter (val itemslist: ArrayList<recycleview_sendpost_data>, private val listener: ClickListner)
    : RecyclerView.Adapter<recycleview_sendpost_adapter.sendpost_recycler_viewholder>()
{
    inner class sendpost_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var sendpost_layout_1 : LinearLayout
        lateinit var sendpost_layout_2 : LinearLayout
        lateinit var sendpost_layout_3 : LinearLayout
        lateinit var user_img_1 : ImageView
        lateinit var username_1 : TextView
        lateinit var user_img_2 : ImageView
        lateinit var username_2 : TextView
        lateinit var user_img_3 : ImageView
        lateinit var username_3 : TextView
        var index : Int = 0

        lateinit var cardview_1 : CardView
        lateinit var cardview_2 : CardView
        lateinit var cardview_3 : CardView

        init {
            sendpost_layout_1 = itemView.findViewById(R.id.sendpost_layout_1)
            sendpost_layout_2 = itemView.findViewById(R.id.sendpost_layout_2)
            sendpost_layout_3 = itemView.findViewById(R.id.sendpost_layout_3)
//            groupLayout1.isClickable = false
//            groupLayout1.isFocusable = false
            user_img_1= itemView.findViewById(R.id.sendpost_userimage_1)
            username_1= itemView.findViewById(R.id.sendpost_username_1)
            user_img_2= itemView.findViewById(R.id.sendpost_userimage_2)
            username_2= itemView.findViewById(R.id.sendpost_username_2)
            user_img_3= itemView.findViewById(R.id.sendpost_userimage_3)
            username_3= itemView.findViewById(R.id.sendpost_username_3)

            cardview_1 = itemView.findViewById(R.id.sendpost_userimage_1_cardview)
            cardview_2 = itemView.findViewById(R.id.sendpost_userimage_2_cardview)
            cardview_3 = itemView.findViewById(R.id.sendpost_userimage_3_cardview)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sendpost_recycler_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_sendpost_item,parent,false)

        return sendpost_recycler_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: sendpost_recycler_viewholder, position: Int) {

//        Glide.with(holder.itemView.context)
//            .load(itemslist[position].img)
//            .into(holder.display_pic)
        holder.index=itemslist[position].count


        if(holder.index==1) {
            holder.username_1.setText(itemslist[position].username_1)
            holder.sendpost_layout_2.visibility= View.INVISIBLE
            holder.sendpost_layout_3.visibility= View.INVISIBLE

            //Maybe we would need to create a new click listner as can not use the same one as homepage
            //or maybe make an abstract function in click listner(this seems to be a better approach)

            holder.sendpost_layout_1.setOnClickListener{
                val currentTint = holder.cardview_1.backgroundTintList?.defaultColor ?: Color.TRANSPARENT
                val newTint = Color.parseColor("#FFC000")

                if (currentTint != newTint) {
                    holder.cardview_1.backgroundTintList = ColorStateList.valueOf(newTint)
                }
                if (currentTint != Color.WHITE) {
                    holder.cardview_1.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                }
//                val whiteColor = ContextCompat.getColor(context, android.R.color.white)
//                val backgroundDrawable = holder.cardview_1.background
//                DrawableCompat.setTint(backgroundDrawable, whiteColor)
//                holder.cardview_1.background = backgroundDrawable
                listener.onCLick_fun(position,"",0)
            }

        }
        else if(holder.index==2){

            holder.username_1.setText(itemslist[position].username_1)
            holder.username_2.setText(itemslist[position].username_2)
            holder.sendpost_layout_3.visibility= View.INVISIBLE

            //Maybe we would need to create a new click listner as can not use the same one as homepage
            //or maybe make an abstract function in click listner(this seems to be a better approach)

            holder.sendpost_layout_1.setOnClickListener{
                val currentTint = holder.cardview_1.backgroundTintList?.defaultColor ?: Color.TRANSPARENT
                val newTint = Color.parseColor("#FFC000")

                if (currentTint != newTint) {
                    holder.cardview_1.backgroundTintList = ColorStateList.valueOf(newTint)
                }
                if (currentTint != Color.WHITE) {
                    holder.cardview_1.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                }//                listener.onCLick_fun(position,"",0)
            }

            holder.sendpost_layout_2.setOnClickListener{
                val currentTint = holder.cardview_2.backgroundTintList?.defaultColor ?: Color.TRANSPARENT
                val newTint = Color.parseColor("#FFC000")

                if (currentTint != newTint) {
                    holder.cardview_2.backgroundTintList = ColorStateList.valueOf(newTint)
                }
                if (currentTint != Color.WHITE) {
                    holder.cardview_2.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                }//                listener.onCLick_fun(position,"",0)
            }

        }
        else{

            holder.username_1.setText(itemslist[position].username_1)
            holder.username_2.setText(itemslist[position].username_2)
            holder.username_3.setText(itemslist[position].username_3)

        //Maybe we would need to create a new click listner as can not use the same one as homepage
        //or maybe make an abstract function in click listner(this seems to be a better approach)

            holder.sendpost_layout_1.setOnClickListener{

                val currentTint = holder.cardview_1.backgroundTintList?.defaultColor ?: Color.TRANSPARENT
                val newTint = Color.parseColor("#FFC000")

                if (currentTint != newTint) {
                    holder.cardview_1.backgroundTintList = ColorStateList.valueOf(newTint)
                }
                if (currentTint != Color.WHITE) {
                    holder.cardview_1.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                }

//                val whiteColor = ContextCompat.getColor(context, android.R.color.white)
//                val backgroundDrawable = holder.cardview_1.background
//                DrawableCompat.setTint(backgroundDrawable, whiteColor)
//                holder.cardview_1.background = backgroundDrawable

            //                listener.onCLick_fun(position,"",0)

            }

            holder.sendpost_layout_2.setOnClickListener{
                val currentTint = holder.cardview_2.backgroundTintList?.defaultColor ?: Color.TRANSPARENT
                val newTint = Color.parseColor("#FFC000")

                if (currentTint != newTint) {
                    holder.cardview_2.backgroundTintList = ColorStateList.valueOf(newTint)
                }
                if (currentTint != Color.WHITE) {
                    holder.cardview_2.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                }//                listener.onCLick_fun(position,"",0)
            }

            holder.sendpost_layout_3.setOnClickListener{
                val currentTint = holder.cardview_3.backgroundTintList?.defaultColor ?: Color.TRANSPARENT
                val newTint = Color.parseColor("#FFC000")

                if (currentTint != newTint) {
                    holder.cardview_3.backgroundTintList = ColorStateList.valueOf(newTint)
                }
                if (currentTint != Color.WHITE) {
                    holder.cardview_3.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                }//                listener.onCLick_fun(position,"",0)
            }

        }

    }
}