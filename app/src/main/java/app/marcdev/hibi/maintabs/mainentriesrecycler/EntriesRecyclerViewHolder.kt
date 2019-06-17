package app.marcdev.hibi.maintabs.mainentriesrecycler

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import app.marcdev.hibi.R
import app.marcdev.hibi.internal.extension.show
import app.marcdev.hibi.internal.utils.formatDateForDisplay
import app.marcdev.hibi.internal.utils.formatTimeForDisplay
import app.marcdev.hibi.maintabs.MainScreenFragmentDirections
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class EntriesRecyclerViewHolder(private val onSelectClick: View.OnClickListener?, itemView: View, private val theme: Resources.Theme) : BaseEntriesRecyclerViewHolder(itemView) {

  // <editor-fold desc="UI Components">
  private var dateDisplay: TextView = itemView.findViewById(R.id.item_date)
  private var timeDisplay: TextView = itemView.findViewById(R.id.item_time)
  private var contentDisplay: TextView = itemView.findViewById(R.id.item_content)
  private var tagChipGroup: ChipGroup = itemView.findViewById(R.id.cg_main_tags)
  private var tagDisplay: LinearLayout = itemView.findViewById(R.id.lin_main_tags)
  private var locationDisplay: TextView = itemView.findViewById(R.id.txt_item_location)
  // </editor-fold>

  // <editor-fold desc="Other">
  private var displayedItem: MainEntriesDisplayItem? = null
  // </editor-fold>

  private val clickListener = View.OnClickListener {
    val viewEntryAction = MainScreenFragmentDirections.viewEntryAction()
    if(displayedItem != null) {
      viewEntryAction.entryId = displayedItem!!.entry.id
    }
    Navigation.findNavController(itemView).navigate(viewEntryAction)
  }

  init {
    itemView.findViewById<ConstraintLayout>(R.id.const_item_main_recycler).setOnClickListener(clickListener)
  }

  override fun display(item: MainEntriesDisplayItem) {
    this.displayedItem = item
    val dateDisplayText = formatDateForDisplay(item.entry.day, item.entry.month, item.entry.year)
    dateDisplay.text = dateDisplayText
    val timeDisplayText = formatTimeForDisplay(item.entry.hour, item.entry.minute)
    timeDisplay.text = timeDisplayText
    contentDisplay.text = item.entry.content
    displayLocation()
    displayTags()
    if(item.isSelected) {
      val typedValue = TypedValue()
      theme.resolveAttribute(R.attr.hibiSelectedItemColor, typedValue, true)
      itemView.setBackgroundColor(typedValue.data)
    } else {
      itemView.background = null
    }
    val selectedIcon: ImageView = itemView.findViewById(R.id.img_item_selected)
    selectedIcon.show(item.isSelected)
    selectedIcon.setOnClickListener(onSelectClick)
  }

  private fun displayLocation() {
    displayedItem?.let { item ->
      if(item.entry.location.isNotBlank()) {
        locationDisplay.text = item.entry.location
        locationDisplay.show(true)
      } else {
        locationDisplay.show(false)
      }
    }
  }

  private fun displayTags() {
    displayedItem?.let { item ->
      tagDisplay.visibility = if(item.tags.isEmpty()) View.GONE else View.VISIBLE

      tagChipGroup.removeAllViews()
      if(item.tags.isNotEmpty()) {
        item.tags.forEach { tagName ->
          val chip = Chip(itemView.context)
          chip.text = tagName
          chip.scaleX = 0.9f
          chip.scaleY = 0.9f
          tagChipGroup.addView(chip)
        }
      }
    }
  }
}