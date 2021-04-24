package com.example.login_example

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.item_contact.view.*

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val name: String? =  intent.getSerializableExtra("post_data") as? String
        val img_path: String? =  intent.getSerializableExtra("post_image") as? String
        var storage= FirebaseStorage.getInstance()
        var imageRef = storage.getReferenceFromUrl("gs://hyve-d0e7b.appspot.com/topic_images/${img_path}")


        val ONE_MEGABYTE = (1024 * 1024).toLong()
        imageRef.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener { bytes ->
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imageView4.setImageBitmap(bmp)

            }
            .addOnFailureListener { exception ->

                exception.message?.let { Log.d("Error", it) }
            }
            textView7.setText(name)

    }
}