package com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views

class ViewFileMessage(
    override val id: String,
    override val from: String,
    override val time_stamp: Any,
    override val file_url: String,
    override var text: String,
    override val width: Int,
    override val height: Int,
    override val duration: Int,
    override val size: Double = 0.0
) : MessageView {

    override fun getTypeView(): Int {
        return MessageView.MESSAGE_FILE
    }

    override fun equals(other: Any?): Boolean {
        return (other as MessageView).id == id
    }
}