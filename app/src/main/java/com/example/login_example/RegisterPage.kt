package com.example.login_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register_page2.*
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page2)

        // da implementare l' inserimento dell'immagine

        button3.setOnClickListener {

            var username = UsernameField.text.toString()
            var password = passField.text.toString()
            var email = emailField.text.toString()
            auth = Firebase.auth

            if (validateCredentials(username, password, email)) { // per ora è inutile... le credenziali sono controllate da firebase in automatico durante l'immissione dei dati, implementare questa funzione per un inserimento corrento dello username
                Log.d("mex", "Username: ${username}, Password: ${password}, email: ${email}")

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("inserimento database", "createUserWithEmail:success")
                            finish()
                        }

                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("inserimento database", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()

                        }

                    }
            }
            else
                Log.d("ERROR", "credenziali non valide")
        }
    }
}

    fun validateCredentials(user: String, pass: String, email: String): Boolean {

        return !(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(user))

    }



