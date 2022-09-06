package com.example.alfamessanger.utills

import androidx.fragment.app.Fragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

object CropImageBuilder {

    fun cropImage(context : Fragment, reqWidth : Int, reqHeight : Int, oval : Boolean){
        if(oval){
            CropImage.activity()
                .setAspectRatio(1, 1)
                .setRequestedSize(reqWidth, reqHeight)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(APP_ACTIVITY_MAIN, context)
        }
        else{
            CropImage.activity()
                .setRequestedSize(reqWidth, reqHeight)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(APP_ACTIVITY_MAIN, context)
        }
    }
}