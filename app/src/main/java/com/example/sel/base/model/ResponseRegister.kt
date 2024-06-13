package com.example.sel.base.model

class ResponseRegister(
    var data: Data? = Data(),
    var errors: Any? = Any(),
    var message: String? = "",
    var status: Boolean? = false
)