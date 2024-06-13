package com.example.sel.base.model

data class RequestRegister(
    var name: String ? = null,
    var password : String ? = null,
    var password_confirmation: String ? = null,
    var email: String ? = null,
    )

