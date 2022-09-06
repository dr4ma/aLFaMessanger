package com.example.alfamessanger.presentation.fragments.singleChat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.viewHolders.AppHolderFactory
import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.viewHolders.MessageHolder
import com.example.alfamessanger.presentation.fragments.singleChat.messageRecyclerView.views.MessageView
import com.example.alfamessanger.utills.showToast

class SingleChatAdapter(val function: (view: MessageView) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messageList = mutableListOf<MessageView>()
    private var userModel: UserModel = UserModel()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(userModel, parent, viewType) {
            function(it)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageHolder).drawMessage(messageList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return messageList[position].getTypeView()
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolder).onAttach(messageList[holder.adapterPosition])
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolder).onDetach()
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun addToList(model: UserModel, item: MessageView, toBottom: Boolean, onSuccess: () -> Unit) {
        userModel = model
        if (toBottom) {
            if (!messageList.contains(item)) {
                messageList.add(item)
                messageList.sortBy { it.time_stamp.toString() }
                notifyItemInserted(messageList.size)
            }
        } else {
            if (!messageList.contains(item)) {
                messageList.add(item)
                messageList.sortBy { it.time_stamp.toString() }
                notifyItemInserted(0)
            }
        }
        onSuccess()
    }

    fun removeFromList(item: MessageView) {
        if (messageList.contains(item)) {
            val position = messageList.indexOf(item)
            messageList.remove(item)
            notifyItemRemoved(position)
        }
    }

    fun changeItemInList(item: MessageView) {
        run breaking@{
            messageList.forEach {
                if(it.id == item.id){
                    val position = messageList.indexOf(it)
                    messageList[position] = item
                    notifyItemChanged(position)
                    return@breaking
                }
            }
        }
    }
}