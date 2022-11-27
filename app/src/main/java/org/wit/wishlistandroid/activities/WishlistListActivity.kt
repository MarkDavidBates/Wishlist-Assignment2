package org.wit.wishlistandroid.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.wit.wishlistandroid.databinding.ActivityWishlistListBinding
import org.wit.wishlistandroid.databinding.CardWishlistBinding
import org.wit.wishlistandroid.main.MainApp
import org.wit.wishlistandroid.models.WishlistModel

class WishlistListActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityWishlistListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = WishlistAdapter(app.wishlists)
    }
}

class WishlistAdapter constructor(private var wishlists: List<WishlistModel>) :
    RecyclerView.Adapter<WishlistAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardWishlistBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val wishlist = wishlists[holder.adapterPosition]
        holder.bind(wishlist)
    }

    override fun getItemCount(): Int = wishlists.size

    class MainHolder(private val binding : CardWishlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(wishlist: WishlistModel) {
            binding.wishlistTitle.text = wishlist.title
            binding.description.text = wishlist.description
        }
    }
}