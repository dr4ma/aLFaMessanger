package com.example.alfamessanger.domain.models

import java.io.Serializable

data class FeedNewsModel(
    var adminId : String = "",
    var feedId : String = "",
    var iconUri : String = "",
    var fileUri : String = "",
    var text : String = "",
    var time : Any = "",
    var nameFile : String = "",
    var nameAdmin : String = "",
    var heightImage : Int = 0,
    var sizeFile : Double = 0.0,
    var channelName : String = "",
    var channelId : String = "",
    var channelIcon : String = ""
) : Serializable
{
    override fun equals(other: Any?): Boolean {
        return (other as FeedNewsModel).feedId == feedId
    }
}