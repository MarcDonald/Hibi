package app.marcdev.hibi.entryscreens

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.hibi.R
import app.marcdev.hibi.maintabs.mainentriesrecycler.BaseEntriesRecyclerViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ImageRecyclerAdapter(private val context: Context, private val theme: Resources.Theme) : RecyclerView.Adapter<ImageRecyclerViewHolder>() {
  private val inflater: LayoutInflater = LayoutInflater.from(context)
  private var items: List<String> = listOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageRecyclerViewHolder {
    val view = inflater.inflate(R.layout.item_image, parent, false)
    return ImageRecyclerViewHolder(view, theme)
  }

  override fun onBindViewHolder(holder: ImageRecyclerViewHolder, position: Int) {
    holder.display(items[position])
  }

  override fun getItemCount(): Int {
    return items.size
  }

  fun updateItems(newItems: List<String>) {
    items = newItems
    notifyDataSetChanged()
  }
}

class ImageRecyclerViewHolder(itemView: View, private val theme: Resources.Theme) : BaseEntriesRecyclerViewHolder(itemView) {
  private val imageDisplay: ImageView = itemView.findViewById(R.id.img_image)

  fun display(imagePath: String) {
    Glide.with(itemView)
      .load(imagePath)
      .apply(RequestOptions().centerCrop())
      .apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24dp, theme)))
      .into(imageDisplay)
  }
}