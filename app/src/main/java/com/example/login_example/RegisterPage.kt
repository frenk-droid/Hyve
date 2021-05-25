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
import java.util.*


 class RegisterPage : AppCompatActivity() {
     private var auth= Firebase.auth
     private var random: String? = null
     var uri: Uri? = null
     var ready=false

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_register_page2)
     }

     fun signUp(v: View?) {
         var username = UsernameField.text.toString()
         var password = passField.text.toString()
         var email = emailField.text.toString()
         if(validateCredentials(username,password,email)) {
             auth.createUserWithEmailAndPassword(email, password)
                     .addOnCompleteListener(this) { task ->
                         if (uri != null)
                             uploadImageToFirebaseStorage()
                         else
                             ready= true
                       while(!ready){}
                         if (task.isSuccessful) {
                             var user_uid = auth.currentUser.uid.toString()
                             var utente: user
                             if (random != null)
                                 utente = user(user_uid, username, password, email, random!!)
                             else
                                 utente = user(user_uid, username, password, email)
                             FirebaseDatabase.getInstance().getReference("users").child(user_uid).setValue(utente)
                             loadLoginPage()
                         } else
                             Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show()
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

     private fun uploadImageToFirebaseStorage() {

         random = UUID.randomUUID().toString()
         val filename = "profile_images/${random}"
         val ref = FirebaseStorage.getInstance().getReference(filename)
         val task= ref.putFile(uri!!)
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
             ready= false
         })
     }

     private fun loadLoginPage(){
         val intent = Intent(this, Login::class.java)
         startActivity(intent)
         finish()
     }
 }







