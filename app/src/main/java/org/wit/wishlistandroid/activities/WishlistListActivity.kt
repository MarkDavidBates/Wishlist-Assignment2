package org.wit.wishlistandroid.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.wishlistandroid.R
import org.wit.wishlistandroid.databinding.ActivityWishlistListBinding
import org.wit.wishlistandroid.main.MainApp
import org.wit.wishlistandroid.adapters.WishlistAdapter
import org.wit.wishlistandroid.adapters.WishlistListener
import org.wit.wishlistandroid.models.WishlistModel

class WishlistListActivity : AppCompatActivity(), WishlistListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityWishlistListBinding
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = WishlistAdapter(app.wishlists.findAll(), this)

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
                notifyItemRangeChanged(0,app.wishlists.findAll().size)
            }
        }

    override fun onWishlistClick(wishlist: WishlistModel, pos: Int) {
        val launcherIntent = Intent(this, WishlistActivity::class.java)
        launcherIntent.putExtra("wishlist_edit", wishlist)
        position = pos
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK){
            (binding.recyclerView.adapter)?.notifyItemRangeChanged(0, app.wishlists.findAll().size)
        }
        else // Deleting
            if (it.resultCode == 99)
                    (binding.recyclerView.adapter)?.notifyItemRemoved(position)
    }
}