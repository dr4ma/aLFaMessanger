package com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views

class ViewVoiceMessage(
    override val id: String,
    override val from: String,
    override val time_stamp: Any,
    override val file_url: String,
    override var text: String = "",
    override val width: Int = 0,
    override val height: Int = 0,
    override val duration: Int,
    override val size: Double = 0.0
) : MessageView {

    override fun getTypeView(): Int {
        return MessageView.MESSAGE_VOICE
    }

    override fun equals(other: Any?): Boolean {
        return (other as MessageView).id == id
    }
}