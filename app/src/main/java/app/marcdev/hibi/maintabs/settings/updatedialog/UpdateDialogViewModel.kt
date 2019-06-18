package app.marcdev.hibi.maintabs.settings.updatedialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.hibi.BuildConfig
import app.marcdev.hibi.data.network.github.GithubAPIService
import app.marcdev.hibi.data.network.github.apiresponse.GithubVersionResponse
import app.marcdev.hibi.internal.NoConnectivityException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateDialogViewModel(private val githubAPIService: GithubAPIService) : ViewModel() {
  private val _displayDismiss = MutableLiveData<Boolean>()
  val displayDismiss: LiveData<Boolean>
    get() = _displayDismiss

  private val _displayOpenButton = MutableLiveData<Boolean>()
  val displayOpenButton: LiveData<Boolean>
    get() = _displayOpenButton

  private val _displayLoading = MutableLiveData<Boolean>()
  val displayLoading: LiveData<Boolean>
    get() = _displayLoading

  private val _displayNoConnection = MutableLiveData<Boolean>()
  val displayNoConnection: LiveData<Boolean>
    get() = _displayNoConnection

  private val _displayNoUpdateAvailable = MutableLiveData<Boolean>()
  val displayNoUpdateAvailable: LiveData<Boolean>
    get() = _displayNoUpdateAvailable

  private val _newVersionName = MutableLiveData<String>()
  val newVersionName: LiveData<String>
    get() = _newVersionName

  private val _displayError = MutableLiveData<Boolean>()
  val displayError: LiveData<Boolean>
    get() = _displayError

  init {
    _displayOpenButton.value = false
    _displayNoConnection.value = false
    _displayNoUpdateAvailable.value = false
    _displayDismiss.value = true
  }

  fun check() {
    viewModelScope.launch(Dispatchers.Default) {
      _displayLoading.postValue(true)
      performCheck()
    }
  }

  private suspend fun performCheck() {
    try {
      val newestVersion = githubAPIService.getNewestVersion()
      checkIfVersionIsNewer(newestVersion)
    } catch(e: NoConnectivityException) {
      _displayLoading.postValue(false)
      _displayNoConnection.postValue(true)
    }
  }

  private fun checkIfVersionIsNewer(newestVersion: GithubVersionResponse) {
    val semicolonIndex = newestVersion.tag_name.indexOf(';')
    try {
      val newestVersionCode = newestVersion.tag_name.substring(semicolonIndex + 1)
      _displayLoading.postValue(false)
      if(newestVersionCode.toInt() > BuildConfig.VERSION_CODE) {
        _newVersionName.postValue(newestVersion.name)
        _displayOpenButton.postValue(true)
        _displayNoUpdateAvailable.postValue(false)
      } else {
        _displayNoUpdateAvailable.postValue(true)
      }
    } catch(e: NumberFormatException) {
      _displayError.postValue(true)
    }
  }
}