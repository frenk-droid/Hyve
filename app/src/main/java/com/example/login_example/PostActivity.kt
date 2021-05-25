package com.example.login_example

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.item_contact.view.*
import kotlinx.android.synthetic.main.prova_download.*
import java.lang.reflect.Array

class PostActivity : AppCompatActivity() {

    private lateinit var topic: Topic
    val context = this
    val posts_finale= mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        topic= intent.getSerializableExtra("topic-data") as Topic

        var imageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://hyve-d0e7b.appspot.com/topic_images/${topic.image_path}")
        val ONE_MEGABYTE = (1024 * 1024).toLong()
        imageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener { bytes ->
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    imageView4.setImageBitmap(bmp)
                }
                .addOnFailureListener { exception ->
                    exception.message?.let { Log.d("Error", it) }
                }
        postListCreate(topic.posts_ids, this)
    }

    fun addTopic(v: View?) {
        val intent = Intent(this, NewPost::class.java)
        intent.putExtra("topic-data", topic)
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null) {
                val img = data.getStringExtra("image_path")
                val id = data.getStringExtra("id")
                val nome = data.getStringExtra("nome")
                val text = data.getStringExtra("text")
                posts_finale.add(Post(id!!, nome!!, img!!, text!!, mutableListOf("")))

                r.adapter = ContactAdapter2(context!!, posts_finale)
                r.layoutManager = LinearLayoutManager(context)

            }
        }
    }

    private  fun postListCreate(ids: List<String>, context: Context) {
        Firebase.database.reference.child("posts").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var posts= mutableListOf<Post>()
                for (data in snapshot.getChildren())
                    posts.add(data.getValue<Post>()!!)
                Log.d("post", posts.toString())

                for(post in posts)
                    if(ids.contains(post.id))
                        posts_finale.add(post)
                r.adapter = ContactAdapter2(context, posts_finale) //se posts_finale è null c'è un problema
                r.layoutManager = LinearLayoutManager(context)
            }
        })
    }
}


