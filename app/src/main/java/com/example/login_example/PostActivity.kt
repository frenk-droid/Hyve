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

    var topic: Topic? = null
    var ids= mutableListOf<String>()
    var post = mutableListOf<Post>()
    val posts_finale= mutableListOf<Post>()
    var context: Context? = null
    private var firebase = Firebase.database.reference
    private var firebase1 = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val name: String? = intent.getSerializableExtra("topic_nome") as? String
        val img_path: String? = intent.getSerializableExtra("topic_image") as? String

        val topic_id: String? = intent.getSerializableExtra("topic_id") as? String
        val topic_text: String? = intent.getSerializableExtra("topic_text") as? String
        val topic_list = intent.getParcelableArrayListExtra<Parcelable>("topic_list") as ArrayList<String>
        topic = Topic(topic_id!!, name!!, img_path!!, topic_text!!, topic_list)
        context = this
        Log.d("Topic values", topic.toString())

        var storage = FirebaseStorage.getInstance()
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
        Log.d("topics_list", topic_list.toString())
    postListCreate(topic_list, this)

    }

    fun addTopic(v: View?) {

        val intent = Intent(this, NewPost::class.java)
        intent.putExtra("topic_nome", topic!!.nome)
        intent.putExtra("topic_image", topic!!.image_path)
        intent.putExtra("topic_id", topic!!.id)
        intent.putExtra("topic_text", topic!!.text)
        intent.putExtra("topic_list", ArrayList(topic!!.posts_ids))
        startActivityForResult(intent, 2)
//startActivity(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null) {
                val img = data.getStringExtra("image_path")
                val id = data.getStringExtra("id")
                val nome = data.getStringExtra("nome")
                val text = data.getStringExtra("text")
               // val list = intent.getParcelableArrayListExtra<Parcelable>("post_list") as java.util.ArrayList<String>
                posts_finale.add(Post(id!!, nome!!, img!!, text!!, mutableListOf("")))

                r.adapter = ContactAdapter2(context!!, posts_finale)
                r.layoutManager = LinearLayoutManager(context)

            }
        }
    }

   /* private fun createTopic(context: Context) { // ora va bene solo per uno ma è da modificare


        firebase.child("topics").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.getChildren())
                    for (pino in data.getValue<Topic>()!!.posts_ids)
                        ids.add(pino)
                postListCreate(ids, context)

                /* recyclerView.adapter = ContactAdapter2(context, post)
                recyclerView.layoutManager = LinearLayoutManager(context)*/


            }*/

          private  fun postListCreate(ids: List<String>, context: Context) {
                firebase.child("posts").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        println(error.message)
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        var posts= mutableListOf<Post>()


                        for (data in snapshot.getChildren())
                            posts.add(data.getValue<Post>()!!)
                        Log.d("post", posts.toString())

                         for(post in posts){

                             if(ids.contains(post.id))
                                 posts_finale.add(post)

                             }
                        Log.d("post_finale", posts_finale.toString())
                        Log.d("ids", ids.toString())

                        r.adapter = ContactAdapter2(context, posts_finale) //se posts_finale è null c'è un problema
                        r.layoutManager = LinearLayoutManager(context)


                    }

                })
            }
       // })
   // }
}


