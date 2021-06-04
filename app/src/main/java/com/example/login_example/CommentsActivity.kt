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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.activity_post.imageView4
import kotlinx.android.synthetic.main.prova_download.*
import java.util.*
import kotlin.collections.ArrayList

class CommentsActivity : AppCompatActivity() {
    val context= this

    private lateinit var post: Post
    private lateinit var text: String
    private lateinit var uid: String
    val commenti_finale= mutableListOf<Comment>()
    val comment_ids= mutableListOf<String>()
    private var firebase = Firebase.database.reference

//testare recycler view
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        post = intent.getSerializableExtra("post-data") as Post

        val ONE_MEGABYTE = (1024 * 1024).toLong()
        FirebaseStorage.getInstance().getReferenceFromUrl("gs://hyve-d0e7b.appspot.com/post_images/${post.image_path}").getBytes(ONE_MEGABYTE)
            .addOnSuccessListener { bytes ->
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imageView7.setImageBitmap(bmp)
            }
            .addOnFailureListener { exception ->
                exception.message?.let { Log.d("Error", it) }
            }
        loadRecyclerView(context)
    }

    private fun loadRecyclerView(context: Context){

        val commenti_list= mutableListOf<Comment>()
        firebase.child("comments").addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.getChildren())
                    commenti_list.add(data.getValue<Comment>()!!)
                for(commento in commenti_list)
                    if(post.commenti_ids.contains(commento.id))
                        commenti_finale.add(commento)
                r1.adapter = ContactAdapter3(context, commenti_finale)
                r1.layoutManager = LinearLayoutManager(context)
            }
        })
    }

    fun addComment(v: View?) {
        firebase.child("users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val username= snapshot.getValue<user>()!!.username
                val text= editTextTextMultiLine3.text.toString()
                val comment_id = UUID.randomUUID().toString()
                val comment= Comment(comment_id, uid, username, text)

                firebase.database.getReference("comments").child(comment_id).setValue(comment)
                comment_ids.add(comment_id)
              //  val newData= Post(post.id, post.nome, post.image_path, post.text, comment_ids)
              //  firebase.database.getReference("posts").child(post.id).setValue(newData)
                commenti_finale.add(comment)
                r1.adapter = ContactAdapter3(context, commenti_finale)
                r1.layoutManager = LinearLayoutManager(context)
            }
        })
    }
}