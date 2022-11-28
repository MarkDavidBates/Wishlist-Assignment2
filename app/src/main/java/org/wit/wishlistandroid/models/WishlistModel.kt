package org.wit.wishlistandroid.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WishlistModel(var id: Long = 0,
                         var title: String = "",
                         var description: String = "") : Parcelable
