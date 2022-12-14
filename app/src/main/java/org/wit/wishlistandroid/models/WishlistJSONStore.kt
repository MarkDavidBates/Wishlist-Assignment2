package org.wit.wishlistandroid.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.wishlistandroid.helpers.*
import org.wit.wishlistandroid.main.exists
import org.wit.wishlistandroid.main.read
import org.wit.wishlistandroid.main.write
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "wishlists.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<WishlistModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class WishlistJSONStore(private val context: Context) : WishlistStore {

    var wishlists = mutableListOf<WishlistModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<WishlistModel> {
        logAll()
        return wishlists
    }

    override fun create(wishlist: WishlistModel) {
        wishlist.id = generateRandomId()
        wishlists.add(wishlist)
        serialize()
    }


    override fun update(wishlist: WishlistModel) {
        val wishlistsList = findAll() as ArrayList<WishlistModel>
        var foundWishlist: WishlistModel? = wishlistsList.find { p -> p.id == wishlist.id }
        if (foundWishlist != null) {
            foundWishlist.title = wishlist.title
            foundWishlist.description = wishlist.description
            foundWishlist.image = wishlist.image
            foundWishlist.lat = wishlist.lat
            foundWishlist.long = wishlist.long
            foundWishlist.zoom = wishlist.zoom
        }
        serialize()
    }

    override fun delete(wishlist: WishlistModel) {
        wishlists.remove(wishlist)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(wishlists, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        wishlists = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        wishlists.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}