package com.myoutdoor.agent.fragment

import MyItem
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.myoutdoor.agent.R
import java.lang.String

class MyClusterRenderer(
    context: Context, map: GoogleMap?,
    clusterManager: ClusterManager<MyItem?>?
) : DefaultClusterRenderer<MyItem?>(context, map, clusterManager) {
    private val mClusterIconGeneratorBig = IconGenerator(context)
    private val mClusterIconGeneratorMed = IconGenerator(context)
    private val mClusterIconGeneratorSml = IconGenerator(context)
    val clusterIconBig: Drawable = context.getResources().getDrawable(R.drawable.markgreen)
    val clusterIconMed: Drawable = context.getResources().getDrawable(R.drawable.markgreen)

    init {
        setupIconGen(mClusterIconGeneratorBig, clusterIconBig, context)
        setupIconGen(mClusterIconGeneratorMed, clusterIconMed, context)
       // setupIconGen(mClusterIconGeneratorSml, clusterIconSml, context)
    }

    private fun setupIconGen(generator: IconGenerator, drawable: Drawable, context: Context) {
/*        val textView = TextView(context)
        textView.setTextAppearance(context, R.style.BubbleText)
        textView.setTypeface(App.FONTS.get(2))
        textView.setId(R.id.amu_text)
        textView.setGravity(Gravity.CENTER)
        textView.setLayoutParams(
            LayoutParams(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight()
            )
        )
        generator.setContentView(textView)
        generator.setBackground(drawable)*/
    }

    protected fun onBeforeClusterItemRendered(item: MyItem?, markerOptions: MarkerOptions) {
        val markerDescriptor: BitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
        markerOptions.icon(markerDescriptor)
    }

    protected override fun onBeforeClusterRendered(cluster: Cluster<MyItem?>, markerOptions: MarkerOptions
    ) {
        /*if (cluster.getSize() > 20) {
            val icon: Bitmap = mClusterIconGeneratorBig.makeIcon(String.valueOf(cluster.getSize()))
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
        } else if (cluster.getSize() > 10) {
            val icon: Bitmap = mClusterIconGeneratorMed.makeIcon(String.valueOf(cluster.getSize()))
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
        } else {
            val icon: Bitmap = mClusterIconGeneratorSml.makeIcon(String.valueOf(cluster.getSize()))
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
        }*/
    }


}