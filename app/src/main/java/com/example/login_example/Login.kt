package com.example.login_example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register_page2.*

//inserire sottolineature rosse per identificare se email o password è errata

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLogin.setOnClickListener{

            var password = editTextTextPassword.text.toString()
            var email = editTextTextPersonName2.text.toString()
            auth= Firebase.auth

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser
                        var intent = Intent( this, HomepageActivity::class.java)
                        startActivity(intent)
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



    }
