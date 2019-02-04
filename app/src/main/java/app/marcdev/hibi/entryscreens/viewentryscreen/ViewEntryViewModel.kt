package app.marcdev.hibi.entryscreens.viewentryscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.repository.EntryRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository

class ViewEntryViewModel(private val entryRepository: EntryRepository, private val tagEntryRelationRepository: TagEntryRelationRepository) : ViewModel() {

  suspend fun getEntry(id: Int): LiveData<Entry> {
    return entryRepository.getEntry(id)
  }

  suspend fun deleteEntry(id: Int) {
    return entryRepository.deleteEntry(id)
  }

  suspend fun getTags(id: Int): LiveData<List<String>> {
    return tagEntryRelationRepository.getTagsWithEntry(id)
  }
}