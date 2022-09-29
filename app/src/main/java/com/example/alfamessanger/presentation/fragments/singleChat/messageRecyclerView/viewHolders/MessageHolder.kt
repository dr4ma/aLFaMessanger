package com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.viewHolders

import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views.MessageView

interface MessageHolder {

    fun drawMessage(view: MessageView)
    fun onAttach(view : MessageView)
    fun onDetach()
}