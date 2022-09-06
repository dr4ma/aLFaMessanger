package com.example.alfamessanger.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.example.alfamessanger.R
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.PopupTypesOperationChat

object PopupWindowApp {

    private val popup = PopupWindow(APP_ACTIVITY_MAIN)

    @SuppressLint("UseCompatLoadingForDrawables")
    fun create(type : String, viewUnder : View?, function : (typeOperation : PopupTypesOperationChat) -> Unit){
        when(type){
            POPUP_TEXT_CHAT -> {
                val view = LayoutInflater.from(APP_ACTIVITY_MAIN)
                    .inflate(R.layout.popup_single_chat_text_layout, null)
                popup.contentView = view
                popup.isOutsideTouchable = true
                popup.setBackgroundDrawable(APP_ACTIVITY_MAIN.resources.getDrawable(R.drawable.popup_style))
                popup.width = (getDisplayMetrics().widthPixels / KF_POPUP_WIDTH).toInt()
                popup.height = (getDisplayMetrics().heightPixels / KF_POPUP_HEIGHT).toInt()
                if(popup.height > 880){
                    popup.height = 880
                }
                popup.animationStyle = R.style.anim_popup
                view.findViewById<View>(R.id.delete_text_message).setOnClickListener {
                    dismissPopup()
                    function(PopupTypesOperationChat.DELETE)
                }
                view.findViewById<View>(R.id.copy_text_message).setOnClickListener {
                    dismissPopup()
                    function(PopupTypesOperationChat.COPY)
                }
                view.findViewById<View>(R.id.edit_text_on_message).setOnClickListener {
                    dismissPopup()
                    function(PopupTypesOperationChat.EDIT)
                }

                popup.showAsDropDown(viewUnder, 0, 0)
            }
            POPUP_NORMAL_CHAT -> {
                val view = LayoutInflater.from(APP_ACTIVITY_MAIN)
                    .inflate(R.layout.popup_single_chat_layout, null)
                popup.contentView = view
                popup.isOutsideTouchable = true
                popup.setBackgroundDrawable(APP_ACTIVITY_MAIN.resources.getDrawable(R.drawable.popup_style))
                popup.width = (getDisplayMetrics().widthPixels / KF_POPUP_WIDTH).toInt()
                popup.height = (getDisplayMetrics().heightPixels / KF_POPUP_HEIGHT_ALT).toInt()
                if(popup.height > 780){
                    popup.height = 780
                }
                popup.animationStyle = R.style.anim_popup
                view.findViewById<View>(R.id.delete_message).setOnClickListener {
                    dismissPopup()
                    function(PopupTypesOperationChat.DELETE)
                }
                view.findViewById<View>(R.id.copy_message).setOnClickListener {
                    dismissPopup()
                    function(PopupTypesOperationChat.COPY)
                }
                popup.showAsDropDown(viewUnder, 0, 0)
            }
        }

    }

    fun dismissPopup(){
        popup.dismiss()
    }
}