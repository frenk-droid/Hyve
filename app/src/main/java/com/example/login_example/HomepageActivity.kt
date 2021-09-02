package com.example.login_example

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.prova_download.*


class HomepageActivity : AppCompatActivity() {
    private var firebase= Firebase.database.reference
    private lateinit var context: Context

    val topic = mutableListOf<Topic>() //change location of this variable

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.prova_download)
        val User: user = intent.getSerializableExtra("USER_DATA") as user
        val ONE_MEGABYTE = (1024 * 1024).toLong()

        FirebaseStorage.getInstance().getReferenceFromUrl("gs://hyve-d0e7b.appspot.com/profile_images/${User.image_profile}").getBytes(ONE_MEGABYTE)
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
              //  recyclerView.adapter = ContactAdapter(context, topic)
                recyclerView.layoutManager = LinearLayoutManager(context)
            }
        })
    }

    fun newTopic(v: View?){
        val intent = Intent(this, NewTopic::class.java)
        startActivityForResult(intent, 1)
    }

    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data!=null) { //this part will change with fragment implementation
            val img= data.getStringExtra("image_path")
            val id= data.getStringExtra("id")
            val nome= data.getStringExtra("nome")
            val text= data.getStringExtra("text")
            topic.add(Topic(id!!, nome!!, img!!, text!!, mutableListOf()))
           // recyclerView.adapter = ContactAdapter(context, topic)
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

}






