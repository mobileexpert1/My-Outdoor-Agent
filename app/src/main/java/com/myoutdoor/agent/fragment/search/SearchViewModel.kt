package com.myoutdoor.agent.fragment.search

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoutdoor.agent.R
import com.myoutdoor.agent.models.RluMapDetails.RLUMapResponse
import com.myoutdoor.agent.models.RluMapDetails.RluBody
import com.myoutdoor.agent.models.fill_map_areas.FillMapAreasResponse
import com.myoutdoor.agent.models.gate_access_points.GateAccessPointsResponse
import com.myoutdoor.agent.models.getallamenities.GetAllAmenitiesResponse
import com.myoutdoor.agent.models.getavailablecountiesbystate.GetAvailableCountiesByStateBody
import com.myoutdoor.agent.models.getavailablecountiesbystate.GetAvailableCountiesByStateResponse
import com.myoutdoor.agent.models.getavailablestates.GetAvailableStatesResponse
import com.myoutdoor.agent.models.multi_polygons.MultipolyegonResponse
import com.myoutdoor.agent.models.point_layer.PointLayerResponse
import com.myoutdoor.agent.models.product_type_rlu.CARluResponse
import com.myoutdoor.agent.models.savedsearches.postsavedsearches.PostSaveSearchesBody
import com.myoutdoor.agent.models.savedsearches.postsavedsearches.PostSaveSearchesResponse
import com.myoutdoor.agent.models.savedsearches.searchautofill.SearchAutoFillBody
import com.myoutdoor.agent.models.savedsearches.searchautofill.SearchAutoFillResponse
import com.myoutdoor.agent.models.search.SearchBody
import com.myoutdoor.agent.models.search.SearchResponse
import com.myoutdoor.agent.models.search.searchV2.SearchV2Response
import com.myoutdoor.agent.models.search.searchV2.body.SearchV2Body
import com.myoutdoor.agent.models.simple_polygen_layer.SimplePolygenResponse
import com.myoutdoor.agent.retrofit.ApiClient
import com.myoutdoor.agent.retrofit.ResponseHandler
import com.myoutdoor.agent.utils.BaseViewModel
import com.myoutdoor.agent.utils.MyResponse
import com.myoutdoor.agent.utils.ShowMessage
import com.myoutdoor.agent.utils.SingleLiveEvent
import com.myoutdoor.agent.utils.isNetworkAvailable
import kotlinx.coroutines.launch
import okhttp3.ResponseBody


/*

class SearchViewModel: ViewModel(){

    var onRLUMapDetailsSuccess = SingleLiveEvent<MyResponse<RLUMapResponse>>()

    fun rluMapDetails(context: Context, rluBody: RluBody, token:String) {
        Log.e("rluMapDetails", "rluBody : " + rluBody)
        viewModelScope.launch {
            if (context.isNetworkAvailable()) {
                onRLUMapDetailsSuccess.value = MyResponse.isLoading()
                try {
                    var apiClient = ApiClient.getApiClientWithHeader(token)!!
                    var response =  apiClient.RluMapDetail(rluBody)
                    Log.e("error_check", "response: " + response)

                    if (response.body()?.statusCode==200) {
                        onRLUMapDetailsSuccess.value = MyResponse.isSuccess(response.body())
                    } else {
                        onRLUMapDetailsSuccess.value = MyResponse.isError((response.body()?.message.toString()))
                    }
                } catch (e: Exception) {
                    Log.e("error_check", "error : " + e.toString())
                    onRLUMapDetailsSuccess.value = MyResponse.isError((e.message))
                }
            } else {
                context.ShowMessage(context.resources.getString(R.string.check_internet_connection_))
            }

        }

    }


    var onParticularPolygenLayerSuccess = SingleLiveEvent<MyResponse<ResponseBody>>()


    fun particluarPolygenLayer(context: Context,where:String,outFields:String,spatialRel:String, f:String) {


        viewModelScope.launch {
            if (context.isNetworkAvailable()) {
                onParticularPolygenLayerSuccess.value = MyResponse.isLoading()
                try {
                    var apiClient = ApiClient.getMapApiClient()!!
                    var response =  apiClient.particularPolygonLayer(where,outFields,spatialRel,f)
                    Log.e("error_check", "response: " + response)

                    if (response.body()!=null) {
                        onParticularPolygenLayerSuccess.value = MyResponse.isSuccess(response.body())
                        Log.e("call","DATA multipolygen  "+response.toString())

                    }
//                    else {
//                        onParticularPolygenLayerSuccess.value = MyResponse.isError((response.body()?.message.toString()))
//                    }
                } catch (e: Exception) {
                    Log.e("error_check", "error : " + e.toString())
                    onParticularPolygenLayerSuccess.value = MyResponse.isError((e.message))
                }
            } else {
                context.ShowMessage(context.resources.getString(R.string.check_internet_connection_))
            }

        }


    }




}

*/

class SearchViewModel: BaseViewModel() {

