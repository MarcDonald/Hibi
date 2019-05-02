package app.marcdev.hibi.search.searchmoreinfoscreen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.base.HibiDialogFragment
import app.marcdev.hibi.search.searchmoreinfoscreen.alternativesrecycler.SearchMoreInfoAlternativesRecyclerAdapter
import app.marcdev.hibi.search.searchmoreinfoscreen.senserecycler.SearchMoreInfoSenseRecyclerAdapter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class SearchMoreInfoDialog : HibiDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // Viewmodel
  private val viewModelFactory: SearchMoreInfoViewModelFactory by instance()
  private lateinit var viewModel: SearchMoreInfoViewModel

  // UI Components
  private lateinit var mainWordDisplay: TextView
  private lateinit var mainReadingDisplay: TextView
  private lateinit var senseTitle: TextView
  private lateinit var alternativeTitle: TextView

  // Recycler Views
  private lateinit var alternativeRecycler: RecyclerView
  private lateinit var alternativesRecyclerAdapter: SearchMoreInfoAlternativesRecyclerAdapter
  private lateinit var senseRecycler: RecyclerView
  private lateinit var senseRecyclerAdapter: SearchMoreInfoSenseRecyclerAdapter

  // Other
  private var mainWordContent = ""
  private var mainReadingContent = ""

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    /* Normally viewmodel is instantiated in onActivityCreated but that seems to crash for this
     * screen so it's instantiated here instead */
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchMoreInfoViewModel::class.java)
    val view = inflater.inflate(R.layout.dialog_search_more_info, container, false)
    bindViews(view)
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    arguments?.let {
      val japaneseListJson = arguments!!.getStringArrayList("japaneseList")
      val sensesListJson = arguments!!.getStringArrayList("sensesList")

      viewModel.japaneseJsonList = japaneseListJson as ArrayList<String>
      viewModel.senseJsonList = sensesListJson as ArrayList<String>
    }
    initAlternativesRecycler()
    initSenseRecycler()

    observeSense()
    observeJapanese()
  }

  private fun bindViews(view: View) {
    mainWordDisplay = view.findViewById(R.id.txt_search_more_info_main_word)
    mainWordDisplay.setOnClickListener(mainWordClickListener)

    mainReadingDisplay = view.findViewById(R.id.txt_search_more_info_main_reading)
    mainReadingDisplay.setOnClickListener(mainReadingClickListener)

    senseTitle = view.findViewById(R.id.txt_search_more_info_sense_title)
    senseRecycler = view.findViewById(R.id.recycler_search_more_info_sense)

    alternativeTitle = view.findViewById(R.id.txt_search_more_info_alternative_title)
    alternativeRecycler = view.findViewById(R.id.recycler_search_more_info_alternative_japanese)
  }

  private val mainWordClickListener = View.OnClickListener {
    val clipboard: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText("Main Word", mainWordContent)
    clipboard.primaryClip = clip

    val toastMessage = resources.getString(R.string.copied_to_clipboard_wc, mainWordContent)
    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
  }

  private val mainReadingClickListener = View.OnClickListener {
    val clipboard: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText("Main Reading", mainReadingContent)
    clipboard.primaryClip = clip

    val toastMessage = resources.getString(R.string.copied_to_clipboard_wc, mainReadingContent)
    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
  }

  private fun initAlternativesRecycler() {
    this.alternativesRecyclerAdapter = SearchMoreInfoAlternativesRecyclerAdapter(requireContext())
    val layoutManager = LinearLayoutManager(context)
    alternativeRecycler.adapter = alternativesRecyclerAdapter
    alternativeRecycler.layoutManager = layoutManager

    val dividerItemDecoration = DividerItemDecoration(alternativeRecycler.context, layoutManager.orientation)
    dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider_standard, null))
    alternativeRecycler.addItemDecoration(dividerItemDecoration)
  }

  private fun initSenseRecycler() {
    this.senseRecyclerAdapter = SearchMoreInfoSenseRecyclerAdapter(requireContext())
    val layoutManager = LinearLayoutManager(context)
    senseRecycler.adapter = senseRecyclerAdapter
    senseRecycler.layoutManager = layoutManager

    val dividerItemDecoration = DividerItemDecoration(senseRecycler.context, layoutManager.orientation)
    dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider_standard, null))
    senseRecycler.addItemDecoration(dividerItemDecoration)
  }

  private fun observeJapanese() {
    val japanese = viewModel.japaneseList
    japanese.observe(this@SearchMoreInfoDialog, Observer { list ->

      if(list.isNotEmpty()) {
        val mainWord: String? = list[0].word
        val mainReading: String? = list[0].reading

        // If no mainWord is supplied, use the reading as the main word and hide the reading display
        if(mainWord == null || mainWord.isBlank()) {
          mainWordDisplay.text = resources.getString(R.string.japanese_word_wc, mainReading)
          mainReadingDisplay.visibility = View.GONE
          mainReading?.let {
            mainWordContent = mainReading
          }
        } else {
          // If a mainWord is displayed, just it as the main word
          mainWordDisplay.text = resources.getString(R.string.japanese_word_wc, mainWord)
          mainWordContent = mainWord

          if(mainReading == null || mainReading.isBlank()) {
            // If no mainReading is supplied then hide the reading field
            mainReadingDisplay.visibility = View.GONE
          } else {
            // Otherwise use the reading
            mainReadingDisplay.text = resources.getString(R.string.reading_wc, mainReading)
            mainReadingContent = mainReading
          }
        }
      }

      // If there is more than one result then display all the others as alternatives
      if(list.size > 1) {
        val listExcludingMainResult = list.subList(1, list.size)
        alternativesRecyclerAdapter.updateList(listExcludingMainResult)
      } else {
        // Otherwise hide the alternative UI components
        alternativeRecycler.visibility = View.GONE
        alternativeTitle.visibility = View.GONE
      }
    })
  }

  private fun observeSense() {
    val sense = viewModel.senseList
    sense.observe(this@SearchMoreInfoDialog, Observer { list ->
      if(list.isNotEmpty()) {
        senseRecyclerAdapter.updateList(list)
      } else {
        senseRecycler.visibility = View.GONE
        senseTitle.visibility = View.GONE
      }
    })
  }
}