package com.example.alfamessanger.presentation.fragments.blackList

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.BlackListUserModel
import com.example.alfamessanger.utills.APP_ACTIVITY_MAIN
import com.example.alfamessanger.utills.OFFLINE
import com.example.alfamessanger.utills.downloadAndSetImage
import com.example.alfamessanger.utills.getStatusUser
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_black_list_recycler.view.*

class BlackListAdapter(private val function:(model : BlackListUserModel) -> Unit) : RecyclerView.Adapter<BlackListAdapter.BlackListViewHolder>(){

    private var mListBlackList = mutableListOf<BlackListUserModel>()

    inner class BlackListViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val icon : CircleImageView = itemView.photo_user_black_list
        val name : TextView = itemView.name_user_black_list
        val status : TextView = itemView.status_user_black_list
        val tittle : TextView = itemView.tittle_black_list
        val chat_btn : ImageView = itemView.go_to_chat_black_list
        val out_of_black_list_btn : ImageView = itemView.out_from_blacklist
        val account: ConstraintLayout = itemView.account_info

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlackListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_black_list_recycler, parent, false)
        return BlackListViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: BlackListViewHolder, position: Int) {
        if (mListBlackList[position].iconUrl != "") {
            holder.icon.downloadAndSetImage(true, mListBlackList[position].iconUrl)
        } else {
            holder.icon.setImageDrawable(
                APP_ACTIVITY_MAIN.resources.getDrawable(
                    R.drawable.default_user,
                    APP_ACTIVITY_MAIN.theme
                )
            )
        }
        holder.name.text = mListBlackList[position].name
        holder.status.text = OFFLINE
        mListBlackList[position].uid?.let {
            getStatusUser(it) { status ->
                holder.status.text = status
            }
        }
        if(mListBlackList[position].tittle == ""){
            holder.tittle.text = APP_ACTIVITY_MAIN.getString(R.string.tittle_null)
            holder.tittle.setTypeface(null, Typeface.ITALIC)
            holder.tittle.setTextColor(APP_ACTIVITY_MAIN.resources.getColor(R.color.statusColor))
        }
        else{
            holder.tittle.text = mListBlackList[position].tittle
        }
        holder.out_of_black_list_btn.setOnClickListener {
            function(mListBlackList[position])
        }
        holder.chat_btn.setOnClickListener {
            BlackListFragment.goToChat(mListBlackList[position])
        }
        holder.icon.setOnClickListener {
            BlackListFragment.goToProfile(mListBlackList[position])
        }
    }

    override fun getItemCount(): Int {
        return mListBlackList.size
    }

    fun setList(list: MutableList<BlackListUserModel>) {
        mListBlackList.clear()
        mListBlackList.addAll(list)
        notifyDataSetChanged()
    }

    fun removeFromList(model : BlackListUserModel) {
        if (mListBlackList.contains(model)) {
            mListBlackList.remove(model)
            notifyDataSetChanged()
        }
    }
}