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
        val commentPost = editTextTextPersonName5.text.toString()
        val namePost = editTextTextPersonName3.text.toString()
        val topicPost = editTextTextPersonName4.text.toString()
        val topics = mutableListOf<Topic>()
        val post_ids = mutableListOf<String>()


    }

}






