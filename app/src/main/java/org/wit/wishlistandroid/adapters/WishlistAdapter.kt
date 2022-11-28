package org.wit.wishlistandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.wishlistandroid.databinding.CardWishlistBinding
import org.wit.wishlistandroid.models.WishlistModel

interface WishlistListener{
    fun onWishlistClick(wishlist: WishlistModel)
}

class WishlistAdapter constructor(private var wishlists: List<WishlistModel>, private val listener: WishlistListener) :
    RecyclerView.Adapter<WishlistAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardWishlistBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val wishlist = wishlists[holder.adapterPosition]
        holder.bind(wishlist, listener)
    }

    override fun getItemCount(): Int = wishlists.size

    class MainHolder(private val binding : CardWishlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(wishlist: WishlistModel, listener: WishlistListener) {
            binding.wishlistTitle.text = wishlist.title
            binding.description.text = wishlist.description
            binding.root.setOnClickListener{listener.onWishlistClick(wishlist)}
        }
    }
}