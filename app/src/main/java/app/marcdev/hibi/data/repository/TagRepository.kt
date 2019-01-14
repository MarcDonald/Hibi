package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.Tag

interface TagRepository {

  suspend fun addTag(tag: Tag)

  suspend fun getTag(tag: String): LiveData<Tag>

  suspend fun getAllTags(): LiveData<List<Tag>>

  suspend fun deleteTag(tag: String)
}