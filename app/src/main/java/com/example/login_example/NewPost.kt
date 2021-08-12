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
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.activity_register_page2.*
import kotlinx.android.synthetic.main.prova_download.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.*

class NewPost : AppCompatActivity() {
    private lateinit var task: StorageTask<UploadTask.TaskSnapshot>
    private lateinit var random: String;
    private lateinit var topicc: Topic
    private lateinit var User: user
    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
        User = intent.getSerializableExtra("user-data") as user
    }

    fun uploadPost(v: View?) {
        random = UUID.randomUUID().toString()
        var task =
            FirebaseStorage.getInstance().getReference("topic_images/${random}").putFile(uri!!)
                .addOnSuccessListener {
                    Log.d("Yes", "Image Uploaded")
                }
                .addOnFailureListener {
                    Log.d("no", "Failed to upload image to storage")
                }
        Tasks.whenAll(task).addOnCompleteListener(OnCompleteListener { task: Task<Void?> ->
            val commentPost = editTextTextPersonName5.text.toString()
            val namePost = editTextTextPersonName3.text.toString()
            val topicPost = editTextTextPersonName4.text.toString()
            val post_id = UUID.randomUUID().toString()

            val post = Post(
                post_id,
                namePost,
                random,
                commentPost,
                mutableListOf(),
                User.username,
                User.image_profile
            )
            var helper = FirebaseHelper()
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    helper.addPost(post, topicPost)
                }
            }

        })
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

    lateinit var list: List<Topic>
    fun getTopics() {
        var helper = FirebaseHelper()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                var list = helper.getTopic()

            }
        }

    }

    val spinner = findViewById<Spinner>(R.id.topic_spinner)
    val dataAdapter = ArrayAdapter(
        this,
        android.R.layout.simple_spinner_item,
        list).also { adapter ->
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    spinner.onItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>,
                                    view: View, position: Int, id: Long) {
            Toast.makeText(this@MainActivity,
                getString(R.string.selected_item) + " " +
                        "" + languages[position], Toast.LENGTH_SHORT).show()
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // write code to perform some action

     x



}






