package com.example.login_example

import java.io.Serializable

class user : Serializable {

    var username= ""
    var password=""
    var email= ""
    var user_id: String? = ""
    var image_profile= "default.png"

    constructor(){}

    constructor(id: String?, user: String, pass : String, mail : String, img:String){
        username= user
        password= pass
        email= mail
        user_id= id
        image_profile= img

    }

    constructor(id: String?, user: String, pass : String, mail : String){
        username= user
        password= pass
        email= mail
        user_id= id


    }

}