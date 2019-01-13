package app.marcdev.hibi.mainscreen.mainscreenrecycler

import app.marcdev.hibi.data.entity.Entry
import app.marcdev.hibi.data.entity.TagEntryRelation

data class MainScreenDisplayItem(var entry: Entry, var tagEntryRelations: List<TagEntryRelation>)