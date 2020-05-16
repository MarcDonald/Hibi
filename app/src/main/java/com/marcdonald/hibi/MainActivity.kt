/*
 * Copyright 2020 Marc Donald
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
package com.marcdonald.hibi

import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.Navigation
import com.marcdonald.hibi.internal.ADD_ENTRY_NOTIFICATION_INTENT_ACTION
import com.marcdonald.hibi.internal.ADD_ENTRY_SHORTCUT_INTENT_ACTION
import com.marcdonald.hibi.internal.REMINDER_NOTIFICATION_ID
import com.marcdonald.hibi.internal.base.HibiActivity
import com.marcdonald.hibi.screens.main.MainScreenFragmentDirections
import timber.log.Timber

class MainActivity : HibiActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		Timber.v("Log: onCreate: Started")
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		if(intent.action == ADD_ENTRY_SHORTCUT_INTENT_ACTION || intent.action == ADD_ENTRY_NOTIFICATION_INTENT_ACTION) {
			Timber.d("Log: onCreate: Started with Add Entry intent")

			val addEntryAction = MainScreenFragmentDirections.addEntryAction()
			try {
				Navigation.findNavController(this, R.id.nav_host_fragment).navigate(addEntryAction)
			} catch(e: IllegalArgumentException) {
				Timber.w("Log: onCreate: Theme changed after opening app with an intent")
			}

			if(intent.action == ADD_ENTRY_NOTIFICATION_INTENT_ACTION)
				NotificationManagerCompat.from(this).cancel(REMINDER_NOTIFICATION_ID)
		}
	}
}
