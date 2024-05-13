package com.umairkhalid.ebuzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class recycleview_pages_adapter (val itemslist: ArrayList<recycleview_pages_data>,private val listener: ClickListner)
    : RecyclerView.Adapter<recycleview_pages_adapter.pages_recycler_viewholder>()
{
    inner class pages_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var pages_layout_1 : LinearLayout
        lateinit var pages_layout_2 : LinearLayout
        lateinit var pages_img_1 : ImageView
        lateinit var pagesname_1 : TextView
        lateinit var pages_img_2 : ImageView
        lateinit var pagesname_2 : TextView
        var index : Int = 0

        val viewmore_btn_1 : Button = itemView.findViewById(R.id.pages_viewmore_1)
        val viewmore_btn_2 : Button = itemView.findViewById(R.id.pages_viewmore_2)


        init {
            pages_layout_1 = itemView.findViewById(R.id.pages_layout_1)
            pages_layout_2 = itemView.findViewById(R.id.pages_layout_2)
//            groupLayout1.isClickable = false
//            groupLayout1.isFocusable = false
            pages_img_1= itemView.findViewById(R.id.pages_groupimage_1)
            pagesname_1= itemView.findViewById(R.id.pages_groupname_1)
            pages_img_2= itemView.findViewById(R.id.pages_groupimage_2)
            pagesname_2= itemView.findViewById(R.id.pages_groupname_2)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pages_recycler_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_pages_item,parent,false)

        return pages_recycler_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: pages_recycler_viewholder, position: Int) {

//        Glide.with(holder.itemView.context)
//            .load(itemslist[position].img)
//            .into(holder.display_pic)
        holder.index=itemslist[position].count


        if(holder.index==1) {
            holder.pagesname_1.setText(itemslist[position].pagename_1)
            holder.pages_layout_2.visibility= View.INVISIBLE

            holder.viewmore_btn_1.setOnClickListener{
                listener.onCLick_fun(position,itemslist[position].pagename_1.toString(),0)
            }
            holder.pages_layout_1.setOnClickListener{
                listener.onCLick_fun(position,itemslist[position].pagename_1.toString(),0)
            }

        }else{
            holder.pagesname_1.setText(itemslist[position].pagename_1)
            holder.pagesname_2.setText(itemslist[position].pagename_2)

            holder.viewmore_btn_1.setOnClickListener{
                listener.onCLick_fun(position,itemslist[position].pagename_1.toString(),0)
            }
            holder.viewmore_btn_2.setOnClickListener{
                listener.onCLick_fun(position,itemslist[position].pagename_2.toString(),0)
            }

            holder.pages_layout_1.setOnClickListener{
                listener.onCLick_fun(position,itemslist[position].pagename_1.toString(),0)
            }

            holder.pages_layout_2.setOnClickListener{
                listener.onCLick_fun(position,itemslist[position].pagename_2.toString(),0)
            }

        }

    }
}