package com.marcdonald.hibi.internal.base

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.utils.ThemeUtils
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

abstract class HibiBottomSheetDialogFragment : BottomSheetDialogFragment(), KodeinAware {
	override val kodein: Kodein by closestKodein()
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