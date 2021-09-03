package com.example.login_example

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ContactAdapter2(private val context: Context, private val posts : List<Post>, private val user:User) : RecyclerView.Adapter<ContactAdapter2.ViewHolder>() {

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
            itemView.editTextTextMultiLine2.setText(post.text)
            itemView.editTextTextMultiLine3.setText(post.nome)
            itemView.nome.text = post.username
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    itemView.imageView6.setImageBitmap(FirebaseHelper().getImage("gs://hyve-d0e7b.appspot.com/post_images/${post.image_path}"))
                    itemView.foto.setImageBitmap(FirebaseHelper().getImage("gs://hyve-d0e7b.appspot.com/profile_images/${post.userImage}"))

                }
            }
            itemView.button4.setOnClickListener {
                val intent = Intent(itemView.context, CommentsActivity::class.java)
                intent.putExtra("post-data", post)
                intent.putExtra("user-data", user)
                itemView.context.startActivity(intent)
            }
        }
    }
}