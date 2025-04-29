package com.myoutdoor.agent.retrofit

import android.annotation.SuppressLint
import com.google.gson.GsonBuilder
import com.myoutdoor.agent.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    fun getApiClient(): ApiInterface? {
        val gson=GsonBuilder().setLenient().create()

        val httpClient=OkHttpClient.Builder().connectTimeout(3,TimeUnit.MINUTES)
            .writeTimeout(3,TimeUnit.MINUTES)
            .readTimeout(3,TimeUnit.MINUTES)

        httpClient.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build()
            chain.proceed(newRequest)

        }.build()

        val interceptor= HttpLoggingInterceptor()
        interceptor.level=HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor(interceptor)

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.BASE_URL)
            .client(httpClient.build())
            .build()

        return retrofit.create(ApiInterface::class.java)

    }

    @SuppressLint("SuspiciousIndentation")
    fun getApiClientWithHeader(token:String): ApiInterface? {
        val gson=GsonBuilder().setLenient().create()

        val httpClient=OkHttpClient.Builder().connectTimeout(3,TimeUnit.MINUTES)
            .writeTimeout(3,TimeUnit.MINUTES)
            .readTimeout(3,TimeUnit.MINUTES)

          httpClient.addInterceptor { chain ->
              val newRequest = chain.request().newBuilder()
                  .addHeader("Content-Type", "application/json; charset=utf-8")
                  .addHeader("Authorization", token)
                  .addHeader("clientsite", "MOA")
                  .build()
              chain.proceed(newRequest)

          }.build()

        val interceptor= HttpLoggingInterceptor()
        interceptor.level=HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor(interceptor)

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.BASE_URL)
            .client(httpClient.build())
            .build()

        return retrofit.create(ApiInterface::class.java)

    }

    @SuppressLint("SuspiciousIndentation")
    fun getApiClientWithCHeader(token:String): ApiInterface? {

        val gson=GsonBuilder().setLenient().create()
        val httpClient=OkHttpClient.Builder().connectTimeout(3,TimeUnit.MINUTES)
            .writeTimeout(3,TimeUnit.MINUTES)
            .readTimeout(3,TimeUnit.MINUTES)

          httpClient.addInterceptor { chain ->
              val newRequest = chain.request().newBuilder()
                  .addHeader("Authorization", token)
                  .addHeader("clientsite", "MOA")
                  .build()
              chain.proceed(newRequest)

          }.build()

        val interceptor= HttpLoggingInterceptor()
        interceptor.level=HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor(interceptor)

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.BASE_URL)
            .client(httpClient.build())
            .build()

        return retrofit.create(ApiInterface::class.java)

    }

    fun getMapApiClient(): ApiInterface? {
        val gson=GsonBuilder().setLenient().create()

        val httpClient=OkHttpClient.Builder().connectTimeout(3,TimeUnit.MINUTES)
            .writeTimeout(3,TimeUnit.MINUTES)
            .readTimeout(3,TimeUnit.MINUTES)

        httpClient.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build()
            chain.proceed(newRequest)

        }.build()

        val interceptor= HttpLoggingInterceptor()
        interceptor.level=HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(interceptor)

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.MAP_BASE_URL)
            .client(httpClient.build())
            .build()

        return retrofit.create(ApiInterface::class.java)

    }



//    fun getApiClientWithUserTokenHeader(userToken:String,token:String): ApiInterface? {
//
//        val gson=GsonBuilder().setLenient().create()
//
//        val httpClient=OkHttpClient.Builder().connectTimeout(3,TimeUnit.MINUTES)
//            .writeTimeout(3,TimeUnit.MINUTES)
//            .readTimeout(3,TimeUnit.MINUTES)
//
//        httpClient.addInterceptor { chain ->
//            val newRequest = chain.request().newBuilder()
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .addHeader("UserToken", userToken)
//                .addHeader("Token", token)
//                .build()
//            chain.proceed(newRequest)
//        }
//            .build()
//
//        val interceptor= HttpLoggingInterceptor()
//        interceptor.level=HttpLoggingInterceptor.Level.BODY
//
//        httpClient.addInterceptor(interceptor)
//
//        val retrofit = Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .baseUrl(Constants.BASE_URL)
//            .client(httpClient.build())
//            .build()
//
//        return retrofit.create(ApiInterface::class.java)
//
//    }
//
//    fun getApiClient(token : String ): Retrofit? {
//
//        val logging = HttpLoggingInterceptor()
//        logging.level = HttpLoggingInterceptor.Level.BODY
//        val httpClientOne: OkHttpClient.Builder = OkHttpClient.Builder()
//
//        httpClientOne.addInterceptor(logging)
//        // httpClientOne.readTimeout(120, TimeUnit.SECONDS);
//        httpClientOne.connectTimeout(60, TimeUnit.SECONDS);
//        httpClientOne.readTimeout(60, TimeUnit.SECONDS);
//        httpClientOne.writeTimeout(60, TimeUnit.SECONDS);
//
//       /* Log.d(
//            "@SessionToken",
//            pref.getString(Constants.SESSION_TOKEN) + "\n Token : " + pref.getString(Constants.TOKEN)
//        )*/
//
//        httpClientOne.addInterceptor(Interceptor { chain ->
//            var request = chain.request().newBuilder();
//            //request.addHeader("Accept", "application/json")
//          //  request.addHeader("Content-Type", "application/x-www-form-urlencoded")
//            request.addHeader("Token", token)
//            chain.proceed(request.build())
//        })
//
//        val retrofit =
//            Retrofit.Builder().baseUrl(Constants.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(Gson()))
//                .client(httpClientOne.build()).build()
//        return retrofit
//
//    }

}