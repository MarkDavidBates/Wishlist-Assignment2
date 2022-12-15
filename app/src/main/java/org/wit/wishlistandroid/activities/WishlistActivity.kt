package org.wit.wishlistandroid.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.wishlistandroid.R
import org.wit.wishlistandroid.databinding.ActivityWishlistBinding
import org.wit.wishlistandroid.helpers.showImagePicker
import org.wit.wishlistandroid.main.MainApp
import org.wit.wishlistandroid.models.Location
import org.wit.wishlistandroid.models.WishlistModel
import timber.log.Timber.i
import java.text.SimpleDateFormat

class WishlistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWishlistBinding
    var wishlist = WishlistModel()
    lateinit var app : MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>


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
            binding.wishlistAttendees.setText(wishlist.attendees)
            binding.wishlistDate.setDate(wishlist.date)
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
            wishlist.attendees = binding.wishlistAttendees.text.toString()
            wishlist.date = binding.wishlistDate.date
            if (wishlist.title.isEmpty() && wishlist.description.isEmpty() && wishlist.attendees.isDigitsOnly()) {
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
            showImagePicker(imageIntentLauncher, this)
        }
        registerImagePickerCallback()

        binding.wishlistLocation.setOnClickListener {
            var location = Location(52.245696, -7.139102, 15f)
            if(wishlist.zoom != 0f){
                location.lat = wishlist.lat
                location.long = wishlist.long
                location.zoom = wishlist.zoom
            }

            val launcherIntent = Intent(this, MapActivity::class.java).putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerMapCallback()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_wishlist, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                setResult(99)
                app.wishlists.delete(wishlist)
                finish()
            }
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

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            wishlist.image = image

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
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            wishlist.lat = location.lat
                            wishlist.long = location.long
                            wishlist.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}