package org.wit.wishlistandroid.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.wishlistandroid.R
import org.wit.wishlistandroid.databinding.ActivityWishlistBinding
import org.wit.wishlistandroid.helpers.showImagePicker
import org.wit.wishlistandroid.main.MainApp
import org.wit.wishlistandroid.models.Location
import org.wit.wishlistandroid.models.WishlistModel
import timber.log.Timber
import timber.log.Timber.i

class WishlistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWishlistBinding
    var wishlist = WishlistModel()
    lateinit var app : MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var location = Location(52.245696, -7.139102, 15f)

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
            Picasso.get()
                .load(wishlist.image)
                .into(binding.wishlistImage)
            if (wishlist.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_wishlist_image)
            }
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

        binding.chooseImage.setOnClickListener {
            i("Select image")
            showImagePicker(imageIntentLauncher)
        }
        registerImagePickerCallback()

        binding.wishlistLocation.setOnClickListener {
            val launcherIntent = Intent(this, MapActivity::class.java).putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerMapCallback()

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

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            wishlist.image = result.data!!.data!!
                            Picasso.get()
                                .load(wishlist.image)
                                .into(binding.wishlistImage)
                            binding.chooseImage.setText(R.string.change_wishlist_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            location = result.data!!.extras?.getParcelable("location")!!
                            i("Location == $location")
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}