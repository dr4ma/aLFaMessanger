package com.example.alfamessanger.presentation

import android.app.Dialog
import android.view.Window
import android.widget.EditText
import com.example.alfamessanger.R
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.DialogSettings
import com.google.android.material.button.MaterialButton

object DialogApp {

    fun create(type: DialogSettings, function: (typeAdd: String, text: String) -> Unit) {
        when (type) {
            DialogSettings.PROFILE_DIALOG -> {
                val dialog = Dialog(APP_ACTIVITY_MAIN)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.setContentView(R.layout.dialog_profile_style)
                dialog.window?.setBackgroundDrawableResource(R.drawable.box_content_style)
                dialog.window?.setLayout(
                    (getDisplayMetrics().widthPixels / 1.1).toInt(),
                    (getDisplayMetrics().heightPixels / 3.6).toInt()
                )
                val text = dialog.findViewById(R.id.cause_block) as EditText
                val block = dialog.findViewById(R.id.block_btn) as MaterialButton
                block.setOnClickListener {
                    function(ADD_TO_BLACK_LIST, text.text.toString())
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    }
}