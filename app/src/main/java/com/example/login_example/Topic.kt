package com.example.login_example

import java.io.Serializable

data class Topic(val id : String, val nome: String, val image_path: String , val text: String, val posts_ids: MutableList<String>) : Serializable {
    constructor() : this("","","","", mutableListOf()){}

}

