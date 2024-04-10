package com.umairkhalid.ebuzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class recycleview_search_adapter (val itemslist: ArrayList<recycleview_search_data>)
    : RecyclerView.Adapter<recycleview_search_adapter.search_recycler_viewholder>()
{
    inner class search_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var user_img : ImageView
        lateinit var username : TextView

        init {
            user_img= itemView.findViewById(R.id.item_user_img)
            username= itemView.findViewById(R.id.item_username)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): search_recycler_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_search_item,parent,false)

        return search_recycler_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: search_recycler_viewholder, position: Int) {

//        Glide.with(holder.itemView.context)
//            .load(itemslist[position].img)
//            .into(holder.display_pic)
        holder.username.setText(itemslist[position].username)

    }
}