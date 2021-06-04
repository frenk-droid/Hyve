 package com.example.login_example


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register_page2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*


 class RegisterPage : AppCompatActivity() {
     private var auth= Firebase.auth
     val topic = mutableListOf<Topic>()

     var uri: Uri? = null
     var ready=false

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_register_page2)
     }

     fun signUp(v: View?) {
         val username = UsernameField.text.toString()
         val password = passField.text.toString()
         val email = emailField.text.toString()
          var random: String?=null
         if(validateCredentials(username,password,email)) {
             auth.createUserWithEmailAndPassword(email, password)
                     .addOnCompleteListener(this) { task ->
                         CoroutineScope(Dispatchers.IO).launch {
                             withContext(Dispatchers.Main) {
                                 if (uri != null)
                                     random= uploadImageToFirebaseStorage()
                                 val user_uid = auth.currentUser!!.uid.toString()
                                 val utente: user
                                 if (random != null)
                                     utente = user(user_uid, username, password, email, random!!)
                                 else
                                     utente = user(user_uid, username, password, email)
                                 FirebaseDatabase.getInstance().getReference("users").child(user_uid).setValue(utente)
                                 loadLoginPage()
                             }
                         }
                     }
         }
         else
             Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show()
     }

     fun validateCredentials(user: String, pass: String, email: String): Boolean {
         return !(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(user))
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
             imageButton.setImageURI(uri)
         }
     }

     suspend fun uploadImageToFirebaseStorage():String {

         val random = UUID.randomUUID().toString()
         val filename = "profile_images/${random}"
         val ref = FirebaseStorage.getInstance().getReference(filename)
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
                 .await()
            return random
     }

     private fun loadLoginPage(){
         val intent = Intent(this, Login::class.java)
         startActivity(intent)
         finish()
     }
 }







