import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.ban_giay_mobile.Model.SliderModel
import com.example.ban_giay_mobile.R

class SliderAdapter(
    private var sliderItems: List<SliderModel>,
    private val viewPager2: ViewPager2
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {
    private lateinit var context: Context
    private val runnable = Runnable {
        sliderItems = sliderItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slider_item_container, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.setImage(sliderItems[position], context)
        if (position == sliderItems.size - 1) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int = sliderItems.size

    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageSlider)

        fun setImage(sliderItem: SliderModel, context: Context) {
            val requestOptions = RequestOptions().centerCrop()
            Glide.with(context)
                .load(sliderItem.url)
                .apply(requestOptions)
                .into(imageView)
        }
    }
}

