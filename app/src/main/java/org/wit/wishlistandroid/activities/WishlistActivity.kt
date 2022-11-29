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
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Wishlist Activity started...")


        if(intent.hasExtra("wishlist_edit")){
            edit = true
            wishlist = intent.extras?.getParcelable("wishlist_edit")!!
            binding.wishlistTitle.setText(wishlist.title)
            binding.wishlistDescription.setText(wishlist.description)
            binding.btnAdd.setText(R.string.save_wishlist)
        }


        binding.btnAdd.setOnClickListener() {
            wishlist.title = binding.wishlistTitle.text.toString()
            wishlist.description = binding.wishlistDescription.text.toString()
            if (wishlist.title.isEmpty() && wishlist.description.isEmpty()) {
                Snackbar.make(it, R.string.enter_title, Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
                if(edit){
                    app.wishlists.update(wishlist.copy())
                } else{
                    app.wishlists.create(wishlist.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
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