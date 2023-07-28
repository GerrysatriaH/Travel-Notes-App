package pnj.ti4a.uas_gerrysatriahalim.data

import android.os.Parcel
import android.os.Parcelable

data class TravelNote(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeString(location)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TravelNote> {
        override fun createFromParcel(parcel: Parcel): TravelNote {
            return TravelNote(parcel)
        }

        override fun newArray(size: Int): Array<TravelNote?> {
            return arrayOfNulls(size)
        }
    }
}