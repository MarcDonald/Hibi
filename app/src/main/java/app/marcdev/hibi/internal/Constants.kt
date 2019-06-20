package app.marcdev.hibi.internal

// <editor-fold desc="Database">
const val PRODUCTION_DATABASE_VERSION = 13
const val PRODUCTION_DATABASE_NAME = "Database.db"
// </editor-fold>

// <editor-fold desc="Main Tab Indices">
const val ENTRIES_TAB = 0
const val CALENDAR_TAB = 1
const val TAGS_TAB = 2
const val BOOKS_TAB = 3
// </editor-fold>

// <editor-fold desc="Keys typically passed in arguments">
const val ENTRY_ID_KEY = "entryId"
const val NEW_WORD_ID_KEY = "newWordId"
const val SEARCH_TERM_KEY = "searchTerm"
const val IS_EDIT_MODE_KEY = "isEditMode"
const val TAG_ID_KEY = "tagId"
const val BOOK_ID_KEY = "bookId"
const val RESTORE_FILE_PATH_KEY = "restore_file_path"
// </editor-fold>

// <editor-fold desc="Preferences">
const val PREF_ENTRY_DIVIDERS = "pref_entry_dividers"
const val PREF_DARK_THEME = "pref_dark_theme"
const val PREF_REMINDER_NOTIFICATION = "pref_reminder_notification"
const val PREF_REMINDER_TIME = "pref_reminder_time"
const val PREF_BACKUP = "pref_backup"
const val PREF_RESTORE = "pref_restore"
const val PREF_CLIPBOARD_BEHAVIOR = "pref_clipboard_behavior"
const val PREF_SAVE_ON_PAUSE = "pref_save_on_pause"
const val PREF_MAIN_ENTRY_DISPLAY_ITEMS = "pref_main_entry_display_items"
const val PREF_MAIN_ENTRY_DISPLAY_LOCATION = "pref_main_entry_display_location"
const val PREF_MAIN_ENTRY_DISPLAY_TAGS = "pref_main_entry_display_tags"
const val PREF_MAIN_ENTRY_DISPLAY_BOOKS = "pref_main_entry_display_books"
// </editor-fold>

// <editor-fold desc="Notifications">
const val REMINDER_NOTIFICATION_ID = 1
const val NOTIFICATION_CHANNEL_REMINDER_ID = "reminder"
// </editor-fold>

// <editor-fold desc="Intent actions">
const val ADD_ENTRY_SHORTCUT_INTENT_ACTION = "app.marcdev.hibi.intent.ADD_ENTRY_SHORTCUT"
const val ADD_ENTRY_NOTIFICATION_INTENT_ACTION = "app.marcdev.hibi.intent.ADD_ENTRY_NOTIFICATION"
const val PACKAGE = "app.marcdev.hibi"
// </editor-fold>

// <editor-fold desc="Request Codes">
const val REMINDER_NOTIFICATION_REQUEST_CODE = 0
const val ALERT_RECEIVER_REQUEST_CODE = 1
const val CHOOSE_RESTORE_FILE_REQUEST_CODE = 2
const val PERMISSION_REQUEST_CODE = 3
const val CHOOSE_IMAGE_TO_ADD_REQUEST_CODE = 4
// </editor-fold>