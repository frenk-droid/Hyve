 package com.example.login_example


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Albums.*
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_register_page2.*
import java.io.ByteArrayOutputStream
import java.util.*


 class RegisterPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private  var random: String ?= null

    var uri : Uri? = null
    val storage = FirebaseStorage.getInstance()
    var filename : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page2)

        // da implementare l' inserimento dell'immagine e il controllo se l'utente è già presente

        button3.setOnClickListener {

            var username = UsernameField.text.toString()
            var password = passField.text.toString()
            var email = emailField.text.toString()

            auth = Firebase.auth

            if (validateCredentials(username, password, email)) { // per ora è inutile... le credenziali sono controllate da firebase in automatico durante l'immissione dei dati, implementare questa funzione per un inserimento corrento dello usernam

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->

                        if(uri!=null){
                            uploadImageToFirebaseStorage()
                        }

                        if (task.isSuccessful) {

                            var database = FirebaseDatabase.getInstance().getReference("users")
                            var user_uid= auth.currentUser.uid.toString()

                            var utente :user

                            if(random!=null)
                                utente = user(user_uid, username, password, email, random!!)
                            else
                                utente = user(user_uid, username, password, email)

                            database.child(user_uid).setValue(utente)

                        }

                        else
                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
            }

            else
                Log.d("ERROR", "credenziali non valide")
        }
    }

    fun validateCredentials(user: String, pass: String, email: String): Boolean {

        return !(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(user))

    }

    fun uploadImage(v: View?){

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type= "image/*"
        startActivityForResult(intent, 0)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && data!=null && resultCode== Activity.RESULT_OK ) {
            uri = data.data
            imageButton.setImageURI(uri)

        }
    }

    private fun uploadImageToFirebaseStorage() {

        random= UUID.randomUUID().toString()
        filename = "profile_images/${random}"
        val ref = storage.getReference(filename!!)
        ref.putFile(uri!!)
            .addOnSuccessListener {
                Log.d("ok", "Successfully uploaded image")

                val intent = Intent(this, HomepageActivity::class.java)
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("IMG_DATA", random)
                startActivity(intent)

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("mm", "File Location: $it")

                }
            }
            .addOnFailureListener {
                Log.d("no", "Failed to upload image to storage")
            }
    }
    }






