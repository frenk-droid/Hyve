package com.example.login_example

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.android.synthetic.main.activity_new_topic.*
import kotlinx.android.synthetic.main.prova_download.*
import java.util.*

class NewPost : AppCompatActivity() {
    private lateinit var task: StorageTask<UploadTask.TaskSnapshot>
    private lateinit var random: String;
    private lateinit var topicc: Topic
    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
    }

    fun uploadPost(v:View?) {
        val textPost = "Provaaaa"
        val namePost = editTextTextPersonName3.text.toString()
        var topics = mutableListOf<Topic>()
        var post_ids= mutableListOf<String>()
        val passed_topic =  intent.getSerializableExtra("topic-data") as Topic

        Firebase.database.reference.child("topics").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children)
                    topics.add(data.getValue<Topic>()!!)
                for(topic in topics)
                    if (topic.nome.equals(passed_topic.nome)) {
                        post_ids.addAll(topic.posts_ids)
                        topicc = topic
                    }
                if (uri != null)
                    uploadImageToFirebaseStorage()
                Tasks.whenAll(task).addOnCompleteListener(OnCompleteListener { task: Task<Void?> ->
                    var post_id = UUID.randomUUID().toString()
                    val returnIntent = Intent()
                    post_ids.add(post_id)
                    /*val post= Post(post_id, namePost, random, textPost, mutableListOf(""))
                    Firebase.database.reference.database.getReference("posts").child(post_id).setValue(post)
                    val newData= Topic(topicc.id, topicc.nome, topicc.image_path, topicc.text, post_ids)
                    Firebase.database.reference.database.getReference("topics").child(topicc.id).setValue(newData)
                    returnIntent.putExtra("image_path", post.image_path)
                    returnIntent.putExtra("nome", post.nome)
                    returnIntent.putExtra("id", post.id)
                    returnIntent.putExtra("text",post.text)
                    setResult(RESULT_OK, returnIntent)
                    finish()*/
                }  )

            }
        })
    }

    fun uploadImage3(v: View?) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, 4)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 4 && data != null && resultCode == Activity.RESULT_OK) {
            uri = data.data
            imageButton3.setImageURI(uri)
        }
    }

    private fun uploadImageToFirebaseStorage() {
        random = UUID.randomUUID().toString()
        val filename = "post_images/${random}"
        task= FirebaseStorage.getInstance().getReference(filename).putFile(uri!!)
            .addOnSuccessListener {
                Log.d("Yes", "Success to upload image to storage")
            }
            .addOnFailureListener {
                Log.d("no", "Failed to upload image to storage")
            }
        }
    }







