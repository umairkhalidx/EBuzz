package com.umairkhalid.ebuzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class recycleview_comment_adapter (val itemslist: ArrayList<recycleview_comment_data>, private val listener: ClickListner)
    : RecyclerView.Adapter<recycleview_comment_adapter.comment_recycler_viewholder>()
{
    inner class comment_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var user_img : ImageView
        lateinit var username : TextView
        lateinit var usercomment : TextView
        lateinit var heart_btn : ImageButton

        init {
            user_img= itemView.findViewById(R.id.item_userimage)
            username = itemView.findViewById(R.id.item_username)
            usercomment = itemView.findViewById(R.id.item_usercomment)
            heart_btn = itemView.findViewById(R.id.item_heart_btn)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): comment_recycler_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_comment_item,parent,false)

        return comment_recycler_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: comment_recycler_viewholder, position: Int) {

//        Glide.with(holder.itemView.context)
//            .load(itemslist[position].img)
//            .into(holder.display_pic)
        holder.username.setText(itemslist[position].username)
        holder.usercomment.setText(itemslist[position].comment)

    }
}