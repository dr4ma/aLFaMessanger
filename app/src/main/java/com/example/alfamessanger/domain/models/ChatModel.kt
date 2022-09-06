package com.example.alfamessanger.domain.models

import java.io.Serializable

data class ChatModel(
    val from : String = "",
    val iconUrl : String = "",
    val id : String = "",
    val text : String = "",
    val time_stamp : Any = "",
    val name : String = ""
) : Serializable
