package com.example.alfamessanger.utills

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.OpenableColumns
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.alfamessanger.R
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.utills.enums.Status
import com.example.alfamessanger.utills.enums.ToolbarSettings
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ServerValue
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

fun showToast(text: String) {
    Toast.makeText(APP_ACTIVITY_REGISTER, text, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.replaceActivity(context: Context, activity: AppCompatActivity) {
        val intent = Intent(context, activity::class.java)
        startActivity(intent)
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        this.finish()
}

fun Activity.replaceActivity(context: Context, activity: AppCompatActivity) {
    val intent = Intent(context, activity::class.java)
    startActivity(intent)
    //overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    this.finish()
}


fun ImageView.downloadAndSetImage(user: Boolean, url: String) {
    if (url.isNotEmpty()) {
        if (user) {
            Picasso.get()
                .load(url).fit()
                .placeholder(R.drawable.default_user)
                .into(this)
        } else {
            Picasso.get()
                .load(url)
                .into(this)
        }
    }
}


fun resizeImageForChat(height: Int, width: Int, imageView: ImageView) {
    if (height > 0 && width > 0) {
        if (height < width) {
            if (width >= 800) {
                imageView.layoutParams.width = 800
            } else {
                imageView.layoutParams.width = width
            }
            if (height >= 450) {
                imageView.layoutParams.height = 450
            } else {
                imageView.layoutParams.height = height
            }
        } else {
            imageView.layoutParams.height = height
            imageView.layoutParams.width = width
        }

    }
}

fun resizeHeightImageForFeed(height: Int, imageView: ImageView) {
    if (height > 0) {
        imageView.layoutParams.height = height
    }
}

fun downloadBitmap(view: ImageView, url: String) {
    GlobalScope.launch(Dispatchers.Main) {
        Picasso.get().load(url).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                view.layoutParams.height = bitmap!!.height
                view.layoutParams.width = bitmap.width
                view.downloadAndSetImage(false, url)
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                showToast(e.toString())
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                //TODO("Not yet implemented")
            }

        })
    }
}


fun RecyclerView.hideShowScrollListener() {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                hideKeyBoard()
            }
        }
    })
}

fun hideKeyBoard() {
    val imm: InputMethodManager =
        APP_ACTIVITY_MAIN.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY_MAIN.window.decorView.windowToken, 0)
}

fun toolbarTools(settings: ToolbarSettings, tittle: String?) {
    when (settings) {
        ToolbarSettings.NORMAL_SETTINGS -> {
            APP_ACTIVITY_MAIN.toolbar.label_toolbar.text = tittle
            APP_ACTIVITY_MAIN.toolbar.label_toolbar.visibility = View.VISIBLE
            APP_ACTIVITY_MAIN.toolbar.edit_text_toolbar_container.visibility = View.GONE
            APP_ACTIVITY_MAIN.toolbar.user_photo_chat.visibility = View.GONE
            APP_ACTIVITY_MAIN.toolbar.user_name_chat.visibility = View.GONE
            APP_ACTIVITY_MAIN.toolbar.status_chat.visibility = View.GONE
            APP_ACTIVITY_MAIN.navigation_view.visibility = View.VISIBLE
            behaviorSwitcher(true)
        }
        ToolbarSettings.SEARCH_SETTINGS -> {
            APP_ACTIVITY_MAIN.toolbar.label_toolbar.visibility = View.GONE
            APP_ACTIVITY_MAIN.toolbar.edit_text_toolbar_container.visibility = View.VISIBLE
            APP_ACTIVITY_MAIN.toolbar.user_photo_chat.visibility = View.GONE
            APP_ACTIVITY_MAIN.toolbar.user_name_chat.visibility = View.GONE
            APP_ACTIVITY_MAIN.toolbar.status_chat.visibility = View.GONE
            APP_ACTIVITY_MAIN.navigation_view.visibility = View.VISIBLE
            behaviorSwitcher(true)
        }
        ToolbarSettings.CHAT_SETTINGS -> {
            APP_ACTIVITY_MAIN.toolbar.label_toolbar.visibility = View.GONE
            APP_ACTIVITY_MAIN.toolbar.edit_text_toolbar_container.visibility = View.GONE
            APP_ACTIVITY_MAIN.toolbar.user_photo_chat.visibility = View.VISIBLE
            APP_ACTIVITY_MAIN.toolbar.user_name_chat.visibility = View.VISIBLE
            APP_ACTIVITY_MAIN.toolbar.status_chat.visibility = View.VISIBLE
            APP_ACTIVITY_MAIN.navigation_view.visibility = View.GONE
            behaviorSwitcher(true)
        }
        ToolbarSettings.PROFILE_SETTINGS -> {
            APP_ACTIVITY_MAIN.toolbar.label_toolbar.text = tittle
            APP_ACTIVITY_MAIN.toolbar.label_toolbar.visibility = View.VISIBLE
            APP_ACTIVITY_MAIN.toolbar.edit_text_toolbar_container.visibility = View.GONE
            APP_ACTIVITY_MAIN.toolbar.user_photo_chat.visibility = View.GONE
            APP_ACTIVITY_MAIN.toolbar.user_name_chat.visibility = View.GONE
            APP_ACTIVITY_MAIN.toolbar.status_chat.visibility = View.GONE
            APP_ACTIVITY_MAIN.navigation_view.visibility = View.GONE
            behaviorSwitcher(true)
        }
        ToolbarSettings.CHANGE_DATA_SETTINGS -> {
            APP_ACTIVITY_MAIN.toolbar.label_toolbar.text = tittle
            APP_ACTIVITY_MAIN.toolbar.label_toolbar.visibility = View.VISIBLE
            APP_ACTIVITY_MAIN.toolbar.edit_text_toolbar_container.visibility = View.GONE
            APP_ACTIVITY_MAIN.toolbar.user_photo_chat.visibility = View.GONE
            APP_ACTIVITY_MAIN.toolbar.user_name_chat.visibility = View.GONE
            APP_ACTIVITY_MAIN.toolbar.status_chat.visibility = View.GONE
            APP_ACTIVITY_MAIN.navigation_view.visibility = View.GONE
            behaviorSwitcher(true)
        }
    }
}

