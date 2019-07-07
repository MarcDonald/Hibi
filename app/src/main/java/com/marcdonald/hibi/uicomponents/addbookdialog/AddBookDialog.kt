package com.marcdonald.hibi.uicomponents.addbookdialog

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.button.MaterialButton
import com.marcdonald.hibi.R
import com.marcdonald.hibi.internal.BOOK_ID_KEY
import com.marcdonald.hibi.internal.base.HibiDialogFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AddBookDialog : HibiDialogFragment(), KodeinAware {
	override val kodein: Kodein by closestKodein()

	// <editor-fold desc="View Model">
	private val viewModelFactory: AddBookViewModelFactory by instance()
	private lateinit var viewModel: AddBookViewModel
	// </editor-fold>

	// <editor-fold desc="UI Components">
	private lateinit var input: EditText
	private lateinit var dialogTitle: TextView
	// </editor-fold>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddBookViewModel::class.java)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.dialog_new_book, container, false)
		bindViews(view)
		setupObservers()
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		arguments?.let {
			viewModel.passArguments(requireArguments().getInt(BOOK_ID_KEY, 0))
		}
	}

	private fun bindViews(view: View) {
		dialogTitle = view.findViewById(R.id.txt_add_book_title)
		input = view.findViewById(R.id.edt_new_book_input)
		val saveButton: MaterialButton = view.findViewById(R.id.btn_save_book)
		saveButton.setOnClickListener(saveClickListener)
		val deleteButton: MaterialButton = view.findViewById(R.id.btn_delete_book)
		deleteButton.setOnClickListener(deleteClickListener)
		input.setOnKeyListener(saveOnEnterListener)
		input.requestFocus()
	}

	private fun setupObservers() {
		viewModel.bookTitle.observe(this, Observer { value ->
			value?.let { title ->
				input.setText(title)
			}
		})

		viewModel.isEditMode.observe(this, Observer { value ->
			value?.let { isEditMode ->
				if(isEditMode)
					dialogTitle.text = resources.getString(R.string.edit_book)
			}
		})

		viewModel.displayEmptyContentWarning.observe(this, Observer { value ->
			value?.let { show ->
				if(show)
					input.error = resources.getString(R.string.empty_content_warning)
			}
		})

		viewModel.displayDuplicateNameWarning.observe(this, Observer { value ->
			value?.let { show ->
				if(show)
					input.error = resources.getString(R.string.book_already_exists)
			}
		})

		viewModel.dismiss.observe(this, Observer { value ->
			value?.let { dismiss ->
				if(dismiss)
					dismiss()
			}
		})
	}

	private val saveClickListener = View.OnClickListener {
		viewModel.saveBook(input.text.toString())
	}

	private val deleteClickListener = View.OnClickListener {
		viewModel.deleteBook()
	}

	private val saveOnEnterListener: View.OnKeyListener =
		View.OnKeyListener { _: View, keyCode: Int, keyEvent: KeyEvent ->
			if((keyEvent.action == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
				viewModel.saveBook(input.text.toString())
			}
			/* This is false so that the event isn't consumed and other buttons (such as the back button)
			 * can be pressed */
			false
		}
}