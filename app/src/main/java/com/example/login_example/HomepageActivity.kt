package com.example.login_example

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.android.synthetic.main.prova_download.*


class HomepageActivity : AppCompatActivity() {

     override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    private var storage : FirebaseStorage = FirebaseStorage.getInstance()
    private lateinit var imageRef : StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.prova_download)

        val User : user? = intent.getSerializableExtra("USER_DATA") as? user
        Log.d("USER DATA HomePage", "${User?.image_profile}")

        imageRef = storage.getReferenceFromUrl("gs://hyve-d0e7b.appspot.com/profile_images/${User?.image_profile}")


        val ONE_MEGABYTE = (1024 * 1024).toLong()
        imageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener { bytes ->
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    imageView5.setImageBitmap(bmp)

                }
                .addOnFailureListener { exception ->

                    exception.message?.let { Log.d("Error", it) }
                }




    }

    }
