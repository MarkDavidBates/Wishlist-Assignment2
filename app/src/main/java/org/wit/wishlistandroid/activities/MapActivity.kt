package org.wit.wishlistandroid.activities

import android.app.Activity
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.wishlistandroid.R
//import org.wit.wishlistandroid.activities.databinding.ActivityMapsBinding
import org.wit.wishlistandroid.databinding.ActivityMapsBinding
import org.wit.wishlistandroid.models.WishlistModel

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var location = org.wit.wishlistandroid.models.Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        location = intent.extras?.getParcelable<org.wit.wishlistandroid.models.Location>("location")!!

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val loc = LatLng(location.lat, location.long)
        val options = MarkerOptions()
            .title("Wishlist")
            .snippet("GPS : $loc")
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
        map.setOnMarkerDragListener(this)
    }

    override fun onMarkerDrag(p0: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        location.lat = marker.position.latitude
        location.long = marker.position.longitude
        location.zoom = map.cameraPosition.zoom
    }

    override fun onMarkerDragStart(p0: Marker) {
    }

    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()
    }
}