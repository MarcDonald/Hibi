package app.marcdev.hibi.entryscreens.addentryscreen

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.entryscreens.ImageRecyclerAdapter
import app.marcdev.hibi.internal.*
import app.marcdev.hibi.search.searchresults.SearchResultsDialog
import app.marcdev.hibi.uicomponents.BinaryOptionDialog
import app.marcdev.hibi.uicomponents.DatePickerDialog
import app.marcdev.hibi.uicomponents.TimePickerDialog
import app.marcdev.hibi.uicomponents.addentrytobookdialog.AddEntryToBookDialog
import app.marcdev.hibi.uicomponents.addtagtoentrydialog.AddTagToEntryDialog
import app.marcdev.hibi.uicomponents.locationdialog.AddLocationToEntryDialog
import app.marcdev.hibi.uicomponents.newwordsdialog.NewWordDialog
import app.marcdev.hibi.uicomponents.views.SearchBar
import com.google.android.material.button.MaterialButton
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AddEntryFragment : Fragment(), KodeinAware {
  override val kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: AddEntryViewModelFactory by instance()
  private lateinit var viewModel: AddEntryViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var dateButton: MaterialButton
  private lateinit var timeButton: MaterialButton
  private lateinit var contentInput: EditText
  private lateinit var backConfirmDialog: BinaryOptionDialog
  private lateinit var toolbarTitle: TextView
  private lateinit var searchBar: SearchBar
  private lateinit var dateDialog: DatePickerDialog
  private lateinit var timeDialog: TimePickerDialog
  private lateinit var imageRecyclerAdapter: ImageRecyclerAdapter
  // <editor-fold desc="Option Bar Buttons">
  private lateinit var addTagButton: ImageView
  private lateinit var addToBookButton: ImageView
  private lateinit var addMediaButton: ImageView
  private lateinit var addLocationButton: ImageView
  private lateinit var wordButton: ImageView
  private lateinit var clipboardButton: ImageView
  // </editor-fold>
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEntryViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_add_entry, container, false)
    bindViews(view)
    initBackConfirmDialog()
    focusInput()
    requireActivity().addOnBackPressedCallback(this, OnBackPressedCallback {
      viewModel.backPress(contentInput.text.toString().isEmpty())
      true
    })
    setupObservers()
    setupImageRecycler(view)
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    arguments?.let { arguments ->
      viewModel.passArgument(AddEntryFragmentArgs.fromBundle(arguments).entryId)
    }
  }

  private fun bindViews(view: View) {
    initClipboardButton(view)
    toolbarTitle = view.findViewById(R.id.txt_add_toolbar_title)

    searchBar = view.findViewById(R.id.searchbar_add_entry)
    searchBar.setSearchAction(this::search)

    dateButton = view.findViewById(R.id.btn_date)
    dateButton.setOnClickListener(dateClickListener)

    timeButton = view.findViewById(R.id.btn_time)
    timeButton.setOnClickListener(timeClickListener)

    contentInput = view.findViewById(R.id.edt_content)

    val saveButton: MaterialButton = view.findViewById(R.id.btn_save)
    saveButton.setOnClickListener(saveClickListener)

    val backButton: ImageView = view.findViewById(R.id.img_add_entry_toolbar_back)
    backButton.setOnClickListener(backClickListener)

    // <editor-fold desc="Option Bar Buttons">
    addTagButton = view.findViewById(R.id.img_option_tag)
    addTagButton.setOnClickListener(addTagClickListener)

    addToBookButton = view.findViewById(R.id.img_option_book)
    addToBookButton.setOnClickListener(addToBookClickListener)

    addLocationButton = view.findViewById(R.id.img_option_location)
    addLocationButton.setOnClickListener(addLocationClickListener)

    addMediaButton = view.findViewById(R.id.img_option_media)
    addMediaButton.setOnClickListener(addMediaClickListener)

    wordButton = view.findViewById(R.id.img_option_words)
    wordButton.setOnClickListener(wordClickListener)
    // </editor-fold>
  }

  private fun initClipboardButton(view: View) {
    clipboardButton = view.findViewById(R.id.img_option_clipboard)
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    when(PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(PREF_CLIPBOARD_BEHAVIOR, "0").toInt()) {
      0 -> {
        clipboardButton.setImageResource(R.drawable.ic_clipboard_plus)
        clipboardButton.setOnClickListener { showClipBoardMenu() }
      }
      1 -> {
        clipboardButton.setImageResource(R.drawable.ic_content_copy_24dp)
        clipboardButton.setOnClickListener { copyToClipboard() }
        clipboardButton.setOnLongClickListener { pasteFromClipboard(); true }
      }
      2 -> {
        clipboardButton.setImageResource(R.drawable.ic_content_paste_24dp)
        clipboardButton.setOnClickListener { pasteFromClipboard() }
        clipboardButton.setOnLongClickListener { copyToClipboard(); true }
      }
    }
  }

  private fun setupObservers() {
    viewModel.displayEmptyContentWarning.observe(this, Observer { value ->
      value?.let { show ->
        if(show)
          contentInput.error = resources.getString(R.string.empty_content_warning)
      }
    })

    viewModel.dateTimeStore.readableDate.observe(this, Observer { date ->
      date?.let {
        dateButton.text = date
      }
    })

    viewModel.dateTimeStore.readableTime.observe(this, Observer { time ->
      time?.let {
        timeButton.text = time
      }
    })

    viewModel.popBackStack.observe(this, Observer { pop ->
      pop?.let {
        if(pop)
          popBackStack()
      }
    })

    viewModel.isEditMode.observe(this, Observer { value ->
      value?.let { isEditMode ->
        if(isEditMode)
          toolbarTitle.text =
            if(isEditMode)
              resources.getString(R.string.edit_entry)
            else
              resources.getString(R.string.add_entry)
      }
    })

    viewModel.entry.observe(this, Observer { entry ->
      entry?.let {
        contentInput.setText(entry.content)
      }
    })

    viewModel.displayBackWarning.observe(this, Observer { value ->
      value?.let { display ->
        if(display)
          backConfirmDialog.show(requireFragmentManager(), "Back Confirm Dialog")
        else
          backConfirmDialog.dismiss()
      }
    })

    viewModel.startObservingEntrySpecificItems.observe(this, Observer { entry ->
      entry?.let { observe ->
        if(observe)
          setupEntrySpecificObservers()
      }
    })
  }

  /**
   * This has to be called after the original because the entryId isn't always provided immediately
   */
  private fun setupEntrySpecificObservers() {
    viewModel.colorTagIcon.observe(this, Observer { entry ->
      entry?.let { shouldColor ->
        colorImageDrawable(addTagButton, shouldColor)
      }
    })

    viewModel.colorBookIcon.observe(this, Observer { entry ->
      entry?.let { shouldColor ->
        colorImageDrawable(addToBookButton, shouldColor)
      }
    })

    viewModel.colorLocationIcon.observe(this, Observer { entry ->
      entry?.let { shouldColor ->
        colorImageDrawable(addLocationButton, shouldColor)
      }
    })

    viewModel.colorNewWordIcon.observe(this, Observer { entry ->
      entry?.let { shouldColor ->
        colorImageDrawable(wordButton, shouldColor)
      }
    })

    viewModel.colorImagesIcon.observe(this, Observer { entry ->
      entry?.let { shouldColor ->
        colorImageDrawable(addMediaButton, shouldColor)
      }
    })

    viewModel.images.observe(this, Observer { entry ->
      entry?.let { imagePaths ->
        imageRecyclerAdapter.updateItems(imagePaths)
      }
    })
  }

  private fun colorImageDrawable(imageView: ImageView, shouldColor: Boolean) {
    if(shouldColor) {
      val accentColor = if(PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(PREF_DARK_THEME, false))
        resources.getColor(R.color.darkThemeColorAccent, null)
      else
        resources.getColor(R.color.lightThemeColorAccent, null)

      imageView.setColorFilter(accentColor)
    } else {
      imageView.clearColorFilter()
    }
  }

  private fun setupImageRecycler(view: View) {
    val recycler: RecyclerView = view.findViewById(R.id.recycler_add_entry_images)
    this.imageRecyclerAdapter = ImageRecyclerAdapter({}, ::onImageLongClick, requireContext(), requireActivity().theme)
    val layoutManager = GridLayoutManager(context, 3)
    recycler.adapter = imageRecyclerAdapter
    recycler.layoutManager = layoutManager
  }

  private fun onImageLongClick(imagePath: String) {
    val deleteImageConfirmDialog = BinaryOptionDialog()
    deleteImageConfirmDialog.setTitle(resources.getString(R.string.warning))
    deleteImageConfirmDialog.setMessage(resources.getString(R.string.delete_image))
    deleteImageConfirmDialog.setNegativeButton(resources.getString(R.string.delete), View.OnClickListener {
      viewModel.removeImage(imagePath)
      deleteImageConfirmDialog.dismiss()
    })
    deleteImageConfirmDialog.setPositiveButton(resources.getString(R.string.cancel), View.OnClickListener {
      deleteImageConfirmDialog.dismiss()
    })
    deleteImageConfirmDialog.show(requireFragmentManager(), "Delete Image Confirm Dialog")
  }

  private fun initBackConfirmDialog() {
    backConfirmDialog = BinaryOptionDialog()
    backConfirmDialog.setTitle(resources.getString(R.string.warning))
    backConfirmDialog.setMessage(resources.getString(R.string.go_back_warning))
    backConfirmDialog.setNegativeButton(resources.getString(R.string.go_back), View.OnClickListener { viewModel.confirmBack() })
    backConfirmDialog.setPositiveButton(resources.getString(R.string.stay), View.OnClickListener { backConfirmDialog.dismiss() })
  }

  private val saveClickListener = View.OnClickListener {
    viewModel.savePress(contentInput.text.toString())
  }

  private val backClickListener = View.OnClickListener {
    viewModel.backPress(contentInput.text.toString().isBlank())
  }

  private val dateClickListener = View.OnClickListener {
    dateDialog = DatePickerDialog
      .Builder()
      .setOkClickListener(dateOkOnClickListener)
      .setCancelClickListener(dateCancelOnClickListener)
      .initDatePicker(
        viewModel.dateTimeStore.year,
        viewModel.dateTimeStore.month,
        viewModel.dateTimeStore.day,
        null)
      .build()
    dateDialog.show(requireFragmentManager(), "Date Picker")
  }

  private val dateCancelOnClickListener = View.OnClickListener {
    dateDialog.dismiss()
  }

  private val dateOkOnClickListener = View.OnClickListener {
    val day = dateDialog.day
    val month = dateDialog.month
    val year = dateDialog.year

    viewModel.dateTimeStore.setDate(day, month, year)
    dateDialog.dismiss()
  }

  private val timeClickListener = View.OnClickListener {
    timeDialog = TimePickerDialog.Builder()
      .setIs24HourView(true)
      .setOkClickListener(timeOkClickListener)
      .setCancelClickListener(timeCancelClickListener)
      .initTimePicker(viewModel.dateTimeStore.hour, viewModel.dateTimeStore.minute, null)
      .build()
    timeDialog.show(requireFragmentManager(), "Time Picker")
  }

  private val timeOkClickListener = View.OnClickListener {
    viewModel.dateTimeStore.setTime(timeDialog.hour, timeDialog.minute)
    timeDialog.dismiss()
  }

  private val timeCancelClickListener = View.OnClickListener {
    timeDialog.dismiss()
  }

  private val addTagClickListener = View.OnClickListener {
    val dialog = AddTagToEntryDialog()

    val bundle = Bundle()
    bundle.putInt(ENTRY_ID_KEY, viewModel.entryId)
    dialog.arguments = bundle

    dialog.show(requireFragmentManager(), "Add Tag Dialog")
  }

  private val addToBookClickListener = View.OnClickListener {
    val dialog = AddEntryToBookDialog()

    val bundle = Bundle()
    bundle.putInt(ENTRY_ID_KEY, viewModel.entryId)
    dialog.arguments = bundle

    dialog.show(requireFragmentManager(), "Add To Book Dialog")
  }

  private val addLocationClickListener = View.OnClickListener {
    val dialog = AddLocationToEntryDialog()

    val bundle = Bundle()
    bundle.putInt(ENTRY_ID_KEY, viewModel.entryId)
    dialog.arguments = bundle

    dialog.show(requireFragmentManager(), "Add Location to Entry Dialog")
  }

  private val addMediaClickListener = View.OnClickListener {
    if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      askForStoragePermissions()
    } else {
      FilePickerBuilder.instance
        .setActivityTheme(R.style.AppTheme_Dark)
        .setActivityTitle(resources.getString(R.string.add_images))
        .setSelectedFiles(arrayListOf())
        .pickPhoto(this, CHOOSE_IMAGE_TO_ADD_REQUEST_CODE)
    }
  }

  private fun askForStoragePermissions() {
    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if(requestCode == CHOOSE_IMAGE_TO_ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
      val photoPathArray = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)
      viewModel.addImages(photoPathArray)
    }
  }

  private fun showClipBoardMenu() {
    val popupMenu = PopupMenu(context, clipboardButton)
    popupMenu.menuInflater.inflate(R.menu.menu_clipboard, popupMenu.menu)
    popupMenu.setOnMenuItemClickListener { menuItem ->
      when(menuItem.itemId) {
        popupMenu.menu.getItem(0).itemId -> {
          copyToClipboard()
          Toast.makeText(requireContext(), resources.getString(R.string.copied_entry_to_clipboard), Toast.LENGTH_SHORT).show()
        }

        popupMenu.menu.getItem(1).itemId -> pasteFromClipboard()
      }
      return@setOnMenuItemClickListener true
    }
    popupMenu.show()
  }

  private fun copyToClipboard() {
    val clipboard: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText("Entry", contentInput.text.toString())
    clipboard.primaryClip = clip
    Toast.makeText(requireContext(), resources.getString(R.string.copied_entry_to_clipboard), Toast.LENGTH_SHORT).show()
  }

  private fun pasteFromClipboard() {
    val clipboard: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = clipboard.primaryClip
    if(clip != null) {
      val clipText = clip.getItemAt(0).text
      val cursorPoint = contentInput.selectionStart
      contentInput.text.insert(cursorPoint, clipText)
    } else {
      Toast.makeText(requireContext(), resources.getString(R.string.nothing_in_clipboard), Toast.LENGTH_SHORT).show()
    }
  }

  private val wordClickListener = View.OnClickListener {
    val dialog = NewWordDialog()

    val bundle = Bundle()
    bundle.putInt(ENTRY_ID_KEY, viewModel.entryId)
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

  private fun popBackStack() {
    Navigation.findNavController(requireView()).popBackStack()
  }

  override fun onPause() {
    viewModel.pause(contentInput.text.toString())
    super.onPause()
  }
}
