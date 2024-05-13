package com.umairkhalid.ebuzz

interface ClickListener_Groups {
    fun onClickGroups_fun(
        position: Int,
        username:String,
        operation:Int,
        groupID: String,
        groupName: String,
        groupImage: String
    )
}