package com.example.login_example

import java.io.Serializable

data class Post(val id : String, val nome: String, val image_path: String , val text: String, val commenti_ids: MutableList<String>, val username:String, val userImage:String) : Serializable {
    constructor() : this("","","","", mutableListOf(), "", ""){}

}
