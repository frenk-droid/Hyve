package com.example.login_example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLogin.setOnClickListener{
            var intent = Intent( this, HomepageActivity::class.java)
            startActivity(intent)
        }



        button2.setOnClickListener{
            var intent= Intent(this, RegisterPage::class.java )
            startActivity(intent)
        }


        }

    }
