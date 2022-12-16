package org.wit.wishlistandroid.main

import android.app.Application
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.wit.wishlistandroid.models.WishlistJSONStore
import org.wit.wishlistandroid.models.WishlistMemStore
import org.wit.wishlistandroid.models.WishlistStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application() {

    //val wishlists = WishlistMemStore()
    lateinit var wishlists: WishlistStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        wishlists = WishlistJSONStore(applicationContext)
        i("Wishlist started")
    }
}