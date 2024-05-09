package com.umairkhalid.ebuzz

import android.os.Parcel
import android.os.Parcelable

data class UserData(var userID:String, var name: String = "", var email: String = "", var contact: String = "", var country: String = "",
                    var province: String = "", var age: String = "", var password: String = "",
                    var profileType: String = "", var profileCategory: String = "")
    : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userID)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(contact)
        parcel.writeString(country)
        parcel.writeString(province)
        parcel.writeString(age)
        parcel.writeString(password)
        parcel.writeString(profileType)
        parcel.writeString(profileCategory)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserData> {
        override fun createFromParcel(parcel: Parcel): UserData {
            return UserData(parcel)
        }

        override fun newArray(size: Int): Array<UserData?> {
            return arrayOfNulls(size)
        }
    }
}