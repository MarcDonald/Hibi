package app.marcdev.hibi.maintabs.mainentriesrecycler

import android.graphics.Canvas
import android.graphics.Rect
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/***
 * 99% taken from this
 * https://gist.github.com/filipkowicz/1a769001fae407b8813ab4387c42fcbd
 * and
 * https://stackoverflow.com/questions/32949971/how-can-i-make-sticky-headers-in-recyclerview-without-external-lib
 */
class MainEntriesHeaderItemDecoration(parent: RecyclerView, private val adapter: EntriesRecyclerAdapter) : RecyclerView.ItemDecoration() {
  private var currentHeader: Pair<Int, RecyclerView.ViewHolder>? = null

  init {
    parent.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
      override fun onChanged() {
        currentHeader = null
      }
    })

    parent.doOnEachNextLayout {
      currentHeader = null
    }

    parent.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
      override fun onInterceptTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent): Boolean {
        return if(motionEvent.action == ACTION_DOWN) {
          motionEvent.y <= currentHeader?.second?.itemView?.bottom ?: 0
        } else false
      }
    })
  }

  private var stickyHeaderHeight = 0

  override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    super.onDrawOver(c, parent, state)

    val topChild = parent.getChildAt(0) ?: return
    val topChildPosition = parent.getChildAdapterPosition(topChild)
    if(topChildPosition != RecyclerView.NO_POSITION) {
      val headerView = getHeaderViewForItem(topChildPosition, parent) ?: return
      val contactPoint = headerView.bottom
      val childInContact = getChildInContact(parent, contactPoint) ?: return
      if(adapter.isHeader(parent.getChildAdapterPosition(childInContact))) {
        moveHeader(c, headerView, childInContact)
        return
      }
      drawHeader(c, headerView)
    }
  }

  private fun getHeaderViewForItem(itemPosition: Int, parent: RecyclerView): View? {
    if(parent.adapter == null)
      return null

    val headerPosition = getHeaderPositionForItem(itemPosition)
    val headerType = parent.adapter?.getItemViewType(headerPosition) ?: return null
    if(currentHeader?.first == headerPosition && currentHeader?.second?.itemViewType == headerType)
      return currentHeader?.second?.itemView

    val headerHolder = parent.adapter?.createViewHolder(parent, headerType)
    if(headerHolder != null) {
      parent.adapter?.onBindViewHolder(headerHolder, headerPosition)
      fixLayoutSize(parent, headerHolder.itemView)
      currentHeader = headerPosition to headerHolder
    }
    return headerHolder?.itemView
  }

  private fun drawHeader(c: Canvas, header: View) {
    c.save()
    c.translate(0f, 0f)
    header.draw(c)
    c.restore()
  }

  private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
    c.save()
    c.translate(0f, (nextHeader.top - currentHeader.height).toFloat())
    currentHeader.draw(c)
    c.restore()
  }

  private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
    var childInContact: View? = null
    for(i in 0 until parent.childCount) {
      val child = parent.getChildAt(i)
      val bounds = Rect()
      parent.getDecoratedBoundsWithMargins(child, bounds)
      if(bounds.bottom > contactPoint) {
        if(bounds.top <= contactPoint) {
          childInContact = child
          break
        }
      }
    }
    return childInContact
  }

  private fun fixLayoutSize(parent: ViewGroup, view: View) {
    val width = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
    val height = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)
    val childWidth = ViewGroup.getChildMeasureSpec(width, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)
    val childHeight = ViewGroup.getChildMeasureSpec(height, parent.paddingTop + parent.paddingBottom, view.layoutParams.height)
    view.measure(childWidth, childHeight)
    stickyHeaderHeight = view.measuredHeight
    view.layout(0, 0, view.measuredWidth, stickyHeaderHeight)
  }

  private fun getHeaderPositionForItem(itemPosition: Int): Int {
    var headerPosition = 0
    var currentPosition = itemPosition
    do {
      if(adapter.isHeader(currentPosition)) {
        headerPosition = currentPosition
        break
      }
      currentPosition -= 1
    } while(currentPosition >= 0)
    return headerPosition
  }
}

inline fun View.doOnEachNextLayout(crossinline action: (view: View) -> Unit) {
  addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ -> action(view) }
}