package com.example.login_example

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_contact.view.*
import kotlinx.android.synthetic.main.prova_download.*

class ContactAdapter(private val context: Context, private val topics : List<Topic>)
    : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private val TAG = "ContactAdapter"

    // Usually involves inflating a layout from XML and returning the holder - THIS IS EXPENSIVE
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        println("okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkokkokkokokokokokkkooooooooo")

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false))

    }

    // Returns the total count of items in the list
    override fun getItemCount() = topics.size

    // Involves populating data into the item through holder - NOT expensive
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder at position $position")
        val topic = topics[position]
        holder.bind(topic)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(topic: Topic) {

            itemView.tvText.text = topic.text
            var storage= FirebaseStorage.getInstance()
            var imageRef = storage.getReferenceFromUrl("gs://hyve-d0e7b.appspot.com/topic_images/${topic.image_path}")


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
               val intent = Intent(itemView.context, PostActivity::class.java)
                intent.putExtra("post_data", topic.nome)
                intent.putExtra("post_image", topic.image_path)
                itemView.context.startActivity(intent)

            }
        }
    }
}