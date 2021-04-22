package com.example.login_example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register_page2.*
import kotlinx.coroutines.NonCancellable.children

//inserire sottolineature rosse per identificare se email o password è errata

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var auth1: FirebaseAuth
    private var firebase= Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLogin.setOnClickListener{

            val password = editTextTextPassword.text.toString()
            val email = editTextTextPersonName2.text.toString()
            auth= Firebase.auth
            val uid= auth.uid.toString()


            auth.signInWithEmailAndPassword(email, password)

                .addOnCompleteListener(this) { task ->
                    println("STArt3")
                    if (task.isSuccessful) {
                        println("STArt4")

                        var intent = Intent( this, HomepageActivity::class.java)
                        loadhomePage(intent, uid)

                    }

                    else {
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        println("STArt5")
                    }
                }
        }

        button.setOnClickListener{
            auth1=Firebase.auth
            auth1.signInWithEmailAndPassword("s@gmail.com", "Ciao12345" )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        //val user = auth.currentUser
                        var intent1 = Intent( this, HomepageActivity::class.java)
                        startActivity(intent1)
                    }

                    else {
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        button2.setOnClickListener{
            var intent= Intent(this, RegisterPage::class.java )
            startActivity(intent)
        }




        }


    private fun loadhomePage ( intent :Intent, uid: String) : user? { // bug: se si torna indietro dopo il login l'uid non viene aggiornato e viene caricata l'immagine del profilo precedente

        var User :user ?= null
        val rootRef = firebase.child("users").child(uid)


        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                User = dataSnapshot.getValue<user>()
                Log.d("User value ondatachange", "${User?.image_profile}")
                intent.putExtra("USER_DATA", User)
                startActivity(intent)

                
        } } )

        Log.d("User value", "${User?.image_profile}")
        return User
    }


}
