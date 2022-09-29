package com.example.alfamessanger.domain.models

data class NotificationModel(
    var id: String = "",
    var type : String = "",
    var iconUrl: String = "",
    var userFrom : String = "",
    var channelId : String = "",
    var timeStamp : Any = "",
    var userIdFrom : String = ""
)
