package com.example.login_example

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class NewPost : AppCompatActivity(), AdapterView.OnItemSelectedListener  {
    private lateinit var task: StorageTask<UploadTask.TaskSnapshot>
    private lateinit var random: String;
    private lateinit var topicc: Topic
    private lateinit var User:user
    var uri: Uri? = null
    private lateinit var spinner: Spinner
    var context=this
    private lateinit var topics: MutableList<String>
    private lateinit var topicPost: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
         User = intent.getSerializableExtra("user-data") as user
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                topics = FirebaseHelper().getTopicName()
                spinner= findViewById<Spinner>(R.id.spinner)
                spinner.setOnItemSelectedListener(context)
                val aa = ArrayAdapter(context, R.layout.spinner, topics)
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.setAdapter(aa)
            }
        }
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        topicPost=topics[position]
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {
    }

    fun uploadPost(v:View?) {
        random= UUID.randomUUID().toString()
        val task= FirebaseStorage.getInstance().getReference("post_images/${random}").putFile(uri!!)
                .addOnSuccessListener {
                    Log.d("Yes", "Image Uploaded")
                }
                .addOnFailureListener {
                    Log.d("no", "Failed to upload image to storage")
                }
        Tasks.whenAll(task).addOnCompleteListener(OnCompleteListener { task: Task<Void?> ->
            val commentPost = editTextTextPersonName5.text.toString()
            val namePost = editTextTextPersonName3.text.toString()

            val post_id = UUID.randomUUID().toString()

            val post = Post(post_id, namePost, random, commentPost, mutableListOf(), User.username, User.image_profile)
            val helper = FirebaseHelper()
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    helper.addPost(post, topicPost)
                }
            }

        })
        finish()
    }

    fun uploadImage(v: View?) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && data != null && resultCode == Activity.RESULT_OK) {
            uri = data.data
            imageButton3.setImageURI(uri)
        }
    }

}






