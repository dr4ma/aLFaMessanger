package com.example.alfamessanger.domain.models

data class NotificationsMainModel(
    var userId : String = "",
    var written : Boolean = false,
    var notification_list: Map<String, NotificationModel> = hashMapOf()
    )
