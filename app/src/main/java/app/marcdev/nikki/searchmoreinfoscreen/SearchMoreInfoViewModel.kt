package app.marcdev.nikki.searchmoreinfoscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.marcdev.nikki.data.network.apiresponse.Japanese
import app.marcdev.nikki.data.network.apiresponse.Sense
import com.google.gson.Gson
import java.util.*

class SearchMoreInfoViewModel : ViewModel() {

  var japaneseJsonList = arrayListOf<String>()
  var senseJsonList = arrayListOf<String>()

  val japaneseList by lazy {
    getJapaneseObjectList(japaneseJsonList)
  }

  val senseList by lazy {
    getSensesObjectList(senseJsonList)
  }

  private fun getJapaneseObjectList(jsonList: ArrayList<String>?): LiveData<List<Japanese>> {
    val returnValue = MutableLiveData<List<Japanese>>()
    val objectList = mutableListOf<Japanese>()

    jsonList?.let {
      jsonList.forEach { json ->
        val japaneseObject = Gson().fromJson(json, Japanese::class.java)
        objectList.add(japaneseObject)
      }
    }
    returnValue.value = objectList

    return returnValue
  }

  private fun getSensesObjectList(jsonList: ArrayList<String>?): LiveData<List<Sense>> {
    val returnValue = MutableLiveData<List<Sense>>()
    val objectList = mutableListOf<Sense>()

    jsonList?.let {
      jsonList.forEach { json ->
        val senseObject = Gson().fromJson(json, Sense::class.java)
        objectList.add(senseObject)
      }
    }
    returnValue.value = objectList

    return returnValue
  }
}