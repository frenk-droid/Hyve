package com.example.login_example




import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.prova_download.*


class HomepageFragment : Fragment(R.layout.prova_download) {
    private var firebase= Firebase.database.reference
    val topic = mutableListOf<Topic>() //change location of this variable
    lateinit var fragmentContext: Context
    lateinit var img: ImageView

    lateinit var User: user


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    //createTopic(requireContext())
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         img = view.findViewById<View>(R.id.imageView5) as ImageView

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(fragmentContext is MainActivity2)
            User = (fragmentContext as MainActivity2).get()
        val ONE_MEGABYTE = (1024 * 1024).toLong()
        FirebaseStorage.getInstance().getReferenceFromUrl("gs://hyve-d0e7b.appspot.com/profile_images/${User.image_profile}").getBytes(
                ONE_MEGABYTE
        )
                .addOnSuccessListener { bytes ->
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    img.setImageBitmap(bmp)
                }
                .addOnFailureListener { exception ->
                    exception.message?.let { Log.d("Error", it) }
                }
    }



    }







