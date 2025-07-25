package com.umairkhalid.ebuzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class recycleview_friendslist_adapter(val itemslist: ArrayList<recycleview_friendslist_data>,private val listener: ClickListner)
    : RecyclerView.Adapter<recycleview_friendslist_adapter.friendslist_recycler_viewholder>()
{
    inner class friendslist_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var user_img : ImageView
        lateinit var username : TextView

        val viewmore_btn = itemView.findViewById<Button>(R.id.friendslist_viewmore_btn)

        init {
            user_img= itemView.findViewById(R.id.item_user_img)
            username= itemView.findViewById(R.id.item_username)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): friendslist_recycler_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_friendslist_item,parent,false)

        return friendslist_recycler_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: friendslist_recycler_viewholder, position: Int) {

//        Glide.with(holder.itemView.context)
//            .load(itemslist[position].img)
//            .into(holder.display_pic)
        try {
            Picasso.get().load(itemslist[position].img).into(holder.user_img)
        } catch (e: Exception) {

        }

        holder.username.setText(itemslist[position].username)

        holder.viewmore_btn.setOnClickListener{
            listener.onCLick_fun(position,itemslist[position].id.toString(),0)
        }

    }
}