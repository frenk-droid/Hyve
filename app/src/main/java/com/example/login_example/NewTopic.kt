package com.example.login_example

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_new_topic.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class NewTopic : AppCompatActivity() {
    private lateinit var random: String
    private lateinit var User: user
    private  var uri : Uri?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_topic)
        User = intent.getSerializableExtra("user-data") as user
    }

    fun uploadTopic(v:View?){
        val nameTopic = editTextTextMultiLine.text.toString()
        val textTopic = editTextTextPersonName.text.toString()
        val topic_id = UUID.randomUUID().toString()

        if (uri != null)
            uploadImageToFirebaseStorage(textTopic, nameTopic, topic_id)
    }

    fun uploadImage(v: View?){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type= "image/*"
        startActivityForResult(intent, 0)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && data!=null && resultCode== Activity.RESULT_OK ) {
            uri = data.data!!
            imageButton2.setImageURI(uri)

        }
    }


    private fun uploadImageToFirebaseStorage(nameTopic: String, textTopic:String, topic_id:String) {
        random= UUID.randomUUID().toString()
        val task= FirebaseStorage.getInstance().getReference("topic_images/${random}").putFile(uri!!)
            .addOnSuccessListener {
                Log.d("Yes", "Image Uploaded")
            }
            .addOnFailureListener {
                Log.d("no", "Failed to upload image to storage")
            }
        Tasks.whenAll(task).addOnCompleteListener(OnCompleteListener { task: Task<Void?> ->
            val topic = Topic(topic_id, nameTopic, random, textTopic, mutableListOf(""))
            val returnIntent = Intent()
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    FirebaseHelper().addTopic(User, topic)
                }
            }
            returnIntent.putExtra("topic-data", topic)
            setResult(RESULT_OK, returnIntent)
            finish()
        })

    }
}