package com.example.sel.screen.user.blog.model

data class UserData(
    val name:String="",
    val email:String="",
    val profileImage:String=""
){
    constructor():this("","","")
}
