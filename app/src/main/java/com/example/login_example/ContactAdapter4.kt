package com.example.login_example

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactAdapter4(private val context: Context, private val comments : List<Comment>) : RecyclerView.Adapter<ContactAdapter4.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false))
    }

    override fun getItemCount() = comments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(comment: Comment) {
            itemView.textView4.text = comment.text
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    itemView.imageView9.setImageBitmap(FirebaseHelper().getImage("gs://hyve-d0e7b.appspot.com/profile_images/${comment.image_path}"))
                }
            }

            }
        }
    }
