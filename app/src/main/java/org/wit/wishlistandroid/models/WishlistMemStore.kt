package org.wit.wishlistandroid.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long{
    return lastId++
}

class WishlistMemStore : WishlistStore{
    val wishlists = ArrayList<WishlistModel>()

    override fun findAll(): List<WishlistModel> {
        return wishlists
    }

    override fun create(wishlist: WishlistModel) {
        wishlist.id = getId()
        wishlists.add(wishlist)
        logAll()
    }

    override fun update(wishlist: WishlistModel) {
        var foundWishlist: WishlistModel? = wishlists.find { p -> p.id == wishlist.id }
        if (foundWishlist != null){
            foundWishlist.title = wishlist.title
            foundWishlist.description = wishlist.description
            foundWishlist.image = wishlist.image
            logAll()
        }
    }

    fun logAll(){
        wishlists.forEach{i("$it")}
    }
}