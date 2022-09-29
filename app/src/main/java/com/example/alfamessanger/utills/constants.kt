package com.example.alfamessanger.utills

import android.Manifest
import android.annotation.SuppressLint
import com.example.alfamessanger.domain.models.*
import com.example.alfamessanger.presentation.activities.mainActivity.MainActivity
import com.example.alfamessanger.presentation.activities.registerActivity.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

lateinit var APP_ACTIVITY_REGISTER: RegisterActivity

@SuppressLint("StaticFieldLeak")
lateinit var APP_ACTIVITY_MAIN: MainActivity
lateinit var AUTH: FirebaseAuth
lateinit var DATABASE_REFERENCE: DatabaseReference
lateinit var UID: String
lateinit var USER: UserModel
lateinit var STORAGE_REFERENCE: StorageReference
lateinit var TIMESTAMP: String

var FEED_LIST = mutableListOf<FeedNewsModel>()
var CHAT_LIST = mutableListOf<ChatModel>()
var CHANNELS_LIST = mutableListOf<ChannelMyChatsModel>()

const val PUBLIC: String = "public"
const val PRIVATE: String = "private"
const val CLOSE: String = "close"

const val KF_POPUP_WIDTH = 2.16
const val KF_POPUP_HEIGHT = 2.97
const val KF_POPUP_HEIGHT_ALT = 3.47
const val KF_POPUP_HEIGHT_SUB = 5.0

const val BASE_URL = "https://fcm.googleapis.com"
const val SERVER_KEY =
    "AAAA1qulGvA:APA91bHPdXZZXX0unEU_ZwE-x_tcoYRDwH7LisJnT6OPgw_o6bIaGk_pxJ66DobNYoz9qVQfQFAGwbVtjypKE0jryg4NhIDbguls80p-DcT-WsGa3TY7b1blynEoPl3AbAPWCwcNbMYH"
const val CONTENT_TYPE = "application/json"
const val TOPIC = "/topics/myTopic"

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
const val NODE_CHANNELS = "channels"
const val NODE_NOTIFICATIONS = "notifications"

const val FOLDER_PROFILE_IMAGE = "profile_image"
const val FOLDER_MESSAGES_IMAGE = "message_image"
const val FOLDER_MESSAGES_VOICE = "message_voice"
const val FOLDER_MESSAGES_FILES = "message_files"
const val FOLDER_CHANNEL_FILE = "channel_files"
const val FOLDER_CHANNEL_IMAGE = "channel_images"
const val FOLDER_CHANNELS_ICON = "channel_icons"

const val CHILD_BIO = "bio"
const val CHILD_NICKNAME = "nickname"
const val CHILD_NAME = "name"
const val CHILD_ICON_URL = "iconUrl"
const val CHILD_ICON_URI = "iconUri"
const val CHILD_TEXT = "text"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_TIMESTAMP = "time_stamp"
const val CHILD_ID = "id"
const val CHILD_IMAGE_URL = "image_url"
const val CHILD_FILE_URL = "file_url"
const val CHILD_FILE_URI = "file_uri"
const val CHILD_WIDTH = "width"
const val CHILD_HEIGHT = "height"
const val CHILD_DURATION = "duration"
const val CHILD_SIZE = "size"
const val CHILD_STATUS = "status"
const val CHILD_COUNT_SUBS = "countSubs"
const val CHILD_TIME = "time"
const val CHILD_FEED = "feed"
const val CHILD_SUBSCRIBERS = "subscribers"
const val CHILD_NOTIFICATION_LIST = "notification_list"

