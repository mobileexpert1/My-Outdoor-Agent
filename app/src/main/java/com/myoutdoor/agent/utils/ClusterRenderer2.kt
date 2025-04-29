package com.myoutdoor.agent.utils

import MyItem
import android.content.Context
import android.graphics.drawable.Drawable
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.myoutdoor.agent.R


class ClusterRenderer2(
    context: Context,
    map: GoogleMap?,
     clusterManager: ClusterManager<MyItem?>?
) :
    DefaultClusterRenderer<MyItem>(context, map, clusterManager) { private val iconGenerator: IconGenerator
    private val contextN: Context


    init {
        iconGenerator = IconGenerator(context)
        contextN = context
     }

    override fun getBucket(cluster: Cluster<MyItem>): Int {
        return cluster.getSize()
    }

    override fun onClusterItemRendered(clusterItem: MyItem, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)

            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.markgreen))

    }

    override fun onBeforeClusterItemRendered(item: MyItem, markerOptions: MarkerOptions) {

            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markgrey))
    }

    override fun onBeforeClusterRendered(cluster: Cluster<MyItem>, markerOptions: MarkerOptions
    ) {
        val clusterIcon: Drawable = contextN.getResources().getDrawable(R.drawable.markgreen)
        iconGenerator.setBackground(clusterIcon)
        iconGenerator.setTextAppearance(R.color.white)
        val icon = iconGenerator.makeIcon(cluster.getSize().toString())
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }
}


