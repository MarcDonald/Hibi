/*
 * Copyright 2021 Marc Donald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marcdonald.hibi.internal

// <editor-fold desc="Database">
const val PRODUCTION_DATABASE_VERSION = 14
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

// <editor-fold desc="Quick Add New Word Arguments">
const val NEW_WORD_QUICK_ADD = "new_word_quick_add"
const val NEW_WORD_READING_QUICK_ADD = "new_word_reading_quick_add"
const val NEW_WORD_MEANING_QUICK_ADD = "new_word_meaning_quick_add"
const val NEW_WORD_PART_QUICK_ADD = "new_word_part_quick_add"
// </editor-fold>

// <editor-fold desc="Preferences">
const val PREF_ENTRY_DIVIDERS = "pref_entry_dividers"
const val PREF_NIGHT_MODE = "pref_night_mode"
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
const val PREF_RECYCLER_ANIMATIONS = "pref_recycler_animations"
const val PREF_PERIODICALLY_CHECK_FOR_UPDATES = "pref_periodically_update_check"
const val PREF_LAST_UPDATE_CHECK = "pref_last_update_check"
const val PREF_DATE_HEADER_PERIOD = "pref_date_header_period"
// </editor-fold>

// <editor-fold desc="Notifications">
const val REMINDER_NOTIFICATION_ID = 1
const val NOTIFICATION_CHANNEL_REMINDER_ID = "reminder"
// </editor-fold>

// <editor-fold desc="Intent actions">
const val ADD_ENTRY_SHORTCUT_INTENT_ACTION = "com.marcdonald.hibi.intent.ADD_ENTRY_SHORTCUT"
const val ADD_ENTRY_NOTIFICATION_INTENT_ACTION = "com.marcdonald.hibi.intent.ADD_ENTRY_NOTIFICATION"
const val PACKAGE = "com.marcdonald.hibi"
// </editor-fold>

// <editor-fold desc="Request Codes">
const val REMINDER_NOTIFICATION_REQUEST_CODE = 0
const val REMINDER_ALERT_RECEIVER_REQUEST_CODE = 1
const val CHOOSE_RESTORE_FILE_REQUEST_CODE = 2
const val PERMISSION_REQUEST_CODE = 3
const val CHOOSE_IMAGE_TO_ADD_REQUEST_CODE = 4
// </editor-fold>