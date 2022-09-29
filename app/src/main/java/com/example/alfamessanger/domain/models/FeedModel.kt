package com.example.alfamessanger.domain.models

import java.io.Serializable

data class FeedModel(
    var channelId : String = "",
    var adminId : String = "",
    var feedId : String = "",
    var iconUri : String = "",
    var fileUri : String = "",
    var text : String = "",
    var time : Any = "",
    var nameFile : String = "",
    var nameAdmin : String = "",
    var heightImage : Int = 0,
    var sizeFile : Double = 0.0
) : Serializable
{
    override fun equals(other: Any?): Boolean {
        return (other as FeedModel).feedId == feedId
    }
}