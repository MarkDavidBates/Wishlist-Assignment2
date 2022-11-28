package org.wit.wishlistandroid.models

import timber.log.Timber.i

class WishlistMemStore : WishlistStore{
    val wishlists = ArrayList<WishlistModel>()

    override fun findAll(): List<WishlistModel> {
        return wishlists
    }

    override fun create(wishlist: WishlistModel) {
        wishlists.add(wishlist)
        logAll()
    }

    fun logAll(){
        //TODO: fix line below
        //wishlists.forEach(i("${it}"))
    }
}