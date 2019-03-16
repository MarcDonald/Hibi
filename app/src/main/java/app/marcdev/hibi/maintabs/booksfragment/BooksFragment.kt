package app.marcdev.hibi.maintabs.booksfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.ScopedFragment
import timber.log.Timber


class BooksFragment : ScopedFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    return inflater.inflate(R.layout.fragment_books, container, false)
  }
}