package com.example.login_example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

//inserire sottolineature rosse per identificare se email o password è errata
//controllare eventuali bug
//todo controllo che campi non devono essere null se no l'app crasha

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun register(v:View?){
        var intent= Intent(this, RegisterPage::class.java )
        startActivity(intent)
    }

   fun Login(v:View?) { //check if the user is already in firebase and then call the function loadhomePage

        val password = editTextTextPassword.text.toString()
        val email = editTextTextPersonName2.text.toString()
        val auth= Firebase.auth
        val uid= auth.uid.toString()

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent( this, MainActivity2::class.java)
                        loadhomePage(intent, uid)
                    }
                    else
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
       }

    private fun loadhomePage(intent :Intent, uid: String) {  //download all the user data from firebase and pass them to HomepageActivity

        lateinit var User :user
        //todo togliere la chiamata a firebase in questo punto del codice e mettere tutto in firebaseHelper
        Firebase.database.reference.child("users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                User = dataSnapshot.getValue<user>()!!
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("USER_DATA", User)
                startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Error.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
