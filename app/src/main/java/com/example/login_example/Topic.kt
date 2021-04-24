package com.example.login_example

import android.net.Uri
import java.io.Serializable

data class Topic(val id : String, val nome: String, val image_path: String , val text: String) : Serializable {
    constructor() : this("","","",""){}
}

