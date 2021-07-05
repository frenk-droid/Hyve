package com.example.login_example

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseHelper {
    suspend fun getPost(User:user): MutableList<Post>{
        var posts=mutableListOf<String>()
        var list= mutableListOf<String>()
        val posts_finale= mutableListOf<Post>()

        for(t in Firebase.database.reference.child("users").child(User.user_id!!).child("topic_ids").getSnapshotValue().children){ // scarico id dei topic
            list.add(t.getValue<String>()!!)
        }
        for(t in list)
            for(i in Firebase.database.reference.child("topics").child(t).child("posts_ids").getSnapshotValue().children){
                posts.add(i.getValue<String>()!!)
            }
        for(t in posts)
            posts_finale.add(Firebase.database.reference.child("posts").child(t).getSnapshotValue().getValue<Post>()!!)
        return posts_finale
    }

    suspend fun getTopic():MutableList<Topic>{
        var list= mutableListOf<Topic>()

        for(i in Firebase.database.reference.child("topics").getSnapshotValue().children)
            list.add(i.getValue<Topic>()!!)
        return list
    }

    suspend fun addTopic(User : user, topic: Topic){
        var utente= Firebase.database.reference.child("users").child(User.user_id!!).getSnapshotValue().getValue<user>()!!
        var duplicate= false
        for(t in utente.topic_ids)
            if(topic.equals(t))
                duplicate=true
        if(duplicate.equals(false))
            utente.topic_ids.add(topic.id)
        Firebase.database.reference.database.getReference("users").child(User.user_id!!).setValue(utente)
    }

    suspend fun addPost(post: Post, topic: String){
        var posts=mutableListOf<String>()
        var list= mutableListOf<String>()
         var topic_id:Topic?=null
        Firebase.database.reference.database.getReference("posts").child(post.id).setValue(post)
        for(t in Firebase.database.reference.child("topics").getSnapshotValue().children){

            if(t.getValue<Topic>()!!.nome == topic){
                println("fiiiiiiiiiiind")
                posts.addAll(t.getValue<Topic>()!!.posts_ids)
                topic_id= t.getValue<Topic>()!!

            }

        }
        topic_id!!.posts_ids.add(post.id)
        println(topic_id!!.posts_ids)
        Firebase.database.reference.database.getReference("topics").child(topic_id!!.id).setValue(topic_id)

    }

    suspend fun getImage(reference:String):Bitmap{
        var bmp: Bitmap?=null
        val ONE_MEGABYTE = (1024 * 1024).toLong()
        FirebaseStorage.getInstance().getReferenceFromUrl(reference).getBytes(ONE_MEGABYTE)
                .addOnSuccessListener { bytes ->
                    bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                }
                .addOnFailureListener { exception ->
                    exception.message?.let { Log.d("Error", it) }
                }
                .await()
        return bmp!!
    }
    suspend fun DatabaseReference.getSnapshotValue(): DataSnapshot {
        return withContext(Dispatchers.IO) {
            suspendCoroutine<DataSnapshot> { continuation ->
                addListenerForSingleValueEvent(FValueEventListener(
                        onDataChange = { continuation.resume(it) },
                        onError = { continuation.resumeWithException(it.toException()) }
                ))
            }
        }
    }

    class FValueEventListener(val onDataChange: (DataSnapshot) -> Unit, val onError: (DatabaseError) -> Unit) : ValueEventListener {
        override fun onDataChange(data: DataSnapshot) = onDataChange.invoke(data)
        override fun onCancelled(error: DatabaseError) = onError.invoke(error)
    }

}
