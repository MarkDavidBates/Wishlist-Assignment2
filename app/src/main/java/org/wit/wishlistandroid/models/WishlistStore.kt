package org.wit.wishlistandroid.models

interface WishlistStore {

    fun findAll(): List<WishlistModel>

    fun create(wishlist: WishlistModel)

    fun update(wishlist: WishlistModel)
}