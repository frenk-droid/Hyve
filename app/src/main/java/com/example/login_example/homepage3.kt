package com.example.login_example

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class homepage3 : Fragment(R.layout.fragment_homepage3) {
    private var firebase= Firebase.database.reference
    val topic = mutableListOf<Topic>()
    lateinit var fragmentContext: Context
    lateinit var img: ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var User: user
    lateinit var usertext: TextView
    var bmp: Bitmap?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        img = view.findViewById<View>(R.id.imageView6) as ImageView
        usertext = view.findViewById(R.id.username) as TextView
        recyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(fragmentContext is MainActivity2)
            User = (fragmentContext as MainActivity2).get()
        usertext.text= User.username
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {

                img.setImageBitmap(FirebaseHelper().getImage("gs://hyve-d0e7b.appspot.com/profile_images/${User.image_profile}"))
                val post= FirebaseHelper().getPostHistory(User)
                System.out.println("topic= ${topic}")
                recyclerView.adapter = ContactAdapter5(fragmentContext, post)
                recyclerView.layoutManager = LinearLayoutManager(fragmentContext)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(!img.equals(null))
            img.setImageBitmap(bmp)
    }
}
