package com.example.alfamessanger.domain.models

import java.io.Serializable

data class MessageModel(
    var id : String = "",
    var text: String = "",
    var type: String = "",
    var from: String = "",
    var time_stamp: Any = "",
    var image_url: String = "",
    var width : Int = 0,
    var height : Int = 0,
    var duration : Int = 0,
    var size : Double = 0.0
) : Serializable
{
    override fun equals(other: Any?): Boolean {
        return (other as MessageModel).id == id
    }
}

