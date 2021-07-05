package com.example.login_example

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Insets.add
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_contact.view.*
import kotlinx.android.synthetic.main.item_post.view.*


class ContactAdapter2(private val context: Context, private val posts : List<Post>) : RecyclerView.Adapter<ContactAdapter2.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, parent, false))
    }
    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(post:Post) {
            //itemView.tvText.text = post.text
            val ONE_MEGABYTE = (1024 * 1024).toLong()
            FirebaseStorage.getInstance().getReferenceFromUrl("gs://hyve-d0e7b.appspot.com/post_images/${post.image_path}").getBytes(ONE_MEGABYTE)
                    .addOnSuccessListener { bytes ->
                        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        itemView.imageView6.setImageBitmap(bmp)
                    }
                    .addOnFailureListener { exception ->
                        exception.message?.let { Log.d("Error", it) }
                    }
           /* itemView.setOnClickListener {
                val intent = Intent(itemView.context, CommentsActivity::class.java)
                intent.putExtra("post-data", post)
                itemView.context.startActivity(intent)
            }*/
        }
    }
}