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

  // <editor-fold desc="View Model">
  private val viewModelFactory: SearchMoreInfoViewModelFactory by instance()
  private lateinit var viewModel: SearchMoreInfoViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var mainWordDisplay: TextView
  private lateinit var mainReadingDisplay: TextView
  private lateinit var senseTitle: TextView
  private lateinit var alternativeTitle: TextView
  // </editor-fold>

  // <editor-fold desc="Recycler Views">
  private lateinit var alternativeRecycler: RecyclerView
  private lateinit var alternativesRecyclerAdapter: SearchMoreInfoAlternativesRecyclerAdapter
  private lateinit var senseRecycler: RecyclerView
  private lateinit var senseRecyclerAdapter: SearchMoreInfoSenseRecyclerAdapter
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchMoreInfoViewModel::class.java)

    arguments?.let {
      val japaneseListJson = requireArguments().getStringArrayList("japaneseList")
      val sensesListJson = requireArguments().getStringArrayList("sensesList")
      viewModel.passArguments(japaneseListJson as ArrayList<String>, sensesListJson as ArrayList<String>)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    Timber.v("Log: onCreateView: Started")
    val view = inflater.inflate(R.layout.dialog_search_more_info, container, false)
    bindViews(view)
    initAlternativesRecycler()
    initSenseRecycler()
    setupObservers()
    return view
  }

  private fun setupObservers() {
    viewModel.mainWord.observe(this, Observer { value ->
      value?.let { word ->
        mainWordDisplay.text = resources.getString(R.string.japanese_word_wc, word)
      }
    })

    viewModel.mainReading.observe(this, Observer { value ->
      value?.let { reading ->
        mainReadingDisplay.text = resources.getString(R.string.japanese_word_wc, reading)
      }
    })

    viewModel.displayMainReading.observe(this, Observer { value ->
      value?.let { show ->
        mainReadingDisplay.visibility = if(show) View.VISIBLE else View.GONE
      }
    })

    viewModel.displayAlternatives.observe(this, Observer { value ->
      value?.let { show ->
        if(show) {
          alternativeRecycler.visibility = View.VISIBLE
          alternativeTitle.visibility = View.VISIBLE
        } else {
          alternativeRecycler.visibility = View.GONE
          alternativeTitle.visibility = View.GONE
        }
      }
    })

    viewModel.alternatives.observe(this, Observer { value ->
      value?.let { list ->
        alternativesRecyclerAdapter.updateList(list)
      }
    })

    viewModel.displaySense.observe(this, Observer { value ->
      value?.let { show ->
        if(show) {
          senseRecycler.visibility = View.VISIBLE
          senseTitle.visibility = View.VISIBLE
        } else {
          senseRecycler.visibility = View.GONE
          senseTitle.visibility = View.GONE
        }
      }
    })

    viewModel.senseList.observe(this, Observer { value ->
      value?.let { list ->
        senseRecyclerAdapter.updateList(list)
      }
    })
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
    viewModel.mainWord.value?.let {
      val clipboard: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
      val clip: ClipData = ClipData.newPlainText("Main Word", viewModel.mainWord.value)
      clipboard.primaryClip = clip

      val toastMessage = resources.getString(R.string.copied_to_clipboard_wc, viewModel.mainWord.value)
      Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }
  }

  private val mainReadingClickListener = View.OnClickListener {
    viewModel.mainReading.value?.let {
      val clipboard: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
      val clip: ClipData = ClipData.newPlainText("Main Reading", viewModel.mainReading.value)
      clipboard.primaryClip = clip

      val toastMessage = resources.getString(R.string.copied_to_clipboard_wc, viewModel.mainReading.value)
      Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }
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
}