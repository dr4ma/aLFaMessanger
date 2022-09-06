package com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views

interface MessageView {
    val id : String
    val from : String
    val time_stamp : Any
    val file_url : String
    var text : String
    val width : Int
    val height : Int
    val duration : Int
    val size : Double

    companion object{
        val MESSAGE_IMAGE : Int get() = 0
        val MESSAGE_TEXT : Int get() = 1
        val MESSAGE_VOICE : Int get() = 2
        val MESSAGE_FILE : Int get() = 3
    }

    fun getTypeView() : Int
}