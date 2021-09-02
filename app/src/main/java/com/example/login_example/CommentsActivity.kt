package com.example.login_example

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CommentsActivity : AppCompatActivity() {
    private lateinit var User: user
    val context= this
    private lateinit var post: Post
    private lateinit var text: String
    private lateinit var uid: String
    val commenti_finale= mutableListOf<Comment>()
    val comment_ids= mutableListOf<String>()
    private lateinit var btt: Button
    private var firebase = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        post = intent.getSerializableExtra("post-data") as Post
        User=  intent.getSerializableExtra("user-data") as user
        val context= this
        val ONE_MEGABYTE = (1024 * 1024).toLong()
        FirebaseStorage.getInstance().getReferenceFromUrl("gs://hyve-d0e7b.appspot.com/post_images/${post.image_path}").getBytes(ONE_MEGABYTE)
            .addOnSuccessListener { bytes ->
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imageView7.setImageBitmap(bmp)
            }
            .addOnFailureListener { exception ->
                exception.message?.let { Log.d("Error", it) }
            }
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                val comments= FirebaseHelper().getComment(post)

                r1.adapter = ContactAdapter4(context,comments)
                r1.layoutManager = LinearLayoutManager(context)
                //todo verificare se anche con adapter vuoto non da errori
            }
        }

        backBtt.setOnClickListener{
            finish()
        }
    }

    fun addComment(v: View?) {
        val commento= Comment(UUID.randomUUID().toString(), User.username, User.image_profile, editTextTextMultiLine3.text.toString())
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                FirebaseHelper().addComment(commento,post)
            }
        }
        finish()
    }
}