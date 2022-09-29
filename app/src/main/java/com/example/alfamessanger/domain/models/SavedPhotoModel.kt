package com.example.alfamessanger.domain.models

import android.os.Parcel
import android.os.Parcelable

data class SavedPhotoModel(
    var id : String = "",
    var image_url: String = "",
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun equals(other: Any?): Boolean {
        return (other as MessageModel).id == id
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(image_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SavedPhotoModel> {
        override fun createFromParcel(parcel: Parcel): SavedPhotoModel {
            return SavedPhotoModel(parcel)
        }

        override fun newArray(size: Int): Array<SavedPhotoModel?> {
            return arrayOfNulls(size)
        }
    }
}
