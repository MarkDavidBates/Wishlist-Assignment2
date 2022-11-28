package org.wit.wishlistandroid.main

import android.app.Application
import org.wit.wishlistandroid.models.WishlistMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application() {

    val wishlists = WishlistMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Wishlist started")
        //wishlists.add(WishlistModel("bing", "chilling"))
    }
}