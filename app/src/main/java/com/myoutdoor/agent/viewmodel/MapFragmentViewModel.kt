package com.myoutdoor.agent.viewmodel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.models.RluMapDetails.RLUMapResponse
import com.myoutdoor.agent.models.RluMapDetails.RluBody
import com.myoutdoor.agent.models.fill_map_areas.FillMapAreasResponse
import com.myoutdoor.agent.models.gate_access_points.GateAccessPointsResponse
import com.myoutdoor.agent.models.multi_polygons.MultipolyegonResponse
import com.myoutdoor.agent.models.point_layer.PointLayerResponse
import com.myoutdoor.agent.models.simple_polygen_layer.SimplePolygenResponse
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.http.Query


class MapFragmentViewModel: ViewModel() {

    var apiError= MutableLiveData<String>()
    var onRLUMapDetailsSuccess = MutableLiveData<RLUMapResponse>()
    var onMultiPolygenLayerSuccess = MutableLiveData<ResponseBody>()
    var onParticularPolygenLayerSuccess = MutableLiveData<SimplePolygenResponse>()
    var particularPolygonFourLayerSuccess = MutableLiveData<SimplePolygenResponse>()

    var onPointLayerSuccess = MutableLiveData<PointLayerResponse>()
    var isLoading= MutableLiveData<Boolean>()

    //RLUMapDetails

    fun rluMapDetails(rluBody: RluBody,token:String) {

        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.RluMapDetail(
                        rluBody)
                )
                isLoading.value = false

                Log.e("call","DATA "+response.data!!.toString())

                onRLUMapDetailsSuccess.value = response.data!!

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }

    var multiPolygonLayerFourSuccess = MutableLiveData<ResponseBody>()

    fun multiPolygonLayerFour(where:String,outFields:String,returnGeometry:Boolean, f:String) {

        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getMapApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.multiPolygonLayerFour(where,outFields,returnGeometry,f)
                )
                isLoading.value = false

                Log.e("call","DATA multipolygen  "+response.data!!.toString())

                multiPolygonLayerFourSuccess.value = response.data!!

            } catch (e: Exception) {
                Log.e("call"," Exception  "+e)
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }

    var gateAccessPointsSuccess = MutableLiveData<GateAccessPointsResponse>()

    fun gateAccessPoints(where:String,outFields:String,returnGeometry:Boolean, f:String) {

        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getMapApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.gateAccessPoints(where,outFields,returnGeometry,f)
                )
                isLoading.value = false

                Log.e("call","DATA multipolygen  "+response.data!!.toString())

                gateAccessPointsSuccess.value = response.data!!

            } catch (e: Exception) {
                Log.e("call"," Exception  "+e)
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }


    var fillMapAreasSuccess = MutableLiveData<FillMapAreasResponse>()

    fun fillAreas(where:String,outFields:String,returnGeometry:Boolean, f:String) {

        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getMapApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.fillAreas(where,outFields,returnGeometry,f)
                )
                isLoading.value = false

                Log.e("call","DATA multipolygen  "+response.data!!.toString())

                fillMapAreasSuccess.value = response.data!!

            } catch (e: Exception) {
                Log.e("call"," Exception  "+e)
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }





    // Specify Layer

    fun particluarPolygenLayer(where:String,outFields:String,spatialRel:String, f:String) {

        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getMapApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.simplePolygonLayer(where,outFields,spatialRel,f)
                )
                isLoading.value = false

                Log.e("call","DATA multipolygen  "+response.data!!.toString())

                onParticularPolygenLayerSuccess.value = response.data!!

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }

    fun particularPolygonFourLayer(where:String,outFields:String,spatialRel:String, f:String) {

        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getMapApiClient()!!


                var response = ResponseHandler().handleSuccess(
                    apiClient.particularPolygonFourLayer(where,outFields,spatialRel,f)
                )
                isLoading.value = false

                Log.e("call","DATA multipolygen  "+response.data!!.toString())

                particularPolygonFourLayerSuccess.value = response.data!!

            } catch (e: Exception) {
                Log.e("call"," Exception  "+e)
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }



    // MultipleGon Layer

    fun multiPolygenLayer(where:String,outFields:String,returnGeometry:Boolean, f:String) {

        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getMapApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.multiPolygonLayer(where,outFields,returnGeometry,f)
                )
                isLoading.value = false

                Log.e("call","DATA multipolygen  "+response.data!!.toString())

                onMultiPolygenLayerSuccess.value = response.data!!

            } catch (e: Exception) {
                Log.e("call"," Exception  "+e)
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }







    // Point Layer

    fun pointLayer(where: String, outFields: String, returnGeometry: Boolean, f: String) {

        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getMapApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.pointLayer(where,outFields,returnGeometry,f)
                )
                isLoading.value = false

                Log.e("call","DATA "+response.data!!.toString())

                onPointLayerSuccess.value = response.data!!

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }



}