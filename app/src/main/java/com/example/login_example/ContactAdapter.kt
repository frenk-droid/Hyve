package com.example.login_example

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_contact.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactAdapter(private val context: Context, private val topics : List<Topic>, private val User:user) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private var isPresent = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false))
    }

    override fun getItemCount() = topics.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = topics[position]
        holder.bind(topic)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                val list = FirebaseHelper().getTopic(User)
                isPresent = list.contains(topic.id)
                System.out.println("isPresent value in onBind ${isPresent}")
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(topic: Topic) {
            itemView.textView.text = topic.text
            itemView.titolo.text = topic.nome
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    itemView.imageView8.setImageBitmap(FirebaseHelper().getImage("gs://hyve-d0e7b.appspot.com/topic_images/${topic.image_path}"))
                    val list = FirebaseHelper().getTopic(User)
                    if (list.contains(topic.id)) {
                        itemView.button.visibility = View.INVISIBLE
                        itemView.button2.visibility = View.VISIBLE
                    } else {
                        itemView.button.visibility = View.VISIBLE
                        itemView.button2.visibility = View.INVISIBLE
                    }
                }
                itemView.button.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        withContext(Dispatchers.Main) {
                            FirebaseHelper().addTopic(User, topic)
                        }
                    }
                    itemView.button.visibility = View.INVISIBLE
                    itemView.button2.visibility = View.VISIBLE
                }

                itemView.button2.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        withContext(Dispatchers.Main) {
                            FirebaseHelper().deleteTopic(User, topic)
                        }
                    }
                    itemView.button.visibility = View.VISIBLE
                    itemView.button2.visibility = View.INVISIBLE
                }

                }
            }
        }
    }
