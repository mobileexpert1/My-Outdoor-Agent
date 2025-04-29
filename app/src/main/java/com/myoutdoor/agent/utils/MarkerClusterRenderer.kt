package com.myoutdoor.agent.utils

import MyItem
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.myoutdoor.agent.R
import com.myoutdoor.agent.models.point_layer.Feature
import java.util.ArrayList


class MarkerClusterRenderer(
    context: Context,
    map: GoogleMap?,
    clusterManager: ClusterManager<MyItem?>?,
    features: ArrayList<Feature>
) :
    DefaultClusterRenderer<MyItem>(context, map, clusterManager) {
    private val iconGenerator: IconGenerator
    private val contextN: Context
    var features: ArrayList<Feature> = ArrayList()
    var markerOptionList: ArrayList<MarkerOptions> = ArrayList()
    private var latLng: LatLng? = null
    private var map: GoogleMap? = null
    private lateinit var clusterManager: ClusterManager<MyItem?>
    init {
        iconGenerator = IconGenerator(context)
        contextN = context
        this.features=features
        this.map=map
        this.markerOptionList=markerOptionList
        this.clusterManager= clusterManager!!
        markerOptionList.add(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location_)))
        markerOptionList.add(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location_)))
        markerOptionList.add(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location_)))
        markerOptionList.add(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location_)))
        markerOptionList.add(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location_)))
    }

    override fun getBucket(cluster: Cluster<MyItem>): Int {
        return cluster.getSize()
    }

    override fun onClusterItemRendered(clusterItem: MyItem, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
       // marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.markgreen))


    }

    override fun setOnClusterClickListener(listener: ClusterManager.OnClusterClickListener<MyItem>?) {
        super.setOnClusterClickListener(listener)
        Log.e("hellooo@@@@","hellooo@@@@");
    }

    override fun setOnClusterItemClickListener(listener: ClusterManager.OnClusterItemClickListener<MyItem>?) {
        super.setOnClusterItemClickListener(listener)
    }

    override fun onBeforeClusterItemRendered(item: MyItem, markerOptions: MarkerOptions) {
      /*  for (i in 1 until features.size) {
            latLng = LatLng(
                features[i].geometry.coordinates.get(1),
                features[i].geometry.coordinates.get(0)
            )
            if (features[i].properties.IsLicensed == 0) {
                Log.e("test1@@@", "test1@@@")
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ellips_green))
            } else {
                Log.e("test2@@@", "test2@@@")
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_))
            }
            //   Log.e("called@@@@@","called@@@@@")
        }*/

       /* Log.e("myitem@@@@",""+item.position);
        for(i in 1 until features.size){
            markerOptionList.add(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location_)))
        }
      Log.e("markerOPtionLIst@@@",markerOptionList.size.toString());*/
   for(i in 1 until features.size){
  /*     latLng= LatLng(features[i].geometry.coordinates.get(1), features[i].geometry.coordinates.get(0))
       Log.e("itemrenderedCalled",""+features[i].properties.IsLicensed);*/
       markerOptionList.add(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location_)))
       if(features[i].properties.IsLicensed==1){
           markerOptionList[i].icon(BitmapDescriptorFactory.fromResource(R.drawable.ellips_green))
       }
       else{
           markerOptionList[i].icon(BitmapDescriptorFactory.fromResource(R.drawable.location_))

       }

      // markerOptionList[i].icon

   }
    }



    override fun onBeforeClusterRendered(cluster: Cluster<MyItem>, markerOptions: MarkerOptions) {

        val clusterIcon: Drawable = contextN.getResources().getDrawable(R.drawable.ellips_green)
        iconGenerator.setBackground(clusterIcon)
        val icon = iconGenerator.makeIcon(cluster.size.toString())
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
        /*if (cluster.getSize() < 10) {
            iconGenerator.setContentPadding(40, 20, 0, 0);
        }
        else {
            iconGenerator.setContentPadding(30, 20, 0, 0);
        }*/

        if (cluster.getSize() < 10) {
            iconGenerator.setContentPadding(40, 20, 0, 0);
        }
        else {
            iconGenerator.setContentPadding(30, 20, 0, 0);
        }
    }

}




/*
class ClusterRenderer internal constructor(
    context: Context?,
    map: GoogleMap?,
    clusterManager: ClusterManager<MyItem?>,
    iconColor: Int

) :
    DefaultClusterRenderer<MyItem>(context, map, clusterManager) {

    private var context: Context? = null
    public val mClusterIconGenerator = IconGenerator(context)
   //  var  features: ArrayList<Feature>?= null
     var  iconColor: Int = -1

    init {
       this.context =context

        this.iconColor=iconColor
    }
    override fun getDescriptorForCluster(cluster: Cluster<MyItem>): BitmapDescriptor {
        mClusterIconGenerator.setBackground(ContextCompat.getDrawable(context!!, R.drawable.markgreen))
        val icon: Bitmap = mClusterIconGenerator.makeIcon(cluster.size.toString())
        return BitmapDescriptorFactory.fromBitmap(icon)
    }
    protected override fun onBeforeClusterItemRendered(
        item: MyItem,
        markerOptions: MarkerOptions
    ) {
    }
   public override fun onClusterItemRendered(item: MyItem, marker: Marker) {
       Log.e("!!!","color   "+iconColor)
       if (iconColor == 1) {
           marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.markgrey))
       } else if(iconColor== 0){
           marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.markgreen))
       }
    }

    override fun getClusterText(bucket: Int): kotlin.String {
        return bucket.toString()
    }
    override fun getBucket(cluster: Cluster<MyItem>): Int {
        return cluster.size
    }

}*/
