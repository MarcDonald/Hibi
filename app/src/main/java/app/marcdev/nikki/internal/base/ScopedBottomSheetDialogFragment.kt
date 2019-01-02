package app.marcdev.nikki.internal.base

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class ScopedBottomSheetDialogFragment : BottomSheetDialogFragment(), CoroutineScope {
  private lateinit var job: Job

  override val coroutineContext: CoroutineContext
    get() = job + Dispatchers.Main

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    job = Job()
  }

  override fun onDestroy() {
    super.onDestroy()
    job.cancel()
  }
}