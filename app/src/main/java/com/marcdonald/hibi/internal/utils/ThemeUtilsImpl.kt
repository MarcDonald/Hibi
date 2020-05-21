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
package com.marcdonald.hibi.internal.utils

import android.content.Context
import android.preference.PreferenceManager
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.PREF_DARK_THEME

class ThemeUtilsImpl(private val context: Context) : ThemeUtils {
	override fun isLightMode(): Boolean {
		return !(PreferenceManager.getDefaultSharedPreferences(context.applicationContext).getBoolean(PREF_DARK_THEME, true))
	}

	override fun getAccentColor(): Int {
		return if(isLightMode())
			context.resources.getColor(R.color.lightThemeColorAccent, null)
		else
			context.resources.getColor(R.color.darkThemeColorAccent, null)
	}
}