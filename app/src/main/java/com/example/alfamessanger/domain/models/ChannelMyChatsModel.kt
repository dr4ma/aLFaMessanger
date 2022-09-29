package com.example.alfamessanger.domain.models

import java.io.Serializable

data class ChannelMyChatsModel(
    var channelId : String = "",
    var adminUid : String = "",
    var name : String = "",
    var type : String = "",
    var iconUrl : String = "",
    var lastNews : String = "",
    var timeNews : Any = ""
) : Serializable