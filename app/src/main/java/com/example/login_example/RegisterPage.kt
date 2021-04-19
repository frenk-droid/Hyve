package com.example.login_example


import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register_page2.*
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import android.provider.MediaStore.Audio.Albums.*
import android.provider.MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import java.util.*
import com.google.firebase.storage.FirebaseStorage

class RegisterPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    var uri : Uri? = null;
    val storage = FirebaseStorage.getInstance()

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
                        if (task.isSuccessful) {

                            var database = FirebaseDatabase.getInstance().getReference("users")
                            var user_id= database.push().key
                            var utente = user(user_id, username, password, email)
                            database.child(user_id!!).setValue(utente)
                            finish()
                        }

                        else
                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
            }

            if(uri!=null){
                uploadImageToFirebaseStorage()


            }
            else
                Log.d("ERROR", "credenziali non valide")
        }
    }

    fun validateCredentials(user: String, pass: String, email: String): Boolean {

        return !(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(user))

    }

    fun uploadImage(v: View?){

        val intent = Intent(Intent.ACTION_PICK,  MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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

        val filename = "images/" + UUID.randomUUID().toString()
        val ref = storage.getReference(filename)
        ref.putFile(uri!!)
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






