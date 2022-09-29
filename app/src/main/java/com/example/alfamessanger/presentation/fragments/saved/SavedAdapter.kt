package com.example.alfamessanger.presentation.fragments.saved

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.ChatModel
import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.presentation.PopupWindowApp
import com.example.alfamessanger.utills.*
import kotlinx.android.synthetic.main.item_saved_recycler.view.*
import java.util.ArrayList

class SavedAdapter : RecyclerView.Adapter<SavedAdapter.SavedViewHolder>() {

    private var mListSaved = mutableListOf<SavedPhotoModel>()

    inner class SavedViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.saved_image
        val main : ConstraintLayout = itemView.main_saved
        val background : View = itemView.background_saved_photo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_recycler, parent, false)
        return SavedViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {
        holder.main.layoutParams.width = (getDisplayMetrics().widthPixels / 3)
        holder.main.layoutParams.height = (getDisplayMetrics().widthPixels / 3)
        holder.image.downloadAndSetImage(false, mListSaved[position].image_url)

        holder.image.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelableArrayList(KEY_CHAT_IMAGE, mListSaved as ArrayList<SavedPhotoModel>)
            bundle.putInt(KEY_POSITION, position)
            APP_ACTIVITY_MAIN.mNavController.navigate(
                R.id.action_savedFragment_to_imageFullScreenFragment,
                bundle
            )
        }
    }

    override fun getItemCount(): Int {
        return mListSaved.size
    }

    fun setList(list: MutableList<SavedPhotoModel>) {
        mListSaved = list
        notifyItemChanged(0, mListSaved.size)
//        if (!mListChat.contains(item)) {
//            mListChat.add(item)
//            mListChat.sortBy { it.time_stamp.toString() }
//            notifyItemInserted(mListChat.size)
//        }
    }
    fun clearList() {
        mListSaved.clear()
        notifyDataSetChanged()
    }
}