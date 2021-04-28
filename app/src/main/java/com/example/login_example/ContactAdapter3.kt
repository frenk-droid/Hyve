package com.example.login_example

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactAdapter3(private val context: Context, private val comments : List<Comment>)
    : RecyclerView.Adapter<ContactAdapter3.ViewHolder>() {

    private val TAG = "ContactAdapter3"

    // Usually involves inflating a layout from XML and returning the holder - THIS IS EXPENSIVE
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        println("okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkokkokkokokokokokkkooooooooo3")

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false))

    }

    // Returns the total count of items in the list
    override fun getItemCount() = comments.size

    // Involves populating data into the item through holder - NOT expensive
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder at position $position")
        val comment = comments[position]
        holder.bind(comment)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(comment:Comment) {

            itemView.tvText.setText("${comment.nome_utente} : ${comment.text} ")
          //  var storage= FirebaseStorage.getInstance()
                /*var imageRef = storage.getReferenceFromUrl("gs://hyve-d0e7b.appspot.com/post_images/${post.image_path}")


            val ONE_MEGABYTE = (1024 * 1024).toLong()
            imageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener { bytes ->
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    itemView.ivProfile.setImageBitmap(bmp)

                }
                .addOnFailureListener { exception ->

                    exception.message?.let { Log.d("Error", it) }
                }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, CommentsActivity::class.java)

                intent.putExtra("post_nome", post.nome)
                intent.putExtra("post_image", post.image_path)
                intent.putExtra("post_id", post.id)
                intent.putExtra("post_text", post.text)
                intent.putExtra("commenti_list", ArrayList(post.commenti_ids))

                itemView.context.startActivity(intent)  // poi si passa alla schermata dei commenti

            }*/
        }
    }
}