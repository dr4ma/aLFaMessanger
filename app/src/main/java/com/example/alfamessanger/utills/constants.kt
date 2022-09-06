package com.example.alfamessanger.utills

import android.Manifest
import android.annotation.SuppressLint
import com.example.alfamessanger.domain.models.UserModel
import com.example.alfamessanger.presentation.activities.MainActivity
import com.example.alfamessanger.presentation.activities.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

lateinit var APP_ACTIVITY_REGISTER : RegisterActivity
@SuppressLint("StaticFieldLeak")
lateinit var APP_ACTIVITY_MAIN : MainActivity
lateinit var AUTH : FirebaseAuth
lateinit var DATABASE_REFERENCE : DatabaseReference
lateinit var UID : String
lateinit var USER : UserModel
lateinit var OTHER_USER : UserModel
lateinit var STORAGE_REFERENCE: StorageReference
lateinit var TIMESTAMP: String

const val KF_POPUP_WIDTH = 2.16
const val KF_POPUP_HEIGHT = 2.97
const val KF_POPUP_HEIGHT_ALT = 3.47

const val BASE_URL = "https://fcm.googleapis.com"
const val SERVER_KEY = "AAAA1qulGvA:APA91bHPdXZZXX0unEU_ZwE-x_tcoYRDwH7LisJnT6OPgw_o6bIaGk_pxJ66DobNYoz9qVQfQFAGwbVtjypKE0jryg4NhIDbguls80p-DcT-WsGa3TY7b1blynEoPl3AbAPWCwcNbMYH"
const val CONTENT_TYPE = "application/json"

const val ONLINE = "Online"
const val OFFLINE = "Offline"
const val NON_STATUS = "Last seen recently"

const val NODE_USERS = "users"
const val NODE_MESSAGES = "messages"
const val NODE_CHATS = "chats"
const val NODE_TIME_SERVER = "time_server"
const val NODE_FRIENDS = "friends"
const val NODE_SAVED = "saved"
const val NODE_BLACK_LIST = "black_list"

const val FOLDER_PROFILE_IMAGE = "profile_image"
const val FOLDER_MESSAGES_IMAGE = "message_image"
const val FOLDER_MESSAGES_VOICE = "message_voice"
const val FOLDER_MESSAGES_FILES = "message_files"

const val CHILD_BIO = "bio"
const val CHILD_NICKNAME = "nickname"
const val CHILD_NAME = "name"
const val CHILD_ICON_URL = "iconUrl"
const val CHILD_TEXT = "text"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_TIMESTAMP = "time_stamp"
const val CHILD_ID = "id"
const val CHILD_IMAGE_URL = "image_url"
const val CHILD_WIDTH = "width"
const val CHILD_HEIGHT = "height"
const val CHILD_DURATION = "duration"
const val CHILD_SIZE = "size"
const val CHILD_STATUS = "status"
const val CHILD_TIME = "time"

const val TAG : String = "TAG"
const val TAG_FILE_STORAGE : String = "TAG_FILE_STORAGE"
const val TAG_REGISTER : String = "TAG_REGISTER"
const val TAG_SINGLE_CHAT : String = "TAG_SINGLE_CHAT"
const val TAG_USER_MODEL : String = "TAG_USER_MODEL"
const val TAG_VOICE_RECORD : String = "TAG_VOICE_RECORD"
const val TAG_FRIENDS_LIST : String = "TAG_FRIENDS_LIST"
const val TAG_IMAGE_FULL_SCREEN : String = "TAG_IMAGE_FULL_SCREEN"
const val TAG_BLACK_LIST : String = "TAG_BLACK_LIST"
const val TAG_NOTIFICATION : String = "TAG_NOTIFICATION"
const val TAG_SAVE_FILE : String = "TAG_SAVE_FILE"


const val ID_AUTH_VERIFY : String = "id_verify"
const val PHONE_AUTH : String = "phone"
const val DEFAULT_STATUS : String = "Last seen recently"
const val NIGHT_THEME : String = "night_mode"
const val LIGHT_THEME : String = "light_mode"

const val TYPE_TEXT : String = "text"
const val TYPE_IMAGE : String = "image"
const val TYPE_VOICE : String = "voice"
const val TYPE_FILE : String = "file"

const val POPUP_TEXT_CHAT = "text_popup_chat"
const val POPUP_NORMAL_CHAT = "normal_popup_chat"

const val KEY_USER_CHAT = "user_chat"
const val KEY_PROFILE = "profile"
const val KEY_CHAT_IMAGE = "chat_image"
const val KEY_POSITION = "chat_position"
const val KEY_UID = "chat_position"

const val CAMERA = "camera"
const val GALLERY = "gallery"
const val FILE = "file"

const val EXIT = "exit"
const val FRIENDS = "friends"
const val SAVED_PHOTOS = "saved_photos"
const val BLACK_LIST = "black_list"

const val SAVE = "save"
const val COPY = "copy"
const val DOWNLOAD = "download"
const val DELETE = "delete"

const val SAVE_IN_GALLERY = "save_in_gallery"
const val CLEAR_CANVAS = "clear_canvas"

const val ADD_TO_BLACK_LIST = "black_list"

const val RECORD_AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO
const val WRITE_FILES = Manifest.permission.WRITE_EXTERNAL_STORAGE
const val PICK_FILE_REQUEST_CODE = 203
const val PICK_IMAGE_CAMERA = 202
const val PICK_IMAGE_GALLERY = 201
const val PERMISSION_REQUEST = 200

const val RECEIVED_MESSAGE = "received_message"
const val CAME_MESSAGE = "came_message"