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
    suspend fun getPost(user:User): MutableList<Post>{
        val posts=mutableListOf<String>()
        val list= mutableListOf<String>()
        val posts_finale= mutableListOf<Post>()

        for(t in Firebase.database.reference.child("users").child(user.user_id!!).child("topic_ids").getSnapshotValue().children) // scarico id dei topic
            list.add(t.getValue<String>()!!)
        //System.out.println(list)

        for(t in list)
            for(i in Firebase.database.reference.child("topics").child(t).child("posts_ids").getSnapshotValue().children)
                posts.add(i.getValue<String>()!!)
        //System.out.println(posts)

        for(t in posts)
            posts_finale.add(Firebase.database.reference.child("posts").child(t).getSnapshotValue().getValue<Post>()!!)
        //System.out.println(posts_finale)
        return posts_finale
    }

    suspend fun getTopic():MutableList<Topic>{
        var list= mutableListOf<Topic>()

        for(i in Firebase.database.reference.child("topics").getSnapshotValue().children)
            list.add(i.getValue<Topic>()!!)
        return list
    }

    suspend fun getTopicName():MutableList<String>{
        val names= mutableListOf<String>()
        val list= getTopic()

        for(t in list)
            names.add(t.nome)
        return names
    }

    suspend fun getTopic(user: User):MutableList<String>{
        var list= mutableListOf<String>()

        for(i in Firebase.database.reference.child("users").child(user.user_id!!).child("topic_ids").getSnapshotValue().children)
            list.add(i.getValue<String>()!!)
        //System.out.println("list topic_ids ${list}")
        return list
    }

    suspend fun getPost():MutableList<Post>{
        var list= mutableListOf<Post>()

        for(i in Firebase.database.reference.child("posts").getSnapshotValue().children)
            list.add(i.getValue<Post>()!!)
        //System.out.println("list posts_ids ${list}")
        return list
    }


    suspend fun getTopicHistory(user: User):MutableList<String>{
        var list= getTopic(user)
        val list_finale= mutableListOf<String>()

        for(t in list){
            for(i in Firebase.database.reference.child("topics").getSnapshotValue().children)
                if(i.getValue<Topic>()!!.id==t)
                    list_finale.add(i.getValue<Topic>()!!.id)
        }
        //System.out.println("list_finale= ${list_finale}")
        return list_finale
    }

    suspend fun getUtente(uid : String) : User{
        val utente= Firebase.database.reference.child("users").child(uid).getSnapshotValue().getValue<User>()!!
        return utente
    }

    suspend fun addTopic(user : User, topic: Topic){
        Firebase.database.reference.database.getReference("topics").child(topic.id).setValue(topic)
        var utente= Firebase.database.reference.child("users").child(user.user_id!!).getSnapshotValue().getValue<User>()!!
        var duplicate= false
        for(t in utente.topic_ids)
            if(topic.equals(t))
                duplicate=true
        if(duplicate.equals(false))
            utente.topic_ids.add(topic.id)
        Firebase.database.reference.database.getReference("users").child(user.user_id!!).setValue(utente)
    }

    suspend fun deleteTopic(user: User, topic: Topic){
        //System.out.println("Funzione chiamata!")
        //System.out.println("Valore topic id= ${topic.id}")
        //System.out.println("Valore User id= ${user.user_id!!}")
        var list= getTopicHistory(user)
        var list_finale= mutableListOf<String>()
        for(t in list)
            if(t != topic.id)
                list_finale.add(t)

        Firebase.database.reference.child("users").child(user.user_id!!).child("topic_ids").setValue(list_finale)

    }


    suspend fun addPost(post: Post, topic: String){
        var posts=mutableListOf<String>()
        var topic_id:Topic?=null
        Firebase.database.reference.database.getReference("posts").child(post.id).setValue(post)
        for(t in Firebase.database.reference.child("topics").getSnapshotValue().children){

            if(t.getValue<Topic>()!!.nome == topic){
                posts.addAll(t.getValue<Topic>()!!.posts_ids)
                topic_id= t.getValue<Topic>()!!

            }

        }
        topic_id!!.posts_ids.add(post.id)
        Firebase.database.reference.database.getReference("topics").child(topic_id!!.id).setValue(topic_id)

    }

    suspend fun getPostHistory(user: User):MutableList<Post>{
        var list_post= getPost()
        val list_finale= mutableListOf<Post>()

        for(post in list_post)
            if (post.username.equals(user.username))
                list_finale.add(post)
        //System.out.println("list_finale history posts= ${list_finale}")
        return list_finale
    }


    suspend fun addComment(comment: Comment, post: Post){
        val list= mutableListOf<String>()
        for(i in Firebase.database.reference.child("posts").child(post.id).child("commenti_ids").getSnapshotValue().children)
            list.add(i.getValue<String>()!!)
        list.add(comment.id)
        Firebase.database.reference.database.getReference("posts").child(post.id).child("commenti_ids").setValue(list)
        Firebase.database.reference.database.getReference("comments").child(comment.id).setValue(comment)
    }

    suspend fun getComment(post: Post): MutableList<Comment>{
        val list= mutableListOf<Comment>()
        val final_list= mutableListOf<Comment>()
        val ids= mutableListOf<String>()
        for(i in Firebase.database.reference.child("posts").child(post.id).child("commenti_ids").getSnapshotValue().children)
            ids.add(i.getValue<String>()!!)
        //System.out.println("ids= ${ids}")

        //System.out.println("lista= ${list}")

        for(t in ids)
            for(i in Firebase.database.reference.database.getReference("comments").getSnapshotValue().children) {
                var commento= i.getValue<Comment>()!!
                //System.out.println("outside if${commento}")

                if ( commento.id== t) {
                    //System.out.println("inside if${i.getValue<Comment>().toString()}")
                    final_list.add(commento)
                }
            }
        return final_list
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
