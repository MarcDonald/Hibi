package app.marcdev.hibi.internal

const val PRODUCTION_DATABASE_VERSION = 9
const val PRODUCTION_DATABASE_NAME = "ProductionAppDatabase.db"
const val BACKUP_PATH = "/Hibi/backup/"

// Main tab indices
const val ENTRIES_TAB = 0
const val CALENDAR_TAB = 1
const val TAGS_TAB = 2
const val BOOKS_TAB = 3

// Keys typically passed in arguments
const val ENTRY_ID_KEY = "entryId"
const val NEW_WORD_ID_KEY = "newWordId"
const val SEARCH_TERM_KEY = "searchTerm"
const val IS_EDIT_MODE_KEY = "isEditMode"
const val TAG_ID_KEY = "tagId"
const val BOOK_ID_KEY = "bookId"

// Preferences
const val PREF_ENTRY_DIVIDERS = "pref_entry_dividers"
const val PREF_DARK_THEME = "pref_dark_theme"
const val PREF_REMINDER_NOTIFICATION = "pref_reminder_notification"
const val PREF_REMINDER_TIME = "pref_reminder_time"
const val PREF_BACKUP = "pref_backup"
const val PREF_RESTORE = "pref_restore"

// Notifications
const val REMINDER_NOTIFICATION_ID = 1
const val NOTIFICATION_CHANNEL_REMINDER_ID = "reminder"

// Intent actions
const val ADD_ENTRY_SHORTCUT_INTENT_ACTION = "app.marcdev.hibi.intent.ADD_ENTRY_SHORTCUT"
const val ADD_ENTRY_NOTIFICATION_INTENT_ACTION = "app.marcdev.hibi.intent.ADD_ENTRY_NOTIFICATION"
const val PACKAGE = "app.marcdev.hibi"

// Request Codes
const val REMINDER_NOTIFICATION_REQUEST_CODE = 0
const val ALERT_RECEIVER_REQUEST_CODE = 1