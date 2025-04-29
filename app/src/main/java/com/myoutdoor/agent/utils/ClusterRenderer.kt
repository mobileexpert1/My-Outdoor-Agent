package com.myoutdoor.agent.utils

import MyItem
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.myoutdoor.agent.R


class ClusterRenderer(
    context: Context,
    map: GoogleMap?,
    clusterManager: ClusterManager<MyItem?>?,
    type: String
) :
    DefaultClusterRenderer<MyItem>(context, map, clusterManager),OnClusterItemClickListener<MyItem> {
    private val iconGenerator: IconGenerator
    private val contextN: Context
    private var type: String=""

    init {
        iconGenerator = IconGenerator(context)
        contextN = context
        this.type =type
        clusterManager?.setOnClusterItemClickListener(null)
     }

    override fun getBucket(cluster: Cluster<MyItem>): Int {
        return cluster.getSize()
    }

    override fun onClusterItemRendered(clusterItem: MyItem, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)

        if(clusterItem.getType().toString()=="0")
        {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.green_newdots))
        }
        else
        {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.grey_newdots))
        }

    }



    override fun onBeforeClusterItemRendered(item: MyItem, markerOptions: MarkerOptions) {
            //  Log.e("called@@@@1","callled@@@@1")
      //      markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markgrey))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBeforeClusterRendered(cluster: Cluster<MyItem>, markerOptions: MarkerOptions) {
        var clusterIcon:Drawable= contextN.getResources().getDrawable(R.drawable.ellips_green)
        iconGenerator.setBackground(clusterIcon)
        iconGenerator.setTextAppearance(R.style.iconGenText)
        val icon = iconGenerator.makeIcon(cluster.getSize().toString())
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun getColor(clusterSize: Int): Int {
        return contextN.resources.getColor(R.color.green)
    }


    override fun onClusterItemClick(item: MyItem?): Boolean {
        TODO("Not yet implemented")
       // Log.e("calleddd####","called####")
    }
}


