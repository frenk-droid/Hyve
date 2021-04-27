package com.example.login_example

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_contact.view.*


class ContactAdapter2(private val context: Context, private val posts : List<Post>)
    : RecyclerView.Adapter<ContactAdapter2.ViewHolder>() {

    private val TAG = "ContactAdapter2"

    // Usually involves inflating a layout from XML and returning the holder - THIS IS EXPENSIVE
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        println("okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkokkokkokokokokokkkooooooooo")

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false))

    }

    // Returns the total count of items in the list
    override fun getItemCount() = posts.size

    // Involves populating data into the item through holder - NOT expensive
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder at position $position")
        val post = posts[position]
        holder.bind(post)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(post:Post) {

            itemView.tvText.text = post.text
            var storage= FirebaseStorage.getInstance()
            var imageRef = storage.getReferenceFromUrl("gs://hyve-d0e7b.appspot.com/post_images/${post.image_path}")


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
                /*val intent = Intent(itemView.context, PostActivity::class.java)

                intent.putExtra("topic_nome", topic.nome)
                intent.putExtra("topic_image", topic.image_path)
                intent.putExtra("topic_id", topic.id)
                intent.putExtra("topic_text", topic.text)
                intent.putExtra("topic_list", ArrayList(topic.posts_ids))

                itemView.context.startActivity(intent)*/  // poi si passa alla schermata dei commenti

            }
        }
    }
}