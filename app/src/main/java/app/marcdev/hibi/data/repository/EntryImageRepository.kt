package app.marcdev.hibi.data.repository

import app.marcdev.hibi.data.entity.EntryImage

interface EntryImageRepository {

  suspend fun addEntryImage(entryImage: EntryImage)

  suspend fun deleteEntryImage(entryImage: EntryImage)
}