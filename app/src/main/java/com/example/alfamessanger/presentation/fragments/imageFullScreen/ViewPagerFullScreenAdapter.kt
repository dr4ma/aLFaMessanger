package com.example.alfamessanger.presentation.fragments.imageFullScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.ChatModel
import com.example.alfamessanger.domain.models.SavedPhotoModel
import com.example.alfamessanger.presentation.fragments.mychats.MyChatsAdapter
import com.example.alfamessanger.utills.downloadAndSetImage
import com.example.alfamessanger.utills.showToast
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.item_full_screen_image.view.*

class ViewPagerFullScreenAdapter(
    private val listener: OnItemClickListenerFullScreen,
    private val onSuccess: (SavedPhotoModel, position : Int) -> Unit
) : RecyclerView.Adapter<ViewPagerFullScreenAdapter.PagerViewHolder>() {

    private var mListPhotos = mutableListOf<SavedPhotoModel>()

    inner class PagerViewHolder(view: View) : RecyclerView.ViewHolder(view),  View.OnClickListener {
        val image : PhotoView = view.full_screen_image

        override fun onClick(p0: View?) {
            listener.onItemClickAdapter()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_full_screen_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.image.setOnClickListener(holder)
    }

    override fun onViewAttachedToWindow(holder: PagerViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.image.downloadAndSetImage(false, mListPhotos[holder.adapterPosition].image_url)
        onSuccess(mListPhotos[holder.adapterPosition], holder.adapterPosition)
    }

    override fun onViewDetachedFromWindow(holder: PagerViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.image.scaleType = ImageView.ScaleType.FIT_CENTER
    }

    override fun getItemCount(): Int {
        return mListPhotos.size
    }

    fun setList(list: MutableList<SavedPhotoModel>) {
        mListPhotos = list
        notifyItemRangeChanged(0, mListPhotos.size)
    }

    interface OnItemClickListenerFullScreen {
        fun onItemClickAdapter()
    }
}