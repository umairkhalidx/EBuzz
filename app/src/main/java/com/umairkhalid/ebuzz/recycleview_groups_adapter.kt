package com.umairkhalid.ebuzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class recycleview_groups_adapter(val itemslist: ArrayList<recycleview_groups_data>,private val listener: ClickListner)
    : RecyclerView.Adapter<recycleview_groups_adapter.groups_recycler_viewholder>()
{
    inner class groups_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var group_layout_1 : LinearLayout
        lateinit var group_layout_2 : LinearLayout
        lateinit var group_img_1 : ImageView
        lateinit var groupname_1 : TextView
        lateinit var group_img_2 : ImageView
        lateinit var groupname_2 : TextView
        var index : Int = 0

        lateinit var leavegroup_btn_1 : Button
        lateinit var leavegroup_btn_2 : Button
        lateinit var viewmore_btn_1 : Button
        lateinit var viewmore_btn_2 : Button


        init {
            group_layout_1 = itemView.findViewById(R.id.groups_layout_1)
            group_layout_2 = itemView.findViewById(R.id.groups_layout_2)
//            groupLayout1.isClickable = false
//            groupLayout1.isFocusable = false
            group_img_1= itemView.findViewById(R.id.groups_groupimage_1)
            groupname_1= itemView.findViewById(R.id.groups_groupname_1)
            group_img_2= itemView.findViewById(R.id.groups_groupimage_2)
            groupname_2= itemView.findViewById(R.id.groups_groupname_2)

            leavegroup_btn_1 = itemView.findViewById(R.id.groups_leave_btn_1)
            leavegroup_btn_2 = itemView.findViewById(R.id.groups_leave_btn_2)
            viewmore_btn_1 = itemView.findViewById(R.id.groups_viewmore_btn_1)
            viewmore_btn_2 = itemView.findViewById(R.id.groups_viewmore_btn_2)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): groups_recycler_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_groups_item,parent,false)

        return groups_recycler_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: groups_recycler_viewholder, position: Int) {

//        Glide.with(holder.itemView.context)
//            .load(itemslist[position].img)
//            .into(holder.display_pic)
        holder.index=itemslist[position].count


        if(holder.index==1) {
            holder.groupname_1.setText(itemslist[position].groupname_1)
            holder.group_layout_2.visibility=View.INVISIBLE
//
//            holder.leavegroup_btn_1.setOnClickListener{
//                listener.onCLick_fun(position,"")
//            }
            holder.group_img_1.setOnClickListener{
                listener.onCLick_fun(position,"",0)
            }
            holder.groupname_1.setOnClickListener{
                listener.onCLick_fun(position,"",0)
            }
            holder.leavegroup_btn_1.setOnClickListener{
                listener.onCLick_fun(position,"",1)
            }
            holder.viewmore_btn_1.setOnClickListener{
                listener.onCLick_fun(position,"",0)
            }


        }else{
            holder.groupname_1.setText(itemslist[position].groupname_1)
            holder.groupname_2.setText(itemslist[position].groupname_2)

//            holder.leavegroup_btn_1.setOnClickListener{
//                listener.onCLick_fun(position,"")
//            }
//            holder.leavegroup_btn_2.setOnClickListener{
//                listener.onCLick_fun(position,"")
//            }

            holder.group_img_1.setOnClickListener{
                listener.onCLick_fun(position,"",0)
            }

            holder.group_img_2.setOnClickListener{
                listener.onCLick_fun(position,"",0)
            }

            holder.groupname_1.setOnClickListener{
                listener.onCLick_fun(position,"",0)
            }

            holder.groupname_2.setOnClickListener{
                listener.onCLick_fun(position,"",0)
            }

            holder.viewmore_btn_1.setOnClickListener{
                listener.onCLick_fun(position,"",0)
            }

            holder.viewmore_btn_1.setOnClickListener{
                listener.onCLick_fun(position,"",0)
            }
            holder.leavegroup_btn_1.setOnClickListener{
                listener.onCLick_fun(position,"",1)
            }
            holder.leavegroup_btn_2.setOnClickListener{
                listener.onCLick_fun(position,"",1)
            }

        }

    }
}