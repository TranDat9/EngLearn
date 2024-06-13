package com.example.sel.base.model

data class ResponseLogin(
    var data: Data? = null,
    var errors: Any? = Any(),
    var message: String? = null,
    var status: Boolean? = false
)

data class Data(
    var access_token: String? = null,
    var expires_at: String? = null,
    var token_type: String? = null,
    var user: User? = User()
)

data class User(
    var created_at: String? = null,
    var email: String? = null,
    var email_verified_at: Any? = Any(),
    var id: Int? = 0,
    var name: String? = null,
    var role: String? = null,
    var updated_at: String? = null
)