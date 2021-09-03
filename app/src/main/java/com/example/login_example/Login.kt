package com.example.login_example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun register(v:View?){
        val intent= Intent(this, RegisterPage::class.java )
        startActivity(intent)
    }

   fun Login(v:View?) {
       val password = editTextTextPassword.text.toString()
       val email = editTextTextPersonName2.text.toString()
       val auth = Firebase.auth
       val uid = auth.uid.toString()

       if(validateCredentials(password,email)) {
           auth.signInWithEmailAndPassword(email, password)
               .addOnCompleteListener(this) { task ->
                   if (task.isSuccessful) {
                       loadhomePage(uid)
                   } else
                       Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                           .show()
               }
       }
       else
           Toast.makeText(baseContext, "Insert credentials", Toast.LENGTH_SHORT)
               .show()
   }

    fun validateCredentials( pass: String, email: String): Boolean {
        return !(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass))
    }

    private fun loadhomePage(uid: String) {

        val intent = Intent( this, BaseActivity::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                val user= FirebaseHelper().getUtente(uid)
                intent.putExtra("USER_DATA", user)
                startActivity(intent)
            }
        }
    }
}
