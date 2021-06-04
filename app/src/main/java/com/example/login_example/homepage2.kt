package com.example.login_example

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class homepage2 : Fragment(R.layout.activity_post) {
    lateinit var fragmentContext: Context
    lateinit var recyclerView:RecyclerView
    lateinit var img: ImageView
    lateinit var User:user
    var bmp: Bitmap?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        img = view.findViewById<View>(R.id.imageView4) as ImageView
        recyclerView = view.findViewById<View>(R.id.r) as RecyclerView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onResume() {
        super.onResume()
        if(!img.equals(null))
            img.setImageBitmap(bmp)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(fragmentContext is MainActivity2)
            User = (fragmentContext as MainActivity2).get()
        CoroutineScope(IO).launch {
            withContext(Dispatchers.Main) {
                img.setImageBitmap(FirebaseHelper().getImage("gs://hyve-d0e7b.appspot.com/profile_images/${User.image_profile}"))
                val posts= FirebaseHelper().getPost(User)
                recyclerView.adapter = ContactAdapter2(fragmentContext, posts)
                recyclerView.layoutManager = LinearLayoutManager(fragmentContext)
            }
        }
    }
}


