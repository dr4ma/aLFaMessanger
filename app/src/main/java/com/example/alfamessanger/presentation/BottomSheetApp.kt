package com.example.alfamessanger.presentation

import android.view.LayoutInflater
import android.view.View
import com.example.alfamessanger.R
import com.example.alfamessanger.utills.*
import com.example.alfamessanger.utills.enums.BottomSheetSettings
import com.google.android.material.bottomsheet.BottomSheetDialog

object BottomSheetApp {

    fun create(type: BottomSheetSettings, function: (typeAdd: String) -> Unit) {
        when (type) {
            BottomSheetSettings.ACCOUNT_BOTTOM_SHEET -> {
                val bottomSheetDialog =
                    BottomSheetDialog(APP_ACTIVITY_MAIN, R.style.BottomSheetDialogTheme)
                val bottomSheetView = LayoutInflater.from(APP_ACTIVITY_MAIN)
                    .inflate(R.layout.bottom_sheet_account, null)

                bottomSheetView.findViewById<View>(R.id.exit_account_bottom_sheet)
                    .setOnClickListener {
                        bottomSheetDialog.dismiss()
                        function(EXIT)
                    }
                bottomSheetView.findViewById<View>(R.id.friends_account_bottom_sheet)
                    .setOnClickListener {
                        bottomSheetDialog.dismiss()
                        function(FRIENDS)
                    }
                bottomSheetView.findViewById<View>(R.id.saved_account_bottom_sheet)
                    .setOnClickListener {
                        bottomSheetDialog.dismiss()
                        function(SAVED_PHOTOS)
                    }
                bottomSheetView.findViewById<View>(R.id.black_list_account_bottom_sheet)
                    .setOnClickListener {
                        bottomSheetDialog.dismiss()
                        function(BLACK_LIST)
                    }
                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()
            }
            BottomSheetSettings.CHAT_BOTTOM_SHEET -> {
                val bottomSheetDialog =
                    BottomSheetDialog(APP_ACTIVITY_MAIN, R.style.BottomSheetDialogTheme)
                val bottomSheetView =
                    LayoutInflater.from(APP_ACTIVITY_MAIN).inflate(R.layout.bottom_sheet_chat, null)

                bottomSheetView.findViewById<View>(R.id.camera_chat).setOnClickListener {
                    bottomSheetDialog.dismiss()
                    function(CAMERA)
                }
                bottomSheetView.findViewById<View>(R.id.gallery_chat).setOnClickListener {
                    bottomSheetDialog.dismiss()
                    function(GALLERY)
                }
                bottomSheetView.findViewById<View>(R.id.file_chat).setOnClickListener {
                    bottomSheetDialog.dismiss()
                    function(FILE)
                }

                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()
            }
            BottomSheetSettings.FULL_SCREEN_IMAGE_SAVED -> {
                val bottomSheetDialog =
                    BottomSheetDialog(APP_ACTIVITY_MAIN, R.style.BottomSheetDialogTheme)
                val bottomSheetView =
                    LayoutInflater.from(APP_ACTIVITY_MAIN).inflate(R.layout.bottom_sheet_full_screen_image, null)

                bottomSheetView.findViewById<View>(R.id.save_image_bottom_sheet).setOnClickListener {
                    bottomSheetDialog.dismiss()
                    function(SAVE)
                }
                bottomSheetView.findViewById<View>(R.id.copy_image_bottom_sheet).setOnClickListener {
                    bottomSheetDialog.dismiss()
                    function(COPY)
                }
                bottomSheetView.findViewById<View>(R.id.download_image_bottom_sheet).setOnClickListener {
                    bottomSheetDialog.dismiss()
                    function(DOWNLOAD)
                }
                bottomSheetView.findViewById<View>(R.id.dismiss_image_bottom_sheet).setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()
            }
            BottomSheetSettings.FULL_SCREEN_IMAGE_NOT_SAVED -> {
                val bottomSheetDialog =
                    BottomSheetDialog(APP_ACTIVITY_MAIN, R.style.BottomSheetDialogTheme)
                val bottomSheetView =
                    LayoutInflater.from(APP_ACTIVITY_MAIN).inflate(R.layout.bottom_sheet_full_screen_image_not_saved, null)

                bottomSheetView.findViewById<View>(R.id.save_image_bottom_sheet_not_saved).setOnClickListener {
                    bottomSheetDialog.dismiss()
                    function(DELETE)
                }
                bottomSheetView.findViewById<View>(R.id.copy_image_bottom_sheet_not_saved).setOnClickListener {
                    bottomSheetDialog.dismiss()
                    function(COPY)
                }
                bottomSheetView.findViewById<View>(R.id.download_image_bottom_sheet_not_saved).setOnClickListener {
                    bottomSheetDialog.dismiss()
                    function(DOWNLOAD)
                }
                bottomSheetView.findViewById<View>(R.id.dismiss_image_bottom_sheet).setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()
            }
            BottomSheetSettings.GRAFFITI_BOTTOM_SHEET -> {
                val bottomSheetDialog =
                    BottomSheetDialog(APP_ACTIVITY_MAIN, R.style.BottomSheetDialogTheme)
                val bottomSheetView =
                    LayoutInflater.from(APP_ACTIVITY_MAIN).inflate(R.layout.bottom_sheet_graffiti, null)

                bottomSheetView.findViewById<View>(R.id.save_bitmap_in_gallery).setOnClickListener {
                    bottomSheetDialog.dismiss()
                    function(SAVE_IN_GALLERY)
                }
                bottomSheetView.findViewById<View>(R.id.clear_canvas).setOnClickListener {
                    bottomSheetDialog.dismiss()
                    function(CLEAR_CANVAS)
                }
                bottomSheetView.findViewById<View>(R.id.dismiss_graffiti_bottom_sheet).setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()
            }
        }
    }
}