package com.example.sel.screen.user.blog.model


import android.os.Parcel
import android.os.Parcelable

data  class CommentItem(

    val comment: String?="null",
    val date: String? ="null",
    val profileImage: String? ="null",
    val username: String? ="null",
    var userId :String="null",
    var commentId :String="null",
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"null",
        parcel.readString()?:"null",
        parcel.readString()?:"null",
        parcel.readString()?:"null",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(comment)
        parcel.writeString(date)
        parcel.writeString(profileImage)
        parcel.writeString(username)
        parcel.writeString(commentId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CommentItem> {
        override fun createFromParcel(parcel: Parcel): CommentItem {
            return CommentItem(parcel)
        }

        override fun newArray(size: Int): Array<CommentItem?> {
            return arrayOfNulls(size)
        }
    }

}

