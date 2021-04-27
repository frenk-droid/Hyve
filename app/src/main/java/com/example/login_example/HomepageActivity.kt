package com.example.login_example

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.prova_download.*


class HomepageActivity : AppCompatActivity() {


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private lateinit var imageRef: StorageReference
    private var firebase = Firebase.database.reference
    val topic = mutableListOf<Topic>()
    private lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.prova_download)

        val User: user? = intent.getSerializableExtra("USER_DATA") as? user
        val img_path : String?= intent.getSerializableExtra("IMG_DATA") as? String
        Log.d("USER DATA HomePage", "${User?.username}  ${User?.email}    ${User?.password}")

        if(img_path==null)
            imageRef = storage.getReferenceFromUrl("gs://hyve-d0e7b.appspot.com/profile_images/${User?.image_profile}")
        else
            imageRef = storage.getReferenceFromUrl("gs://hyve-d0e7b.appspot.com/profile_images/${img_path}")

        val ONE_MEGABYTE = (1024 * 1024).toLong()
        imageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener { bytes ->
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    imageView5.setImageBitmap(bmp)

                }
                .addOnFailureListener { exception ->

                    exception.message?.let { Log.d("Error", it) }
                }
        context = this
        createTopic(context)



    }


    private fun createTopic(context: Context) {


        firebase.child("topics").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.getChildren())
                    topic.add(data.getValue<Topic>()!!)

                recyclerView.adapter = ContactAdapter(context, topic)
                recyclerView.layoutManager = LinearLayoutManager(context)


            }
        })

        Log.d("Topic out onDChange", topic.toString())

    }


    fun newTopic(v: View?){

        val intent = Intent(this, NewTopic::class.java)
        startActivityForResult(intent, 1)

    }

    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                val img= data.getStringExtra("image_path")
                Log.d("imgkkkkkkkkkkkkkkk", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa ${img.toString()}")
                val id= data.getStringExtra("id")
                val nome= data.getStringExtra("nome")
                val text= data.getStringExtra("text")
                topic.add(Topic(id!!, nome!!, img!!, text!!, mutableListOf()))

                recyclerView.adapter = ContactAdapter(context, topic)
                recyclerView.layoutManager = LinearLayoutManager(context)
            }


        }
    }

}






