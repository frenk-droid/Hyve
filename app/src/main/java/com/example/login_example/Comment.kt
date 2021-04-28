package com.example.login_example

import java.io.Serializable

data class Comment(val id : String, val id_utente: String, val nome_utente: String, val text: String) : Serializable {
    constructor() : this("","", "",""){}

}