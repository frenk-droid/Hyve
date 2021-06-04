package com.example.login_example



import android.net.Uri
import java.io.Serializable

data class Post(val id : String, val nome: String, val image_path: String , val text: String, val commenti_ids: MutableList<String>) : Serializable {
    constructor() : this("","","","", mutableListOf()){}

}
