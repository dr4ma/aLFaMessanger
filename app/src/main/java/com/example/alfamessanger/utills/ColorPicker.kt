package com.example.alfamessanger.utills

import android.content.DialogInterface
import com.example.alfamessanger.R
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder

object ColorPicker {

    fun showPicker(tittle : String, colorBrush : Int, function : (lastColor : Int) -> Unit){
        val color = colorBrush
        ColorPickerDialogBuilder
            .with(APP_ACTIVITY_MAIN)
            .setTitle(tittle)
            .initialColor(color)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setPositiveButton(APP_ACTIVITY_MAIN.getString(R.string.ok_color)) { d, lastSelectedColor, allColors ->
                function(lastSelectedColor)
            }
            .setNegativeButton(APP_ACTIVITY_MAIN.getString(R.string.cancel_color)) { p0, p1 ->

            }
            .build()
            .show()
    }
}