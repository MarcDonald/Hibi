package app.marcdev.hibi.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.hibi.data.entity.Tag

interface TagRepository {

  suspend fun addTag(tag: Tag)

  suspend fun getTagByName(tag: String): LiveData<Tag>

  suspend fun getTagById(id: Int): Tag

  suspend fun getAllTags(): LiveData<List<Tag>>

  suspend fun deleteTag(tag: String)

  suspend fun isTagInUse(tag: String): Boolean

  suspend fun getTagName(tagId: Int): String
}