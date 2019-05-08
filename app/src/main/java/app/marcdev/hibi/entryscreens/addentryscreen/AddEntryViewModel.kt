package app.marcdev.hibi.entryscreens.addentryscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.marcdev.hibi.data.entity.BookEntryRelation
import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.TagEntryRelation
import app.marcdev.hibi.data.repository.BookEntryRelationRepository
import app.marcdev.hibi.data.repository.EntryRepository
import app.marcdev.hibi.data.repository.NewWordRepository
import app.marcdev.hibi.data.repository.TagEntryRelationRepository

class AddEntryViewModel(private val entryRepository: EntryRepository, private val tagEntryRelationRepository: TagEntryRelationRepository, private val bookEntryRelationRepository: BookEntryRelationRepository, private val newWordRepository: NewWordRepository) : ViewModel() {

  init {
    TagsToSaveToNewEntry.list.clear()
    NewWordsToSaveToNewEntry.clearList()
  }

  suspend fun addEntry(day: Int, month: Int, year: Int, hour: Int, minute: Int, content: String) {
    entryRepository.addEntry(Entry(day, month, year, hour, minute, content))
    val id = entryRepository.getLastEntryId()
    TagsToSaveToNewEntry.list.forEach {
      val tagEntryRelation = TagEntryRelation(it, id)
      tagEntryRelationRepository.addTagEntryRelation(tagEntryRelation)
    }
    BooksToSaveToNewEntry.list.forEach {
      val bookEntryRelation = BookEntryRelation(it, id)
      bookEntryRelationRepository.addBookEntryRelation(bookEntryRelation)
    }
    if(NewWordsToSaveToNewEntry.list.value != null) {
      NewWordsToSaveToNewEntry.list.value!!.forEach {
        it.entryId = id
        newWordRepository.addNewWord(it)
      }
    }
  }

  suspend fun getEntry(id: Int): LiveData<Entry> {
    return entryRepository.getEntry(id)
  }

  suspend fun updateEntry(day: Int, month: Int, year: Int, hour: Int, minute: Int, content: String, entryId: Int) {
    val entryToUpdate = Entry(day, month, year, hour, minute, content)
    entryToUpdate.id = entryId
    entryRepository.addEntry(entryToUpdate)
  }
}