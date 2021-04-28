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
    private lateinit var random: String;
    var uri: Uri? = null
    val storage = FirebaseStorage.getInstance()
    var filename: String? = null
    var namePost:String?= null
    var topicc :Topic?= null
    var passed_topic:Topic?=null
    var post : Post?=null
    var task: StorageTask<UploadTask.TaskSnapshot>?= null
    private var firebase = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        // setContentView(R.layout.activity_new_post2)

        button7.setOnClickListener {
           val textPost = "Provaaaa"
             namePost = editTextTextPersonName3.text.toString()
            var topics = mutableListOf<Topic>()
            var post_ids= mutableListOf<String>()

            val name: String? =  intent.getSerializableExtra("topic_nome") as? String
            val img_path: String? =  intent.getSerializableExtra("topic_image") as? String

            val topic_id: String? =  intent.getSerializableExtra("topic_id") as? String
            val topic_text: String? =  intent.getSerializableExtra("topic_text") as? String
            val topic_list = intent.getParcelableArrayListExtra<Parcelable>("topic_list") as ArrayList<String>
            passed_topic= Topic(topic_id!!, name!!, img_path!!, topic_text!!, topic_list)


            firebase.child("topics").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    for (data in snapshot.children)
                        topics.add(data.getValue<Topic>()!!)
                    for(topic in topics) {
                       if (topic.nome.equals(passed_topic!!.nome)) {
                           post_ids.addAll(topic.posts_ids)
                           topicc = topic

                       }
                    }

                    if (uri != null) {
                        uploadImageToFirebaseStorage()
                    }

                    Tasks.whenAll(task!!).addOnCompleteListener(OnCompleteListener { task: Task<Void?> ->


                    var post_id = UUID.randomUUID().toString()
                    post_ids.add(post_id)
                    post= Post(post_id, namePost!!, random, textPost, mutableListOf(""))
                    var reference = firebase.database.getReference("posts")
                    reference.child(post_id).setValue(post)
                    reference = firebase.database.getReference("topics")
                    var dr  = reference.child(topicc!!.id)
                    val newData= Topic(topicc!!.id, topicc!!.nome, topicc!!.image_path, topicc!!.text, post_ids)
                    dr.setValue(newData)




                    val returnIntent = Intent()
                    returnIntent.putExtra("image_path", post!!.image_path)
                    returnIntent.putExtra("nome", post!!.nome)
                    returnIntent.putExtra("id", post!!.id)
                    returnIntent.putExtra("text",post!!.text)
                    //returnIntent.putExtra("post_list", ArrayList(post!!.commenti_ids)) // non serve perchè all'inizio non ci saranno mai commenti di altre persone
                    setResult(RESULT_OK, returnIntent)
                    finish()
                    }  )

                }
            })






        }
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
        filename = "post_images/${random}"

        val ref = storage.getReference(filename!!)
         task= ref.putFile(uri!!)
            .addOnSuccessListener {
                Log.d("ok", "Successfully uploaded image")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("mm", "File Location: $it")

                }
            }
            .addOnFailureListener {
                Log.d("no", "Failed to upload image to storage")
            }




        }
    }







