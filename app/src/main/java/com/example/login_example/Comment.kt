package com.example.login_example

import java.io.Serializable

data class Comment(val id : String, val nome_utente: String, val image_path: String,  val text: String) : Serializable {
    constructor() : this("","", "", ""){}

}