fun behaviorSwitcher(switch: Boolean) {
    if (switch) {
        val param: CoordinatorLayout.LayoutParams =
            APP_ACTIVITY_MAIN.nav_host_fragment_main.view?.layoutParams as CoordinatorLayout.LayoutParams
        param.behavior = AppBarLayout.ScrollingViewBehavior()
    } else {
        val param: CoordinatorLayout.LayoutParams =
            APP_ACTIVITY_MAIN.nav_host_fragment_main.view?.layoutParams as CoordinatorLayout.LayoutParams
        param.behavior = null
    }
}

fun showSnackBar(view: View, title: String, color: Int) {
    val snack = Snackbar.make(view, title, Snackbar.LENGTH_SHORT)
        .setTextColor(Color.WHITE)
        .setBackgroundTint(APP_ACTIVITY_MAIN.getColor(color))
        .setActionTextColor(APP_ACTIVITY_MAIN.getColor(R.color.white))
    snack.setAction(APP_ACTIVITY_MAIN.getString(R.string.cancel)) {
        snack.dismiss()
    }
    snack.show()

}

fun showSnackBarLong(view: View, title: String, color: Int) {
    val snack = Snackbar.make(view, title, Snackbar.LENGTH_LONG)
        .setTextColor(Color.WHITE)
        .setBackgroundTint(APP_ACTIVITY_MAIN.getColor(color))
        .setActionTextColor(APP_ACTIVITY_MAIN.getColor(R.color.white))
    snack.setAction(APP_ACTIVITY_MAIN.getString(R.string.cancel)) {
        snack.dismiss()
    }
    snack.show()

}

@SuppressLint("ClickableViewAccessibility")
fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x >= v.width - v.totalPaddingRight) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    timeFormat.timeZone = TimeZone.getTimeZone("GMT+3")
    return timeFormat.format(time).toString()
}

fun asDate(timeStamp: Long): String {
    val time = Date(timeStamp)
    val timeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    timeFormat.timeZone = TimeZone.getTimeZone("GMT+3")
    return timeFormat.format(time).toString()
}

fun toTime(mills: Int): String {
    val min = TimeUnit.MILLISECONDS.toMinutes(mills.toLong())
    val sec = TimeUnit.MILLISECONDS.toSeconds(mills.toLong())
    return String.format("%02d:%02d", min, sec)
}

