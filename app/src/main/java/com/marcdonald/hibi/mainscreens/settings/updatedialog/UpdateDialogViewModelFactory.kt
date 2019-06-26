package com.marcdonald.hibi.mainscreens.settings.updatedialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.hibi.data.network.github.GithubAPIService

class UpdateDialogViewModelFactory(private val githubAPIService: GithubAPIService)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return UpdateDialogViewModel(githubAPIService) as T
  }
}