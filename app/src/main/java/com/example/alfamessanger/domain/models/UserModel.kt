package com.example.alfamessanger.domain.models

import java.io.Serializable

data class UserModel(
    var uid: String? = "",
    var phone: String = "",
    var name: String = "",
    var nickname : String = "",
    var bio : String = "",
    var status : String = "",
    var iconUrl : String = "",
    var tokenNotification : String = ""
) : Serializable
