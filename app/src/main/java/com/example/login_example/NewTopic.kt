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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_new_topic.*
import kotlinx.android.synthetic.main.activity_register_page2.*
import java.util.*


   class NewTopic : AppCompatActivity() {
    private lateinit var random: String;
    var uri : Uri? = null
    val storage = FirebaseStorage.getInstance()
    var filename : String?=null
       var topic_id:String?=null
    private lateinit var textTopic: String
       private lateinit var nameTopic: String
       var database = Firebase.database.reference


       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_topic)

        button5.setOnClickListener {

             textTopic = editTextTextMultiLine.text.toString()
             nameTopic = editTextTextPersonName.text.toString()


            println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh")


             topic_id = UUID.randomUUID().toString()

            if (uri != null) {
                uploadImageToFirebaseStorage()
            }

        }
    }

    fun uploadImage2(v: View?){

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type= "image/*"
        startActivityForResult(intent, 0)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && data!=null && resultCode== Activity.RESULT_OK ) {
            uri = data.data
            imageButton2.setImageURI(uri)

        }
    }


    private fun uploadImageToFirebaseStorage() {

       random= UUID.randomUUID().toString()
       filename = "topic_images/${random}"
        val ref = storage.getReference(filename!!)
        var task= ref.putFile(uri!!)
            .addOnSuccessListener {
                Log.d("ok", "Successfully uploaded image")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("mm", "File Location: $it")


                }
            }
            .addOnFailureListener {
                Log.d("no", "Failed to upload image to storage")
            }

        Tasks.whenAll(task).addOnCompleteListener(OnCompleteListener { task: Task<Void?> ->
            var topic = Topic(topic_id!!, nameTopic, random, textTopic, mutableListOf(""))
            database.child("topics").child(topic_id!!).setValue(topic)
            val returnIntent = Intent()
            returnIntent.putExtra("image_path", topic.image_path)
            returnIntent.putExtra("nome", topic.nome)
            returnIntent.putExtra("id", topic.id)
            returnIntent.putExtra("text", topic.text)
            setResult(RESULT_OK, returnIntent)
            finish()
        })

    }
}