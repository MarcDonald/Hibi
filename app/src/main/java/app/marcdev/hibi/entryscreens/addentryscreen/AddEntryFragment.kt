package app.marcdev.hibi.entryscreens.addentryscreen

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.ENTRY_ID_KEY
import app.marcdev.hibi.internal.SEARCH_TERM_KEY
import app.marcdev.hibi.internal.base.BinaryOptionDialog
import app.marcdev.hibi.internal.base.ScopedFragment
import app.marcdev.hibi.internal.formatDateForDisplay
import app.marcdev.hibi.internal.formatTimeForDisplay
import app.marcdev.hibi.search.searchresults.SearchResultsDialog
import app.marcdev.hibi.uicomponents.addentrytobookdialog.AddEntryToBookDialog
import app.marcdev.hibi.uicomponents.addtagtoentrydialog.AddTagToEntryDialog
import app.marcdev.hibi.uicomponents.newwordsdialog.NewWordDialog
import app.marcdev.hibi.uicomponents.views.SearchBar
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class AddEntryFragment : ScopedFragment(), KodeinAware {

  // Kodein initialisation
  override val kodein by closestKodein()

  // Viewmodel
  private val viewModelFactory: AddEntryViewModelFactory by instance()
  private lateinit var viewModel: AddEntryViewModel

  // UI Components
  private lateinit var dateButton: MaterialButton
  private lateinit var timeButton: MaterialButton
  private lateinit var contentInput: EditText
  private lateinit var backConfirmDialog: BinaryOptionDialog
  private lateinit var toolbarTitle: TextView
  private lateinit var searchBar: SearchBar

  // Other
  private val dateTimeStore = DateTimeStore()
  private var entryIdBeingEdited = 0

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEntryViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.fragment_add_entry, container, false)

    bindViews(view)
    initBackConfirmDialog()
    focusInput()

    requireActivity().onBackPressedDispatcher.addCallback { onBackPress() }

    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    arguments?.let {
      val entryId = AddEntryFragmentArgs.fromBundle(it).entryId
      if(entryId != 0) {
        convertToEditMode(entryId)
      }
    }
  }

  private fun bindViews(view: View) {
    toolbarTitle = view.findViewById(R.id.txt_add_toolbar_title)

    searchBar = view.findViewById(R.id.searchbar_add_entry)
    searchBar.setSearchAction(this::search)

    dateButton = view.findViewById(R.id.btn_date)
    dateButton.setOnClickListener(dateClickListener)
    initDateButton()

    timeButton = view.findViewById(R.id.btn_time)
    timeButton.setOnClickListener(timeClickListener)
    initTimeButton()

    contentInput = view.findViewById(R.id.edt_content)

    val saveButton: MaterialButton = view.findViewById(R.id.btn_save)
    saveButton.setOnClickListener(saveClickListener)

    val backButton: ImageView = view.findViewById(R.id.img_add_entry_toolbar_back)
    backButton.setOnClickListener(backClickListener)

    // Option bar icons
    val addTagButton: ImageView = view.findViewById(R.id.img_option_tag)
    addTagButton.setOnClickListener(addTagClickListener)

    val addToBookButton: ImageView = view.findViewById(R.id.img_option_book)
    addToBookButton.setOnClickListener(addToBookClickListener)

    val addLocationButton: ImageView = view.findViewById(R.id.img_option_location)
    addLocationButton.setOnClickListener(addLocationClickListener)

    val addMediaButton: ImageView = view.findViewById(R.id.img_option_media)
    addMediaButton.setOnClickListener(addMediaClickListener)

    val clipboardButton: ImageView = view.findViewById(R.id.img_option_clipboard)
    clipboardButton.setOnClickListener(clipboardClickListener)

    val wordButton: ImageView = view.findViewById(R.id.img_option_words)
    wordButton.setOnClickListener(wordClickListener)
  }

  private fun initBackConfirmDialog() {
    backConfirmDialog = BinaryOptionDialog()
    backConfirmDialog.setTitle(resources.getString(R.string.warning_caps))
    backConfirmDialog.setMessage(resources.getString(R.string.go_back_warning))
    backConfirmDialog.setNegativeButton(resources.getString(R.string.go_back), confirmBackClickListener)
    backConfirmDialog.setPositiveButton(resources.getString(R.string.stay), cancelBackClickListener)
  }

  private val saveClickListener = View.OnClickListener {
    launch {
      val content = contentInput.text.toString()

      if(content.isBlank()) {
        contentInput.error = resources.getString(R.string.empty_content_warning)
      } else {
        val day = dateTimeStore.getDay()
        val month = dateTimeStore.getMonth()
        val year = dateTimeStore.getYear()
        val hour = dateTimeStore.getHour()
        val minute = dateTimeStore.getMinute()

        if(entryIdBeingEdited == 0) {
          viewModel.addEntry(day, month, year, hour, minute, content)
        } else {
          viewModel.updateEntry(day, month, year, hour, minute, content, entryIdBeingEdited)
        }

        popBackStack()
      }
    }
  }

  private val backClickListener = View.OnClickListener {
    onBackPress()
  }

  private fun onBackPress(): Boolean {
    if(contentInput.text.toString().isBlank()) {
      popBackStack()
    } else {
      backConfirmDialog.show(requireFragmentManager(), "Back Confirm Dialog")
    }
    return true
  }

  private val confirmBackClickListener = View.OnClickListener {
    backConfirmDialog.dismiss()
    popBackStack()
  }

  private val cancelBackClickListener = View.OnClickListener {
    backConfirmDialog.dismiss()
  }

  private val dateClickListener = View.OnClickListener {
    val dateDialog = EntryDatePickerDialog()
    dateDialog.bindDateTimeStore(dateTimeStore)
    dateDialog.show(requireFragmentManager(), "Date Picker")
  }

  private val timeClickListener = View.OnClickListener {
    val timeDialog = EntryTimePickerDialog()
    timeDialog.bindDateTimeStore(dateTimeStore)
    timeDialog.show(requireFragmentManager(), "Time Picker")
  }

  private val addTagClickListener = View.OnClickListener {
    val dialog = AddTagToEntryDialog()

    val bundle = Bundle()
    bundle.putInt(ENTRY_ID_KEY, entryIdBeingEdited)
    dialog.arguments = bundle

    dialog.show(requireFragmentManager(), "Add Tag Dialog")
  }

  private val addToBookClickListener = View.OnClickListener {
    val dialog = AddEntryToBookDialog()

    val bundle = Bundle()
    bundle.putInt(ENTRY_ID_KEY, entryIdBeingEdited)
    dialog.arguments = bundle

    dialog.show(requireFragmentManager(), "Add To Book Dialog")
  }

  private val addLocationClickListener = View.OnClickListener {
    Toast.makeText(requireContext(), resources.getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
  }

  private val addMediaClickListener = View.OnClickListener {
    Toast.makeText(requireContext(), resources.getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
  }

  private val clipboardClickListener = View.OnClickListener {
    val clipboard: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText("Entry", contentInput.text.toString())
    clipboard.primaryClip = clip
    Toast.makeText(requireContext(), resources.getString(R.string.copied_entry_to_clipboard), Toast.LENGTH_SHORT).show()
  }

  private val wordClickListener = View.OnClickListener {
    val dialog = NewWordDialog()

    val bundle = Bundle()
    bundle.putInt(ENTRY_ID_KEY, entryIdBeingEdited)
    dialog.arguments = bundle

    dialog.show(requireFragmentManager(), "New Words Dialog")
  }

  private fun focusInput() {
    contentInput.requestFocus()
    val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
  }

  private fun search(searchTerm: String) {
    val args = Bundle()
    args.putString(SEARCH_TERM_KEY, searchTerm)

    val searchDialog = SearchResultsDialog()
    searchDialog.arguments = args

    searchDialog.show(requireFragmentManager(), "Add Entry Search")
  }

  private fun initDateButton() {
    dateTimeStore.readableDate.observe(this@AddEntryFragment, Observer { date ->
      dateButton.text = date
    })
  }

  private fun initTimeButton() {
    dateTimeStore.readableTime.observe(this@AddEntryFragment, Observer { time ->
      timeButton.text = time
    })
  }

  private fun convertToEditMode(entryId: Int) = launch {
    toolbarTitle.text = resources.getString(R.string.edit_entry)
    entryIdBeingEdited = entryId
    viewModel.getEntry(entryId).observe(this@AddEntryFragment, Observer { entry ->
      contentInput.setText(entry.content)

      val day = entry.day
      val month = entry.month
      val year = entry.year
      val hour = entry.hour
      val minute = entry.minute

      dateTimeStore.setDate(day, month, year)
      dateTimeStore.setTime(hour, minute)
      dateButton.text = formatDateForDisplay(day, month, year)
      timeButton.text = formatTimeForDisplay(hour, minute)
    })
  }

  private fun popBackStack() {
    Navigation.findNavController(view!!).popBackStack()
  }
}
