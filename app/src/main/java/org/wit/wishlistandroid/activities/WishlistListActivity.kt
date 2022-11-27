package org.wit.wishlistandroid.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.wit.wishlistandroid.R
import org.wit.wishlistandroid.main.MainApp

class WishlistListActivity : AppCompatActivity() {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist_list)
        app = application as MainApp
    }
}