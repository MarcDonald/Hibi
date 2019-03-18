package app.marcdev.hibi.maintabs.mainentriesrecycler

import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.TagEntryRelation

data class MainEntriesDisplayItem(var entry: Entry, var tagEntryRelations: List<TagEntryRelation>)