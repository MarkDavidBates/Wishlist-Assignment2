package org.wit.wishlistandroid.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import org.wit.wishlistandroid.R
import org.wit.wishlistandroid.databinding.ActivityWishlistBinding
import org.wit.wishlistandroid.main.MainApp
import org.wit.wishlistandroid.models.WishlistModel
import timber.log.Timber
import timber.log.Timber.i

class WishlistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWishlistBinding
    var wishlist = WishlistModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Wishlist Activity started...")


        binding.btnAdd.setOnClickListener() {
            wishlist.title = binding.wishlistTitle.text.toString()
            wishlist.description = binding.wishlistDescription.text.toString()
            if (wishlist.title.isNotEmpty() && wishlist.description.isNotEmpty()) {
                i("add Button Pressed: ${wishlist.title} , ${wishlist.description}")
                app.wishlists.add(wishlist.copy())
                for (i in app.wishlists.indices){
                    i("Wishlist[$i]: ${this.app.wishlists[i]}")
                }
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_wishlist, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}