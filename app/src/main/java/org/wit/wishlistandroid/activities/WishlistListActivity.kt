package org.wit.wishlistandroid.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.wit.wishlistandroid.R
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

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, WishlistActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.wishlists.size)
            }
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