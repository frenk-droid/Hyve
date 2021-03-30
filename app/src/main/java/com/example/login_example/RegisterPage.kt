package com.example.login_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register_page2.*

class RegisterPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page2)


        button3.setOnClickListener{

            var username = UsernameField.text.toString()
            var password =  passField.text.toString()
            var email    =  emailField.text.toString()

            if(validateCredentials(username!!, password!!, email!!)) { //ricontrollare la validità delle credenziali
                Log.d("mex","Username: ${username}, Password: ${password}, email: ${email}")
                finish()
            }

            else {
                Log.d("mex", "Credenziali non valide")
            }
        }
    }

    fun validateCredentials(user:String, pass:String, email:String): Boolean {

        return !(user == "" || pass=="" || email=="")

    }
}