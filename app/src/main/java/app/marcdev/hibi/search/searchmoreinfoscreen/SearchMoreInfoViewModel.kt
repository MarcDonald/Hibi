package app.marcdev.hibi.search.searchmoreinfoscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.network.apiresponse.Japanese
import app.marcdev.hibi.data.network.apiresponse.Sense
import com.google.gson.Gson

class SearchMoreInfoViewModel : ViewModel() {
  private var _senseList = MutableLiveData<List<Sense>>()
  val senseList: LiveData<List<Sense>>
    get() = _senseList

  private var _alternatives = MutableLiveData<List<Japanese>>()
  val alternatives: LiveData<List<Japanese>>
    get() = _alternatives

  private var _displayAlternatives = MutableLiveData<Boolean>()
  val displayAlternatives: LiveData<Boolean>
    get() = _displayAlternatives

  private var _displaySense = MutableLiveData<Boolean>()
  val displaySense: LiveData<Boolean>
    get() = _displaySense

  private var _mainWord = MutableLiveData<String>()
  val mainWord: LiveData<String>
    get() = _mainWord

  private var _mainReading = MutableLiveData<String>()
  val mainReading: LiveData<String>
    get() = _mainReading

  private var _displayMainReading = MutableLiveData<Boolean>()
  val displayMainReading: LiveData<Boolean>
    get() = _displayMainReading

  fun passArguments(japaneseListArg: ArrayList<String>, sensesListArg: ArrayList<String>) {
    getJapaneseObjectList(japaneseListArg)
    getSensesObjectList(sensesListArg)
  }

  private fun getJapaneseObjectList(jsonList: ArrayList<String>?) {
    val objectList = mutableListOf<Japanese>()

    jsonList?.let {
      jsonList.forEach { json ->
        val japaneseObject = Gson().fromJson(json, Japanese::class.java)
        objectList.add(japaneseObject)
      }
    }

    if(objectList.isNotEmpty()) {
      val mainWord: String? = objectList[0].word
      val mainReading: String? = objectList[0].reading

      // If no mainWord is supplied, use the reading as the main word and hide the reading display
      if(mainWord == null || mainWord.isBlank()) {
        _mainWord.value = mainReading
        _displayMainReading.value = false

        mainReading?.let {
          _mainWord.value = mainReading
        }
      } else {
        // If a mainWord is displayed, just use it as the main word
        _mainWord.value = mainWord

        if(mainReading == null || mainReading.isBlank()) {
          // If no mainReading is supplied then hide the reading field
          _displayMainReading.value = false
        } else {
          // Otherwise use the reading
          _mainReading.value = mainReading
        }
      }
    }

    // If there is more than one result then display all the others as alternatives
    if(objectList.size > 1) {
      val listExcludingMainResult = objectList.subList(1, objectList.size)
      _alternatives.value = listExcludingMainResult
    } else {
      // Otherwise hide the alternative UI components
      _displayAlternatives.value = false
    }
  }

  private fun getSensesObjectList(jsonList: ArrayList<String>?) {
    val objectList = mutableListOf<Sense>()

    jsonList?.let {
      jsonList.forEach { json ->
        val senseObject = Gson().fromJson(json, Sense::class.java)
        objectList.add(senseObject)
      }
    }

    if(objectList.isNotEmpty()) {
      _senseList.value = objectList.toList()
    } else {
      _displaySense.value = false
    }
  }
}