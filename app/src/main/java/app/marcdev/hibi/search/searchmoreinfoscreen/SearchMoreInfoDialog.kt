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
import app.marcdev.hibi.internal.base.ScopedDialogFragment
import app.marcdev.hibi.search.searchmoreinfoscreen.alternativesrecycler.SearchMoreInfoAlternativesRecyclerAdapter
import app.marcdev.hibi.search.searchmoreinfoscreen.senserecycler.SearchMoreInfoSenseRecyclerAdapter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

class SearchMoreInfoDialog : ScopedDialogFragment(), KodeinAware {
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

    val toastMessage = resources.getString(R.string.copied_to_clipboard, mainWordContent)
    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
  }

  private val mainReadingClickListener = View.OnClickListener {
    val clipboard: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText("Main Reading", mainReadingContent)
    clipboard.primaryClip = clip

    val toastMessage = resources.getString(R.string.copied_to_clipboard, mainReadingContent)
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
        val mainWordString = resources.getString(R.string.japanese_word_with_output, mainWord)
        val mainReadingString = resources.getString(R.string.reading_with_output, mainReading)

        if(mainWord != null && mainWord.isNotBlank()) {
          mainWordDisplay.text = mainWordString
          mainWordContent = mainWord
        } else {
          mainWordDisplay.visibility = View.GONE
        }

        if(mainReading != null && mainReading.isNotBlank()) {
          mainReadingDisplay.text = mainReadingString
          mainReadingContent = mainReading
        } else {
          mainReadingDisplay.visibility = View.GONE
        }
      }

      if(list.size > 1) {
        val listExcludingMainResult = list.subList(1, list.size)
        alternativesRecyclerAdapter.updateList(listExcludingMainResult)
      } else {
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