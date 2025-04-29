package com.myoutdoor.agent.utils

sealed class MyResponse<T>(var data:T?=null, var error:String?=null) {
//    class isEmpty<T>:MyResponse<T>()
    class isLoading<T>:MyResponse<T>()
    class isSuccess<T>( mData:T?=null):MyResponse<T>(data = mData){
        init {
            // Insert data into the local database upon success
            mData?.let {
                // Assuming loaclDb is an instance of your local database class
            }
        }
    }
    class isError<T>(mError:String?=null):MyResponse<T>(error = mError)
}