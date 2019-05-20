package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.Tag

interface TagRepository {

  suspend fun addTag(tag: Tag)

  suspend fun deleteTag(tagId: Int)

  suspend fun isTagInUse(tag: String): Boolean

  suspend fun getTagName(tagId: Int): String

  fun getAllTags(): LiveData<List<Tag>>
}