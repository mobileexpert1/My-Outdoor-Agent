package com.myoutdoor.agent.utils

import android.content.Context
import android.content.SharedPreferences

class ShrdPreferences(val context: Context) {
    var pref: SharedPreferences
    var edit: SharedPreferences.Editor

    init{
        pref=context.getSharedPreferences(Constants.SHARED_PREF2,0)
        edit=pref.edit()
    }

    public fun setString(key : String,value : String){
        edit.putString(key,value)
        edit.commit()
    }

    public fun getString(key : String):String{
        return pref.getString(key,"").toString()
    }

    public fun setInt(key : String,value : Int){
        edit.putInt(key,value)
        edit.commit()
    }

    public fun getInt(key : String):Int{
        return pref.getInt(key,0)
    }

    public fun setBoolean(key : String,value : Boolean){
        edit.putBoolean(key,value)
        edit.commit()
    }

    public fun getBoolean(key : String):Boolean{
        return pref.getBoolean(key,false)
    }

    fun clearPref(){
        edit.clear()
        edit.commit()

    }
}