const val TAG: String = "TAG"
const val TAG_FILE_STORAGE: String = "TAG_FILE_STORAGE"
const val TAG_REGISTER: String = "TAG_REGISTER"
const val TAG_SINGLE_CHAT: String = "TAG_SINGLE_CHAT"
const val TAG_USER_MODEL: String = "TAG_USER_MODEL"
const val TAG_VOICE_RECORD: String = "TAG_VOICE_RECORD"
const val TAG_FRIENDS_LIST: String = "TAG_FRIENDS_LIST"
const val TAG_IMAGE_FULL_SCREEN: String = "TAG_IMAGE_FULL_SCREEN"
const val TAG_BLACK_LIST: String = "TAG_BLACK_LIST"
const val TAG_NOTIFICATION: String = "TAG_NOTIFICATION"
const val TAG_SAVE_FILE: String = "TAG_SAVE_FILE"
const val TAG_CREATE_CHANNEL: String = "TAG_CREATE_CHANNEL"
const val TAG_CHANNEL: String = "TAG_CHANNEL"
const val TAG_CREATE_FEED: String = "TAG_CREATE_FEED"
const val TAG_DELETE_FEED: String = "TAG_DELETE_FEED"
const val TAG_CREATE_NOTIFICATION: String = "TAG_CREATE_NOTIFICATION"


const val ID_AUTH_VERIFY: String = "id_verify"
const val PHONE_AUTH: String = "phone"
const val DEFAULT_STATUS: String = "Last seen recently"
const val NIGHT_THEME: String = "night_mode"
const val LIGHT_THEME: String = "light_mode"

const val TYPE_TEXT: String = "text"
const val TYPE_IMAGE: String = "image"
const val TYPE_VOICE: String = "voice"
const val TYPE_FILE: String = "file"

const val TYPE_ADD_FRIEND_OPEN: String = "add_friend_open"
const val TYPE_ADD_FRIEND_CLOSE: String = "add_friend_close"
const val TYPE_INVITE_HIDE_CHANNELS: String = "invite_hide_channel"

const val POPUP_TEXT_CHAT = "text_popup_chat"
const val POPUP_NORMAL_CHAT = "normal_popup_chat"
const val POPUP_CHANNEL_SUB = "popup_channel_sub"
const val POPUP_CHANNEL_ADMIN = "popup_channel_admin"

const val KEY_USER_CHAT = "user_chat"
const val KEY_PROFILE = "profile"
const val KEY_ACCOUNT = "account"
const val KEY_CHAT_IMAGE = "chat_image"
const val KEY_POSITION = "chat_position"
const val KEY_UID = "chat_position"
const val KEY_CHANNEL = "chat_channel"
const val KEY_SUBS = "chat_subs"
const val KEY_RECYCLER_CHANNEL_STATE = "recycler_channel_state"

const val CAMERA = "camera"
const val GALLERY = "gallery"
const val FILE = "file"

const val EXIT = "exit"
const val FRIENDS = "friends"
const val SAVED_PHOTOS = "saved_photos"
const val BLACK_LIST = "black_list"
const val MY_CHANNELS = "my_channels"

const val SAVE = "save"
const val COPY = "copy"
const val DOWNLOAD = "download"
const val DELETE = "delete"

const val SAVE_IN_GALLERY = "save_in_gallery"
const val CLEAR_CANVAS = "clear_canvas"

const val ADD_PHOTO = "add_photo"
const val ADD_FILE = "add_file"

const val SETTINGS = "settings"
const val SUBSCRIBERS = "subscribers"
const val DELETE_CHANNEL = "delete_channel"

const val CHANNEL_PAGE = "channel_page"
const val SUBSCRIBERS_ = "subscribers"
const val SUBSCRIBE_CHANNEL = "subscribe_channel"
const val UNSUBSCRIBE_CHANNEL = "unsubscribe_channel"

const val ADD_TO_BLACK_LIST = "black_list"

const val RECORD_AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO
const val WRITE_FILES = Manifest.permission.WRITE_EXTERNAL_STORAGE
const val PICK_FILE_REQUEST_CODE = 203
const val PICK_IMAGE_CAMERA = 202
const val PICK_IMAGE_GALLERY = 201
const val PERMISSION_REQUEST = 200

const val RECEIVED_MESSAGE = "received_message"
const val CAME_MESSAGE = "came_message"

const val ADAPTER_CHANNELS = "adapter_channels"
const val ADAPTER_CHATS = "adapter_chats"

const val ID_USER = "userId"
const val WRITTEN = "written"