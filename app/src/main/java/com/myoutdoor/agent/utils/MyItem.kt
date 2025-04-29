import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

 class MyItem(
    lat: Double,
    lng: Double,
    title: String,
    snippet: String,
    type: String,
   /* idCluster:String,*/
    clusterPosition: Int
) : ClusterItem {

    val latlng: LatLng
    private val title: String
    private val snippet: String
    private val type:String
     private val clusterPosition:Int
    /* private val idCluster:String*/

    override fun getPosition(): LatLng {
        return latlng
    }

     fun getClusterPosition(): Int {
        return clusterPosition
    }

    override fun getTitle(): String? {
        return title
    }

    override fun getSnippet(): String? {
        return snippet
    }
    /* fun getidCluster(): String? {
        return idCluster
    }
*/
     fun getType(): String? {
         return type
     }

    init {
        latlng = LatLng(lat, lng)
        this.title = title
        this.snippet = snippet
        this.type=type
        this.clusterPosition = clusterPosition
      /*  this.idCluster=idCluster*/
    }
}