    var getAvailableStatesResponseSuccess = MutableLiveData<GetAvailableStatesResponse>()
    var getAllAmenitiesResponseSuccess = MutableLiveData<GetAllAmenitiesResponse>()
    var postSaveSearchesResponse = MutableLiveData<PostSaveSearchesResponse>()
    var searchAutoFillResponseSuccess = MutableLiveData<SearchAutoFillResponse>()
    var onPointLayerSuccess = MutableLiveData<PointLayerResponse>()
//    var searchResponseSuccess = MutableLiveData<SearchResponse>()
    var searchResponseSuccess = MutableLiveData<SearchV2Response>()
//    var onParticularPolygenLayerSuccess = MutableLiveData<ResponseBody>()
    var onMultiPolygenLayerSuccess = MutableLiveData<ResponseBody>()
    var multiPolygonLayerFourSuccess = MutableLiveData<ResponseBody>()

    var getAvailableCountiesByStateResponseSuccess = MutableLiveData<GetAvailableCountiesByStateResponse>()
    var onRLUMapDetailsSuccess = MutableLiveData<RLUMapResponse>()
    var gateAccessPointsSuccess = MutableLiveData<GateAccessPointsResponse>()
    var fillMapAreasSuccess = MutableLiveData<FillMapAreasResponse>()
    var selectedStatesSuccess = MutableLiveData<SimplePolygenResponse>()




    fun rluMapDetails(rluBody: RluBody, token:String) {
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


/*
    // Specify Layer

    fun particluarPolygenLayer(where:String,outFields:String,spatialRel:String, f:String) {

        viewModelScope.launch {
            isLoading.value = true

            try {
                isLoading.value = false
                var apiClient = ApiClient.getMapApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.particularPolygonLayer(where,outFields,spatialRel,f)
                )


                Log.e("call","DATA multipolygen  "+response.data!!.toString())

                onParticularPolygenLayerSuccess.value = response.data!!

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }
*/


    // Specify Layer


    var onParticularPolygenLayerSuccess = MutableLiveData<SimplePolygenResponse>()

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

    var particularPolygonFourLayerSuccess = MutableLiveData<SimplePolygenResponse>()


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

    fun selectedStates(stateAbbr:String, f:String) {

        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getMapApiClient()!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.selectedStates(where = "STATE_ABBR='$stateAbbr'",
                       outFields =  "STATE_ABBR",
                        spatialRel = "esriSpatialRelIntersects",
                        f = f)
                )
                isLoading.value = false

                Log.e("call","DATA multipolygen  "+response.data!!.toString())

                selectedStatesSuccess.value = response.data!!

            } catch (e: Exception) {
                Log.e("call"," Exception  "+e)
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }

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



    fun getAvailableCountiesByStateRequest(getAvailableCountiesByStateBody: GetAvailableCountiesByStateBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.getAvailableCountiesByStateRequest(
                        getAvailableCountiesByStateBody
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    getAvailableCountiesByStateResponseSuccess.value = response.data!!
                } else {
                    apiError.value = data.message
                }

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }


    fun searchRequest(searchBody: SearchV2Body, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.searchRequest(
                        searchBody
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    searchResponseSuccess.value = response.data!!
                } else {
                    apiError.value = data.message
                }

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }



    fun searchAutoFillRequest(searchAutoFillBody: SearchAutoFillBody, token:String){

        viewModelScope.launch {
          //  isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.searchAutoFillRequest(
                        searchAutoFillBody
                    )
                )
              //  isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    searchAutoFillResponseSuccess.value = response.data!!
                } else {
                    apiError.value = data.message
                }

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)

                if (exception.message.toString().equals("Something went wrong")){

                }
                else{
                    apiError.value = exception.message.toString()

                }
                isLoading.value = false

            }

        }

    }

    fun postSaveSearchesRequest(postSaveSearchesBody: PostSaveSearchesBody, token:String){

        viewModelScope.launch {
            isLoading.value = true
            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.postSaveSearchesRequest(
                        postSaveSearchesBody
                    )
                )
                isLoading.value = false

                var data = response.data

                if (data!!.statusCode == 200) {
                    postSaveSearchesResponse.value = response.data!!
                } else {
                    apiError.value = data.message
                }

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }

    fun getAllAmenitiesRequest(token:String) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.getAllAmenitiesRequest()
                )
                isLoading.value = false


                var data = response.data

                if (data!!.statusCode == 200) {
                    getAllAmenitiesResponseSuccess.value = data!!

                } else {
                    apiError.value = data.message
                }

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }

    fun getAvailableStatesRequest(token:String) {
        viewModelScope.launch {
//            isLoading.value = true
            isLoading.value = false

            try {
                var apiClient = ApiClient.getApiClientWithHeader(token)!!
                var response = ResponseHandler().handleSuccess(
                    apiClient.getAvailableStatesRequest()
                )
                isLoading.value = false

                Log.e("call","DATA "+response.data!!.toString())

                var data = response.data

                if (data!!.statusCode == 200) {
                    getAvailableStatesResponseSuccess.value = data!!
                } else {
                    apiError.value = data.message
                }

            } catch (e: Exception) {
                var exception = ResponseHandler().handleException<String>(e)
                apiError.value = exception.message.toString()
                isLoading.value = false
            }

        }

    }

    // Point Layer
//    where:1%3D1
//    outFields:*
//    returnGeometry:true
//    f:geojson


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
