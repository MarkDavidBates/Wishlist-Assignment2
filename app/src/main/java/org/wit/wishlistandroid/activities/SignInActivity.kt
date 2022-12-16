package org.wit.wishlistandroid.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.wit.wishlistandroid.adapters.WishlistAdapter
import org.wit.wishlistandroid.databinding.ActivityWishlistSigninBinding
import androidx.recyclerview.widget.LinearLayoutManager
import timber.log.Timber.i

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWishlistSigninBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        binding = ActivityWishlistSigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button3.setOnClickListener()
        {

            if(EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches() && binding.password.text.toString().length > 5)
            {
                signIn(binding.email.text.toString(), binding.password.text.toString());
            }
            else
            {
                i("Invalid Signin Input")
            }
        }

        binding.button2.setOnClickListener()
        {

            if(EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches() && binding.password.text.toString().length > 5)
            {
                createAccount(binding.email.text.toString(), binding.password.text.toString());
            }
            else
            {
                i("Invalid Signin Input")
            }
        }
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK)
            {
                i("Logged Out")
                binding.email.text.clear()
                binding.password.text.clear()
            }
        }

    public override fun onStart()
    {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null)
        {
            //if we are signed in already redirect to the character list
        }
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    i("signInWithEmail:success")
                    val user = auth.currentUser
                    launchApp(user)
                } else {
                    // If sign in fails, display a message to the user.
                    i("signInWithEmail:failure" + task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    i("createUserWithEmail:success")
                    val user = auth.currentUser
                    launchApp(user)
                } else {
                    // If sign in fails, display a message to the user.
                    i("createUserWithEmail:failure" + task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
        // [END create_user_with_email]
    }

    private fun launchApp(user: FirebaseUser?)
    {
        val userIntent = Intent(this, WishlistListActivity::class.java)
        userIntent.putExtra("user_logged_in", user)
        getResult.launch(userIntent)
    }
}