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

    private lateinit var post: Post
    private lateinit var context:Context
    private lateinit var text: String
    private lateinit var uid: String
    val commenti_finale= mutableListOf<Comment>()
    val comment_ids= mutableListOf<String>()
    private var firebase = Firebase.database.reference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        val name: String? = intent.getSerializableExtra("post_nome") as? String
        val img_path: String? = intent.getSerializableExtra("post_image") as? String

        val post_id: String? = intent.getSerializableExtra("post_id") as? String
        val post_text: String? = intent.getSerializableExtra("post_text") as? String
        val commenti_list = intent.getParcelableArrayListExtra<Parcelable>("commenti_list") as ArrayList<String>
        post = Post(post_id!!, name!!, img_path!!, post_text!!, commenti_list)
        Log.d("Post value in comment", post.toString())
        auth= Firebase.auth
        uid= auth.uid.toString()
        context= this
        var storage = FirebaseStorage.getInstance()
        var imageRef = storage.getReferenceFromUrl("gs://hyve-d0e7b.appspot.com/post_images/${img_path}")


        val ONE_MEGABYTE = (1024 * 1024).toLong()
        imageRef.getBytes(ONE_MEGABYTE)
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



        /*recyclerView.adapter = ContactAdapter3(context, commenti_finale)
        recyclerView.layoutManager = LinearLayoutManager(context)*/

        firebase.child("users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                var  username= snapshot.getValue<user>()!!.username
                text= editTextTextMultiLine3.text.toString()
                var comment_id = UUID.randomUUID().toString()
                var comment= Comment(comment_id, uid, username, text)
                var reference = firebase.database.getReference("comments")
                reference.child(comment_id).setValue(comment)
                reference = firebase.database.getReference("posts")
                comment_ids.add(comment_id)
                var dr  = reference.child(post.id)
                val newData= Post(post.id, post.nome, post.image_path, post.text, comment_ids)
                dr.setValue(newData)

                commenti_finale.add(comment)
               r1.adapter = ContactAdapter3(context, commenti_finale)
               r1.layoutManager = LinearLayoutManager(context)


            }
        })


    }


   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null) {
                val img = data.getStringExtra("image_path")
                val id = data.getStringExtra("id")
                val nome = data.getStringExtra("nome")
                val text = data.getStringExtra("text")
                // val list = intent.getParcelableArrayListExtra<Parcelable>("post_list") as java.util.ArrayList<String>
                commenti_finale.add(Post(id!!, nome!!, img!!, text!!, mutableListOf("")))

                r.adapter = ContactAdapter2(context!!, posts_finale)
                r.layoutManager = LinearLayoutManager(context)

            }
        }
    }
    }*/
}