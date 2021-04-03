package com.example.login_example

class user {

    var username= ""
    var password= ""
    var email= ""
    var user_id: String? = ""

    constructor()

    constructor(id: String?, user: String, pass : String, mail : String){
        username= user
        password= pass
        email= mail
        user_id= id

    }

}