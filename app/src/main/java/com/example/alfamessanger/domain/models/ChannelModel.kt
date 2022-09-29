package com.example.alfamessanger.domain.models

import java.io.Serializable

data class ChannelModel(
    var channelId : String = "",
    var adminUid : String = "",
    var name : String = "",
    var description : String = "",
    var type : String = "",
    var iconUrl : String = "",
    var countSubs : Int = 0,
    var feed: Map<String, FeedModel> = hashMapOf(),
    var subscribers: Map<String, UserModel> = hashMapOf()
) : Serializable