fun getTimeStamp(onSuccess: (time: String) -> Unit) {
    DATABASE_REFERENCE.child(NODE_TIME_SERVER).child(CHILD_TIME).setValue(ServerValue.TIMESTAMP)
        .addOnSuccessListener {
            DATABASE_REFERENCE.child(NODE_TIME_SERVER).child(CHILD_TIME).get()
                .addOnSuccessListener {
                    TIMESTAMP = it.value.toString()
                    onSuccess(TIMESTAMP)
                }
        }
}

fun setStatus(status: Status) {
    val mapStatus: Map<String, String>
    when (status) {
        Status.ONLINE -> {
            mapStatus = mapOf(
                CHILD_STATUS to ONLINE
            )
        }
        Status.OFFLINE -> {
            mapStatus = mapOf(
                CHILD_STATUS to OFFLINE
            )
        }
        else -> {
            mapStatus = mapOf(
                CHILD_STATUS to NON_STATUS
            )
        }
    }
    DATABASE_REFERENCE.child(NODE_USERS).child(UID).updateChildren(mapStatus).addOnFailureListener {
        Log.e(TAG_USER_MODEL, "Set status error" + it.message.toString())
    }
}

fun getStatusUser(userId: String, onSuccess: (status: String) -> Unit) {
    DATABASE_REFERENCE.child(NODE_USERS).child(userId).get().addOnSuccessListener {
        val status = it.getValue(UserModel::class.java)
        if (status != null) {
            onSuccess(status.status)
        }
    }
        .addOnFailureListener {
            onSuccess(OFFLINE)
            Log.e(TAG_USER_MODEL, "Get status error: " + it.message.toString())
        }
}


fun toTimeDifference(timeMessage: Long, onSuccess: (time: String) -> Unit) {
    getTimeStamp {
        TIMESTAMP = it
        val difference = TIMESTAMP.toLong() - timeMessage
        val sec = TimeUnit.MILLISECONDS.toSeconds(difference)
        if (sec < 60) {
            onSuccess(APP_ACTIVITY_MAIN.getString(R.string.one_minute))
        } else {
            val min = TimeUnit.MILLISECONDS.toMinutes(difference)
            if (min < 60) {
                onSuccess(min.toString() + APP_ACTIVITY_MAIN.getString(R.string.minute))
            } else {
                val hours = TimeUnit.MILLISECONDS.toHours(difference)
                if (hours < 24) {
                    onSuccess(hours.toString() + APP_ACTIVITY_MAIN.getString(R.string.hour))
                } else {
                    val days = TimeUnit.MILLISECONDS.toDays(difference)
                    if (days < 60) {
                        onSuccess(days.toString() + APP_ACTIVITY_MAIN.getString(R.string.day))
                    } else {
                        asDate(difference)
                    }
                }
            }
        }
    }
}

@SuppressLint("Recycle", "Range")
fun checkSizeOfFile(uri: Uri, function: (name: String, size: String) -> Unit) {
    var size = ""
    var name = ""
    val cursor = APP_ACTIVITY_MAIN.contentResolver.query(uri, null, null, null, null)
    try {
        if (cursor != null && cursor.moveToFirst()) {
            size = cursor.getString(cursor.getColumnIndex(OpenableColumns.SIZE))
            name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            function(name, size)
        }
    } catch (e: Exception) {
        Log.e(TAG, "Get size and name file error" + e.message.toString())
    } finally {
        cursor?.close()
    }
}

fun convertBytesToUpper(bytes: Double): String {
    val kB: Double = bytes / 1024.0
    val mB: Double = kB / 1024
    val gB: Double = mB / 1024.0

    return when {
        kB < 1000 -> {
            ((kB * 100.0).roundToInt() / 100.0).toString() + " KB"
        }
        mB < 1000 -> {
            ((mB * 100.0).roundToInt() / 100.0).toString() + " MB"
        }
        else -> {
            ((gB * 100.0).roundToInt() / 100.0).toString() + " GB"
        }
    }
}

fun copyTextClipBoard(text: String) {
    val clipBoard =
        APP_ACTIVITY_MAIN.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("textUser", text)
    clipBoard.setPrimaryClip(clip)
}

fun getDisplayMetrics(): DisplayMetrics {
    return APP_ACTIVITY_MAIN.resources.displayMetrics
}

fun createBitmapForSize(uri : Uri?) : Bitmap?{
    return BitmapFactory.decodeStream(
        APP_ACTIVITY_MAIN.contentResolver.openInputStream(uri!!),
        null,
        null)
}