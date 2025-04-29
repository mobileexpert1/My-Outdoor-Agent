package com.myoutdoor.agent.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.myoutdoor.agent.R
import com.myoutdoor.agent.models.licence.newModel.response.Image
import com.myoutdoor.agent.utils.Constants
import com.squareup.picasso.Picasso


class ViewPagerAdapter(private val context: Context,private val images: List<Image>) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
   // private val images = arrayOf<Int>(R.drawable.layer_2, R.drawable.tents, R.drawable.layer_1)
    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater!!.inflate(R.layout.custom_layout, null)
        val imageView = view.findViewById<View>(R.id.imageView) as ImageView

      //  imageView.setImageResource(images[position])

        if (images.get(position).imageFileName!=null && !images.get(position).imageFileName.equals("")){
            Picasso.get().load(Constants.IMAGE_URL +images.get(position).imageFileName)
                .placeholder(R.drawable.view_pager_logo)
                .error(R.drawable.view_pager_logo)
                .into(imageView)
        }


        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}