/*
 * Copyright 2019 Marc Donald
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
package com.marcdonald.hibi.internal.base

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marcdonald.hibi.HibiAndroidViewModelFactory
import com.marcdonald.hibi.HibiViewModelFactory
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.utils.ThemeUtils
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

abstract class HibiBottomSheetDialogFragment : BottomSheetDialogFragment(), KodeinAware {
	override val kodein: Kodein by closestKodein()
	protected val viewModelFactory: HibiViewModelFactory by instance()
	protected val androidViewModelFactory: HibiAndroidViewModelFactory by instance()

	private val themeUtils: ThemeUtils by instance()

	override fun getTheme(): Int {
		return if(themeUtils.isLightMode())
			R.style.Theme_Hibi_BottomSheetDialog_Light
		else
			R.style.Theme_Hibi_BottomSheetDialog_Dark
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return BottomSheetDialog(requireContext(), theme)
	}
}